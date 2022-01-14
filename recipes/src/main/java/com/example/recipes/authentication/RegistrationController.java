package com.example.recipes.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class RegistrationController {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public RegistrationController(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @PostMapping("/api/register")
    public ResponseEntity<?> regUser(@Validated @RequestBody User user) {
        if (user.getEmail().length() < 2 || user.getPassword().length() < 8 || !user.getEmail().contains("."))
            return ResponseEntity.badRequest().build();
        Optional<User> checkUser = userRepository.findByEmail(user.getEmail());
        if (checkUser.isPresent())
            return ResponseEntity.badRequest().build();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
}
