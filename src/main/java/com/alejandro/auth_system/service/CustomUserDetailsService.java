package com.alejandro.auth_system.service;

import com.alejandro.auth_system.entity.Token;
import com.alejandro.auth_system.entity.User;
import com.alejandro.auth_system.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public CustomUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Transactional
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

        Token confirmationToken = new Token(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user

        );
        System.out.println("Enlace de confirmación: http://localhost:8080/register/confirmToken?token="
                + confirmationToken.getToken());
        tokenService.save(confirmationToken);
    }

    public void enableUser(User user) {
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Transactional
    public void confirmToken(String token) {
        Token confirmationToken = tokenService.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("User already confirmed");
        }

        LocalDateTime expiresAt = confirmationToken.getExpiredAt();

        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired");
        }

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        tokenService.save(confirmationToken);

        enableUser(confirmationToken.getUser());
    }
}
