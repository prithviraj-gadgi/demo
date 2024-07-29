package com.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @Pattern(regexp = "^[a-z0-9_]+$", message = "username can contain only small letters, digits, and underscore")
    private String username;

    @NotEmpty(message = "name can't be empty")
    private String name;

    @NotEmpty(message = "address can't be empty")
    private String address;

    @Column(unique = true)
    @NotEmpty(message = "phoneNo can't be empty")
    @Size(min = 10, max = 10, message = "phoneNo must have 10 digits")
    private String phoneNo;

    @Column(unique = true)
    @NotEmpty(message = "email can't be empty")
    private String email;

    @NotEmpty(message = "password can't be empty")
    private String password;
}
