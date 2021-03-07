package com.test.test.controller;

import com.test.test.config.EmailServiceImpl;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AuthUser authUser;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private EmailServiceImpl emailService;

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

        if(userDao.getRole().isEmpty()){
            userDao.setRole("Admin");
        }
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

    @PutMapping("/user/{id}")
    public UserDao updateUser(@RequestBody UserDao userDao, @PathVariable Long id){
        System.out.print(userDao.toString());
        return userService
                .getUserById(id)
                .map(userDao1 -> {
                    if(userDao.getEmail()!=null && !userDao.getEmail().isEmpty()){
                        userDao1.setEmail(userDao.getEmail());
                    }
                    else if(userDao.getFirstName()!=null && !userDao.getFirstName().isEmpty()){
                        userDao1.setFirstName(userDao.getFirstName());
                    }
                    else if(userDao.getLastName()!=null && !userDao.getLastName().isEmpty()){
                        userDao1.setLastName(userDao.getLastName());
                    }
                    else if(userDao.getRole()!=null && !userDao.getRole().isEmpty()){
                        userDao1.setRole(userDao.getRole());
                    }
                    else if(userDao.getPhone()!=null && !userDao.getPhone().isEmpty()){
                        userDao1.setPhone(userDao.getPhone());
                    }
                    else if(userDao.getTitle()!=null && !userDao.getTitle().isEmpty()){
                        userDao1.setTitle(userDao.getTitle());
                    }
                    UserDao updatedUser = userService.save(userDao1);
                    updatedUser.setPassword(null);
                    return updatedUser;
                }).get();
    }

    @DeleteMapping("/user/{id}")
    public Map<String,String> deleteUser(@PathVariable Long id){
        UserDao deletedUser = userService.getUserById(id).map(userDao -> {
            userDao.setStatus(1);
            userDao.setDateDeactivated(LocalDate.now());
            return userService.save(userDao);
        }).get();

        if(deletedUser.getStatus() == 1){

            Map<String, String> response = new HashMap<>();
            response.put("message","success");
            return response;
        }

        userService.deleteUser(id);
        Map<String, String> response = new HashMap<>();
        response.put("message","error deleting user");

        return response;
    }

    public void sendmail(){
        emailService.sendSimpleMessage("adetunji.akinde@techadvance.ng","test", "testing....");
    }
}
