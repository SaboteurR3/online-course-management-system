package com.task.onlinecoursemanagementsystem.security.service;

import com.task.onlinecoursemanagementsystem.common.service.ExceptionUtil;
import com.task.onlinecoursemanagementsystem.security.controller.dto.AuthenticationResponse;
import com.task.onlinecoursemanagementsystem.security.controller.dto.RegisterInstructorRequestDto;
import com.task.onlinecoursemanagementsystem.security.controller.dto.RegisterStudentRequestDto;
import com.task.onlinecoursemanagementsystem.security.user.repository.UserRepository;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.Role;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.User;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.UserStatus;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final JwtService jwtService;

    public AuthenticationResponse registerStudent(RegisterStudentRequestDto request) {
        User user = buildStudent(request);
        return saveUserAndGenerateResponse(user);
    }

    public AuthenticationResponse registerInstructor(RegisterInstructorRequestDto request) {
        User user = buildInstructor(request);
        return saveUserAndGenerateResponse(user);
    }

    private User buildStudent(RegisterStudentRequestDto request) {
        return User.builder()
                .firstName(request.firstname())
                .lastName(request.lastname())
                .userType(UserType.STUDENT)
                .status(UserStatus.ACTIVE)
                .email(request.email())
                .registrationDate(LocalDateTime.now())
                .mobileNumber(request.mobileNumber())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.STUDENT)
                .build();
    }

    private User buildInstructor(RegisterInstructorRequestDto request) {
        return User.builder()
                .firstName(request.firstname())
                .lastName(request.lastname())
                .userType(UserType.INSTRUCTOR)
                .status(UserStatus.ACTIVE)
                .email(request.email())
                .registrationDate(LocalDateTime.now())
                .mobileNumber(request.mobileNumber())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.INSTRUCTOR)
                .build();
    }

    private AuthenticationResponse saveUserAndGenerateResponse(User user) {
        try {
            repository.saveAndFlush(user);
        } catch (Exception e) {
            ExceptionUtil.handleDatabaseExceptions(e, Map.of(
                    "unique_email", "email_already_exists",
                    "unique_mobile_number", "mobile_number_already_exists"));
        }
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        authenticationService.saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
}
