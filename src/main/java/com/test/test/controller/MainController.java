package com.test.test.controller;

import com.test.test.config.EmailServiceImpl;
import com.test.test.config.UserNotSavedException;
import com.test.test.dao.UserDao;
import com.test.test.dao.UserServiceImpl;
import com.test.test.model.JwtRequest;
import com.test.test.securiy.AuthUser;
import com.test.test.securiy.JwtResponse;
import com.test.test.securiy.JwtTokenUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
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

    private Logger log = LoggerFactory.getLogger(MainController.class);

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
    public ResponseEntity<JwtResponse> getOauth2Token(@RequestBody JwtRequest jwtRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getEmail(),jwtRequest.getPassword()));

        final UserDetails userDetails = authUser.loadUserByUsername(jwtRequest.getEmail());
        String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/user")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public UserDao createUser(@RequestBody UserDao userDao){

        if(userDao == null) {
            throw new IllegalArgumentException("User can't be null");
        }
        else if(userDao.getEmail().isEmpty() || !userDao.getEmail().contains("@")) {
            throw new UnsupportedOperationException("Unsupported email format");
        }
        else if(userDao.getPassword().isEmpty()){
            throw new IllegalArgumentException("Password can't be empty");
        }
        else if(userDao.getPhone().isEmpty() || userDao.getPhone().length() !=11){
            throw new IllegalArgumentException("Phone number should be 11 digits");
        }

        userDao.setDateRegistered(LocalDate.now());
        if(userService.findUserByEmail(userDao.getEmail()) != null){
            throw new DuplicateKeyException("Duplicate email");
        }
        UserDao savedUser = userService.save(userDao);
        if(savedUser==null) throw new UserNotSavedException("Error user not saved");

        //send On-boarding email to user email address
        try{
            sendOnboardingMail(savedUser);
        }catch (MailAuthenticationException ex){
            log.error(ex.getLocalizedMessage());
        }
        return savedUser;
    }

    @GetMapping("/users")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public List<UserDao> getUsers(@RequestParam("page") int page,@RequestParam("size") int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<UserDao> list = userService.getUserList(pageable);
        return list.getContent();
    }


    @GetMapping("/user/{email}")
    @ApiOperation(value = "The Api is for getting a user by email")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public UserDao getUserByEmail(@PathVariable String email){
        UserDao userDao = userService.findUserByEmail(email);
        if(userDao == null) throw new UsernameNotFoundException("User not found");

        return userDao;

    }

    @PutMapping("/user/{id}")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
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
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public Map<String,String> deleteUser(@PathVariable Long id){
        UserDao deletedUser = userService.getUserById(id).map(userDao -> {
            userDao.setStatus(1);
            userDao.setDateDeactivated(LocalDate.now());
            return userService.save(userDao);
        }).get();

        if(deletedUser.getStatus() == 1){

            Map<String, String> response = new HashMap<>();
            response.put("message","success");

            try{
                //send Off-boarding email to deactivated user
                sendOffboardingMail(deletedUser);
            }catch (MailAuthenticationException ex){
                log.error(ex.getLocalizedMessage());
            }


            return response;
        }

        userService.deleteUser(id);
        Map<String, String> response = new HashMap<>();
        response.put("message","error deleting user");

        return response;
    }

    public void sendOnboardingMail(UserDao userDao){
        String subject = "On-Boarding Notification";
        String message = "Hi "+userDao.getFirstName()+",\n"+
                "This mail is to inform you that a user account with "+userDao.getRole()+
                "access has been created for you.\n"+
                "Cheers";
        emailService.sendSimpleMessage(userDao.getEmail(),subject, message);
    }

    public void sendOffboardingMail(UserDao userDao){
        String subject = "On-Boarding Notification";
        String message = "Hi "+userDao.getFirstName()+",\n"+
                "Your user account with "+userDao.getRole()+
                "access has been deactivated.\n"+
                "Regards";
        emailService.sendSimpleMessage(userDao.getEmail(),subject, message);
    }
}
