package com.test.test.controller;

import com.test.test.config.UserNotSavedException;
import com.test.test.dao.UserDao;
import com.test.test.dao.UserServiceImpl;
import com.test.test.securiy.AuthUser;
import com.test.test.securiy.JwtResponse;
import com.test.test.securiy.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    AuthUser authUser;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/oauth/token")
    public ResponseEntity<?> getOauth2Token(@RequestBody Map<String, String> payload){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.get("email"),payload.get("password")));

        final UserDetails userDetails = authUser.loadUserByUsername(payload.get("email"));
        String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/user")
    public UserDao createUser(@RequestBody UserDao userDao){

        if(userDao == null) throw new IllegalArgumentException("User can't be null");
        if(userDao.getEmail().isEmpty() || !userDao.getEmail().contains("@")) throw new UnsupportedOperationException("Unsupported email format");
        if(userDao.getPassword().isEmpty()) throw new IllegalArgumentException("Password can't be empty");
        if(userDao.getPhone().isEmpty() || userDao.getPhone().length() !=11) throw new IllegalArgumentException("Phone number should be 11 digits");

        userDao.setDateRegistered(LocalDate.now());
        UserDao savedUser = userService.save(userDao);
        if(savedUser==null) throw new UserNotSavedException("Error user not saved");

        return savedUser;
    }

    @GetMapping("/users")
    public List<UserDao> getUsers(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserDao> list = userService.getUserList(pageable);
        return list.getContent();
    }


    @GetMapping("/user/{email}")
    public UserDao getUser(@PathVariable String email){

        UserDao userDao = userService.findUserByEmail(email);
        if(userDao == null) throw new UsernameNotFoundException("User not found");

        return userDao;

    }
}
