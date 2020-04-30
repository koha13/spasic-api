package music.server.controllers;

import java.io.IOException;

import javax.validation.Valid;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import music.server.models.ChangePasswordRequest;
import music.server.models.LoginRequest;
import music.server.models.LoginResponse;
import music.server.models.SignupRequest;
import music.server.repositories.UserRepository;
import music.server.services.JwtService;
import music.server.services.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtService jwtService;

    @PostMapping("/auth/signup")
    public LoginResponse signup(@Valid @RequestBody SignupRequest signupRequest) throws Exception {
        return userService.signup(signupRequest);
    }

    @PostMapping("/auth/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.getLoginResponse(loginRequest.getUsername(), loginRequest.getPassword());
    }

    @GetMapping("/auth/verify/{token}")
    public LoginResponse checkToken(@PathVariable String token)
            throws JsonParseException, JsonMappingException, IOException {
        return userService.checkToken(token);
    }

    @PostMapping("/auth/changepass")
    public LoginResponse changePass(@Valid @RequestBody ChangePasswordRequest c) {
        return userService.changePass(c);
    }

    @PostMapping("/like/{idSong}")
    public void likeSong(@PathVariable String idSong) {
        userService.likeSong(Integer.parseInt(idSong));
    }

    @PostMapping("/unlike/{idSong}")
    public void unlikeSong(@PathVariable String idSong) {
        userService.unlikeSong(Integer.parseInt(idSong));
    }
}