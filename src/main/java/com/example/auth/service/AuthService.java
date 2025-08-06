package com.example.auth.service;

import com.example.auth.model.User;
import com.example.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User authenticate(String username, String password) {
        try {
            User user = userRepository.findByUsername(username);
            if (user.getPassword().equals(password)) {
                return user;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}