package com.task.onlinecoursemanagementsystem.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.onlinecoursemanagementsystem.exception.BusinessException;
import com.task.onlinecoursemanagementsystem.security.controller.dto.AuthenticationRequestDto;
import com.task.onlinecoursemanagementsystem.security.controller.dto.AuthenticationResponse;
import com.task.onlinecoursemanagementsystem.security.controller.dto.RegisterInstructorRequestDto;
import com.task.onlinecoursemanagementsystem.security.controller.dto.RegisterStudentRequestDto;
import com.task.onlinecoursemanagementsystem.security.token.model.Token;
import com.task.onlinecoursemanagementsystem.security.token.model.TokenType;
import com.task.onlinecoursemanagementsystem.security.token.repository.TokenRepository;
import com.task.onlinecoursemanagementsystem.security.user.repository.UserRepository;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.Role;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.User;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.UserStatus;
import com.task.onlinecoursemanagementsystem.security.user.repository.entity.UserType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse registerStudent(RegisterStudentRequestDto request) {
        checkIfEmailOrPhoneExists(request.email(), request.mobileNumber());
        var user = buildStudent(request);
        return saveUserAndGenerateResponse(user);
    }

    public AuthenticationResponse registerInstructor(RegisterInstructorRequestDto request) {
        checkIfEmailOrPhoneExists(request.email(), request.mobileNumber());
        var user = buildInstructor(request);
        return saveUserAndGenerateResponse(user);
    }

    public void checkIfEmailOrPhoneExists(String email, String mobileNumber) {
        Optional<User> byEmail = repository.findByEmail(email);
        Optional<User> byPhone = repository.findByMobileNumber(mobileNumber);
        if (byEmail.isPresent() || byPhone.isPresent()) {
            throw new BusinessException("email_or_phone_number_exists");
        }
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
        repository.saveAndFlush(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        var user = repository.findByEmail(request.email()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.repository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
