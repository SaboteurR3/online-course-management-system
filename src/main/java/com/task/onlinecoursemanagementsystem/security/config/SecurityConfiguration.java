package com.task.onlinecoursemanagementsystem.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static com.task.onlinecoursemanagementsystem.security.user.repository.entity.Role.INSTRUCTOR;
import static com.task.onlinecoursemanagementsystem.security.user.repository.entity.Role.STUDENT;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf()
                .disable()
                .cors(corsConfig ->
                        corsConfig.configurationSource(corsConfigurationSource()))
                .exceptionHandling(config ->
                        config.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .authorizeHttpRequests(
                        configurer -> configurer // TODO correct apis urls
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/swagger/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/profile").hasAnyRole(STUDENT.name(), INSTRUCTOR.name())
                                .requestMatchers(HttpMethod.POST, "registration/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "student/session/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "instructor/session/**").permitAll()
                                .requestMatchers("/instructor/courses/**").hasAnyRole(INSTRUCTOR.name())
                                .requestMatchers("/instructor/lessons").hasAnyRole(INSTRUCTOR.name())
                                .requestMatchers("/instructor/lessons/**").hasAnyRole(INSTRUCTOR.name())
                                .requestMatchers("/student/enrollments").hasAnyRole(STUDENT.name())
                                .requestMatchers("/student/enrollments/**").hasAnyRole(STUDENT.name())
                )
                .sessionManagement(configurer ->
                        configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                .and().build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of(allowedOrigins));
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));
        configuration.setAllowedHeaders(List.of("origin", "content-type", "accept", "authorization", "enctype", "content-disposition"));
        configuration.setMaxAge((long) (10 * 60));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
