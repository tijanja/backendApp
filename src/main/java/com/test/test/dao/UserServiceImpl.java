package com.test.test.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDao save(UserDao userDao) {
        userDao.setPassword(passwordEncoder.encode(userDao.getPassword()));
        return userRepository.save(userDao);
    }

    @Override
    public Page<UserDao> getUserList(Pageable pageable) {
        return userRepository.getAllActiveUsers(pageable);
    }

    @Override
    public UserDao update(UserDao userDao) {
        return null;
    }

    @Override
    public List<UserDao> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deactivateUser(id);
    }

    @Override
    public UserDao findUserByEmail(String email) {

        return userRepository.findUseByEmail(email);
    }

    @Override
    public Optional<UserDao> getUserById(Long id) {
        return userRepository.findById(id);
    }
}
