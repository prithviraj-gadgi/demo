package com.demo.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String message;
}
