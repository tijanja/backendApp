package com.test.test.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/** User Service an interface to simplify data persist process */

public interface UserService {
    // Method to save instance of a user
    UserDao save(UserDao userDao);
    // Method to retrieve all users
    Page<UserDao> getUserList(Pageable pageable);
    // Method to update the user data
    UserDao update(UserDao userDao);
    // Method to retrieve user data by Id
    List<UserDao> getAll();
    // Method to deactivate a user by Id
    void deleteUser(Long id);
    //get user by their email address
    UserDao findUserByEmail(String clientId);
}
