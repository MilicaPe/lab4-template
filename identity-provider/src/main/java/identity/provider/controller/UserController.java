package identity.provider.controller;


import identity.provider.model.Role;
import identity.provider.model.User;
import identity.provider.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.ok(userRepository.save(user));
    }



//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//
//    @PostMapping("/users")
//    @PreAuthorize("hasAuthority('ROLE_Admin')")
//    public ResponseEntity<?> createUser(@RequestBody User user) {
//        if (userRepository.existsByUsername(user.getUsername())) {
//            return ResponseEntity.badRequest().body(Map.of("error", "Username already exists"));
//        }
//
//        if (userRepository.existsByEmail(user.getEmail())) {
//            return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
//        }
//
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setRole(Role.USER);
//        userRepository.save(user);
//
//        return ResponseEntity.ok(Map.of("message", "User created successfully"));
//    }
//
//    @GetMapping("/users/me")
//    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
//        return ResponseEntity.ok(Map.of(
//                "username", jwt.getSubject(),
//                "email", jwt.getClaim("email"),
//                "roles", jwt.getClaim("roles")
//        ));
//    }
//
//    @GetMapping("/all")
//    public List<User> getAllUsers() {
//        return userRepository.findAll();
//    }




}
