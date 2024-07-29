package com.demo.dto;

import lombok.Data;

@Data
public class UpdateUser {
    private String name;
    private String address;
    private String phoneNo;
    private String email;
    private String password;
}
