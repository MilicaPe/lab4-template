package identity.provider.service;

import identity.provider.model.Role;
import identity.provider.model.User;
import identity.provider.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;



//    public User register(JwtAuthenticationRequest jwtAuthenticationRequest){
//        User user = new User();
//        user.setEmail(jwtAuthenticationRequest.getEmail());
//        user.setPassword(passwordEncoder.encode(jwtAuthenticationRequest.getPassword()));
//        user.setRole(Role.USER);
//        return userRepository.save(user);
//    }

//    public boolean userExists(String username){
//        return userRepository.existsByUsername(username);
//    }


//    public User findByUsername(String username) {
//        return this.userRepository.findByUsername(username);
//    }

//    public boolean findByUsernameAndPassword(String username, String password) {
//        return userRepository.existsByUsernameAndPassword(username, password);
//    }
//
//    public String getUserRole(String username) {
//        return this.userRepository.findByUsername(username).getRole();
//    }
}
