package music.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import music.server.config.CustomUserDetails;
import music.server.entities.User;
import music.server.repositories.UserRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null)
            throw new InternalAuthenticationServiceException("Khong tim thay user");
        return new CustomUserDetails(user);

    }

    public UserDetails loadUserById(Integer id) throws UsernameNotFoundException {
        User user = userRepository.findById(id).get();
        if (user == null)
            throw new InternalAuthenticationServiceException("Unauthorized. Username/password incorrected.");

        return new CustomUserDetails(user);
    }
}