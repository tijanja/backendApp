package com.test.test;

import com.test.test.controller.MainController;
import com.test.test.securiy.AuthUser;
import com.test.test.securiy.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TestApplicationTests {

	@Autowired
	MainController mainController;

	@Autowired
	AuthUser userAuth;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Test
	void authtokenTest() {
		boolean condition = false;
		final UserDetails userDetails = User.withUsername("adetunji@gmail.com").password("Project123").roles("User").build();
		String token = jwtTokenUtil.generateToken(userDetails);
		if(token != null){
			condition = true;
		}
		assertTrue(condition,"Should return token");
	}

}
