package demo.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import demo.boundaries.MessageBoundary;
import demo.boundaries.UserBoundary;
import demo.element.NewUserDetails;
import demo.element.UserId;
import demo.helpers.UserHelper;

@RestController
public class UserController {
	
	
	/*--------------------- GET APIS ------------------- */

	//Login user get request
	@RequestMapping(path = "/acs/users/login/{userDomain}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary login(@PathVariable("userDomain") String userDomain, @PathVariable("userEmail") String userEmail) {
		if(UserHelper.isSignedIn(userEmail, userEmail)) {
			return new UserBoundary(new UserId(userDomain,userEmail),"role", "userName","avatar");
		}
		return null;
	}
	
	/*--------------------- POST APIS ------------------- */

	
	//Create a new user
	@RequestMapping(path = "/acs/users",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary createNewUser (@RequestBody NewUserDetails input) {
		UserBoundary ub=new UserBoundary(new UserId("2020b.lior_trachtman",input.getEmail()), input.getRole()
				, input.getUserName(), input.getAvatar());
		return ub;
	}
	
	/*--------------------- PUT APIS ------------------- */

	
	//Update user details
	@RequestMapping(path = "/acs/users/{userDomain}/{userEmail}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateUserDetails (
			@PathVariable("userDomain") String userDomain, @PathVariable("userEmail") String userEmail, 
			@RequestBody UserBoundary update) {
		if(UserHelper.isLoggedIn(userDomain, userEmail)) {
			// TODO update user by userDomain and userEmail
		}
	}
	
	


}