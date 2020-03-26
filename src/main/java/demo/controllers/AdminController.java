package demo.controllers;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import demo.boundaries.UserBoundary;
import demo.database.Database;
import demo.helpers.UserHelper;

@RestController
public class AdminController {
	
	/*--------------------- GET APIS ------------------- */
	
	@RequestMapping(path = "/acs/elements/{adminDomain}/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserBoundary> getAllUsers(@PathVariable("adminDomain") String adminDomain,@PathVariable("adminEmail") String adminEmail) {
		
		if(UserHelper.isLoggedIn(adminDomain,adminEmail)) {
			if(UserHelper.isAdmin(adminDomain,adminEmail))
			
			//Some tests
			System.out.println("userDoamin = " + adminDomain);
			System.out.println("userEmail = " + adminEmail);
			return Database.getAllUsers();
		} 
		//User is not logged in.
		return null;				
	}
	
	/*--------------------- DELETE APIS ------------------- */
	
	@RequestMapping(path = "/acs/elements/{adminDomain}/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteMessage () {
		
		Database.deleteAllActions();
	}




}
