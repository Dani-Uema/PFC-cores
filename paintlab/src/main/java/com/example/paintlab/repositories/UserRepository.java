package com.example.paintlab.repositories;

import com.example.paintlab.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    UserDetails findByEmail(String email);
    User findUserByEmail(String email);
}
