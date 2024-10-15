package com.task.onlinecoursemanagementsystem.security.user.controller;

import lombok.Builder;

@Builder
public record UserDTO(
         String firstName,
         String lastname,
         String email,
         String phoneNumber,
         String image
) {
}
