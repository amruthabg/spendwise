package com.gaonkar.spendwise.service;

import com.gaonkar.spendwise.dto.UserRegisterRequest;
import com.gaonkar.spendwise.exception.BadRequestException;
import com.gaonkar.spendwise.exception.ResourceNotFoundException;
import com.gaonkar.spendwise.model.User;
import com.gaonkar.spendwise.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User registerUser(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }
        User user = new User(request.getName(), request.getEmail(), request.getPassword());
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}
