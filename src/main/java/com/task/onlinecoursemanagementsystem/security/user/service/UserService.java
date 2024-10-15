package com.task.onlinecoursemanagementsystem.security.user.service;

import com.task.onlinecoursemanagementsystem.exception.SecurityViolationException;
import com.task.onlinecoursemanagementsystem.security.user.controller.UserDTO;
import com.task.onlinecoursemanagementsystem.security.user.repository.UserRepository;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User lookUpUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(SecurityViolationException::new);
    }

    public User lookUpUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(SecurityViolationException::new);
    }

    public UserDTO getProfile() {
        User user = curentUser();
        return UserDTO.builder()
                .firstName(user.getFirstName())
                .lastname(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getMobileNumber())
                .build();
    }

    public User curentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((User) authentication.getPrincipal()).getUsername();
        return lookUpUserByEmail(username);
    }
}
