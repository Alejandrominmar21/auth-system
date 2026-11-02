package com.alejandro.auth_system.service;

import com.alejandro.auth_system.entity.User;
import com.alejandro.auth_system.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public void registerUser(User user) {
        userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail())
                .ifPresent(
                        existingUser -> {
                            throw new IllegalStateException("El usuario ya existe");
                        });

        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        // user.setEnabled(true);
        userRepository.save(user);
    }
}
