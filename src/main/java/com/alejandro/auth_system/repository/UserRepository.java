package com.alejandro.auth_system.repository;

import org.springframework.data.repository.CrudRepository;
import com.alejandro.auth_system.entity.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameOrEmail(String username, String email);
}

