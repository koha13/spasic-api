package music.server.services;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import music.server.config.CustomUserDetails;
import music.server.entities.User;
import music.server.exceptionhandle.ApiMissingException;
import music.server.exceptionhandle.UsernameIsAlreadyTakenException;
import music.server.models.ChangePasswordRequest;
import music.server.models.LoginResponse;
import music.server.models.SignupRequest;
import music.server.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private SongService songService;

    public LoginResponse signup(SignupRequest signupRequest) throws Exception {
        User userAdmin = getCurrentUser();
        if (userAdmin.getRole().compareTo("admin") != 0)
            throw new Exception("Only admin can add use");
        User userCheck = userRepository.findByUsername(signupRequest.getUsername());
        if (userCheck != null) {
            throw new UsernameIsAlreadyTakenException();
        }
        String username = signupRequest.getUsername();
        String password = signupRequest.getPassword();
        String passwordEncode = passwordEncoder.encode(password);
        String role = signupRequest.getRole();

        User user = new User(username, passwordEncode, role);
        userRepository.save(user);
        return getLoginResponse(username, password);
    }

    private User login(String username, String password) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = getCurrentUser();
        return user;
    }

    public LoginResponse getLoginResponse(String username, String password) {
        User user = login(username, password);
        String jwt = jwtService.generateToken(new CustomUserDetails(user));
        LoginResponse loginResponse = new LoginResponse(user.getId(), user.getUsername(), jwt, user.getRole());
        return loginResponse;
    }

    public LoginResponse checkToken(String token) throws JsonParseException, JsonMappingException, IOException {
        User user = jwtService.getUserFromToken(token);
        if (user != null) {
            String jwt = jwtService.generateToken(new CustomUserDetails(user));
            LoginResponse loginResponse = new LoginResponse(user.getId(), user.getUsername(), jwt, user.getRole());
            return loginResponse;
        } else
            throw new RuntimeException("Token is invalid!");
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new ApiMissingException();
        }
        CustomUserDetails cusUser = (CustomUserDetails) authentication.getPrincipal();
        return cusUser.getUser();
    }

    public User getUserRepo() {
        int id = getCurrentUser().getId();
        return userRepository.findById(id).get();
    }

    public LoginResponse changePass(ChangePasswordRequest c) {
        User user = getCurrentUser();
        if (passwordEncoder.matches(c.getOldPass(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(c.getNewPass()));
            userRepository.save(user);
            String jwt = jwtService.generateToken(new CustomUserDetails(user));
            LoginResponse loginResponse = new LoginResponse(user.getId(), user.getUsername(), jwt, user.getRole());
            return loginResponse;
        } else
            throw new RuntimeException("Old password not match");
    }

    public void likeSong(int idSong) {
        User user = getCurrentUser();
        User userRepo = userRepository.findById(user.getId()).get();
        userRepo.likeSong(songService.getSongById(idSong));
        userRepository.save(userRepo);
    }

    public void unlikeSong(int idSong) {
        User user = getCurrentUser();
        User userRepo = userRepository.findById(user.getId()).get();
        userRepo.unlikeSong(songService.getSongById(idSong));
        userRepository.save(userRepo);
    }
}