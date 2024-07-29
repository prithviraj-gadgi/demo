package com.demo.service;

import com.demo.dto.UpdateUser;
import com.demo.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User saveUser(User user);
    User getUser(String userName);
    User updateUser(String userName, UpdateUser updateUser);
    String deleteUser(String userName);

}
