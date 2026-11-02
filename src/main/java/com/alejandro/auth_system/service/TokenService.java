package com.alejandro.auth_system.service;

import com.alejandro.auth_system.entity.Token;
import com.alejandro.auth_system.repository.TokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository){
        this.tokenRepository=tokenRepository;
    }

    public Optional<Token> findByToken(String token){
        return tokenRepository.findByToken(token);
    }

    public Token save(Token token){
        return tokenRepository.save(token);
    }
}
