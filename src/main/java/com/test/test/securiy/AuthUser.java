package com.test.test.securiy;

import com.test.test.dao.UserDao;
import com.test.test.dao.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthUser implements UserDetailsService {

    @Autowired
    private UserServiceImpl userService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDao userDAO = userService.findUserByEmail(email);
        if(userDAO==null) throw new UsernameNotFoundException(email);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(userDAO.getRole()));
        return User.withUsername(userDAO.getEmail()).password(userDAO.getPassword()).authorities(grantedAuthorities).build();
    }
}
