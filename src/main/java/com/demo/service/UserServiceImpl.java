package com.demo.service;

import com.demo.dto.UpdateUser;
import com.demo.exception.ResourceAlreadyExistsException;
import com.demo.exception.UserNotFoundException;
import com.demo.model.User;
import com.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(User user) {
        if (userRepository.existsById(user.getUsername())) {
            throw new ResourceAlreadyExistsException("User already exists with username: " + user.getUsername());
        }
        if (userRepository.existsByPhoneNo(user.getPhoneNo())) {
            throw new ResourceAlreadyExistsException("User already exists with phoneNo: " + user.getPhoneNo());
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResourceAlreadyExistsException("User already exists with email: " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User getUser(String userName) {
        Optional<User> optionalUser = userRepository.findById(userName);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new UserNotFoundException("User doesn't exists by username: " + userName);
        }
    }

    @Override
    public User updateUser(String userName, UpdateUser updateUser) {
        Optional<User> optionalUser = userRepository.findById(userName);
        if (optionalUser.isPresent()) {
            if (updateUser.getName() != null) {
                optionalUser.get().setName(updateUser.getName());
            }
            if (updateUser.getAddress() != null) {
                optionalUser.get().setAddress(updateUser.getAddress());
            }
            if (updateUser.getPhoneNo() != null) {
                optionalUser.get().setPhoneNo(updateUser.getPhoneNo());
            }
            if (updateUser.getEmail() != null) {
                optionalUser.get().setEmail(updateUser.getEmail());
            }
            if (updateUser.getPassword() != null) {
                optionalUser.get().setPassword(updateUser.getPassword());
            }
            return userRepository.save(optionalUser.get());
        } else {
            throw new UserNotFoundException("Can't update, user doesn't exists by username: " + userName);
        }
    }

    @Override
    public String deleteUser(String userName) {
        Optional<User> optionalUser = userRepository.findById(userName);
        if (optionalUser.isPresent()) {
            userRepository.deleteById(userName);
            return "User deleted by username: " + userName;
        } else {
            throw new UserNotFoundException("User not found with username: " + userName);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUser(username);

        //Creating a dummy set of authorities, need to add roles User model to have authorities
        Set<String> roles = new HashSet<>();
        roles.add("ADMIN");
        roles.add("USER");

        return new org.springframework.security.core.userdetails.User(
                username,
                user.getPassword(),
                roles.stream().map(SimpleGrantedAuthority::new).toList()
        );
    }

}
