package com.task.onlinecoursemanagementsystem.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

}
