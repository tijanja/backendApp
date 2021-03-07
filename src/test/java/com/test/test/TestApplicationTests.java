package com.test.test;

import com.test.test.controller.MainController;
import com.test.test.dao.UserDao;
import com.test.test.securiy.AuthUser;
import com.test.test.securiy.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

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

	@Test
	void userRegistrationTest() {
		UserDao userDao = new UserDao();
		userDao.setFirstName("Frank");
		userDao.setLastName("Akinde");
		userDao.setEmail("Frank007@gmail.com");
		userDao.setPassword("Project123");
		userDao.setPhone("08060000000");
		assertEquals(userDao,mainController.createUser(userDao),"Return value should be instance of the user saved");
	}

	@Test
	void getAllusersTest(){

		boolean condition = false;
		List<UserDao> pages = mainController.getUsers();
		System.out.print(pages.size());
		if(pages.size()>0) {
			condition = true;
		}
		assertTrue(condition,"condition should be true");

	}

	@Test
	void getUserByEmail(){
		String expected = "adetunjiakinde@gmail.com";
		UserDao userDao = mainController.getUser(expected);
		assertEquals(expected,userDao.getEmail(),"Returned email should be the same as the value in expected");
	}

}
