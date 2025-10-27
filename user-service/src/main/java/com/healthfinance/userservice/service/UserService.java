package com.healthfinance.userservice.service;

import com.healthfinance.userservice.dto.UserResponse;
import com.healthfinance.userservice.exception.ResourceNotFoundException;
import com.healthfinance.userservice.model.User;
import com.healthfinance.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserResponse.fromUser(user);
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserResponse.fromUser(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::fromUser)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse updateUser(String id, User updateData) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (updateData.getFirstName() != null) user.setFirstName(updateData.getFirstName());
        if (updateData.getLastName() != null) user.setLastName(updateData.getLastName());
        if (updateData.getPhoneNumber() != null) user.setPhoneNumber(updateData.getPhoneNumber());
        if (updateData.getAddress() != null) user.setAddress(updateData.getAddress());
        if (updateData.getCity() != null) user.setCity(updateData.getCity());
        if (updateData.getState() != null) user.setState(updateData.getState());
        if (updateData.getZipCode() != null) user.setZipCode(updateData.getZipCode());

        user = userRepository.save(user);
        return UserResponse.fromUser(user);
    }

    @Transactional
    public void deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setIsActive(false);
        userRepository.save(user);
    }
}
