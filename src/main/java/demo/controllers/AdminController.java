package demo.controllers;

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
	
	@RequestMapping(path = "/acs/admin/users/{adminDomain}/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] getAllUsers(@PathVariable("adminDomain") String adminDomain,@PathVariable("adminEmail") String adminEmail) {
		
		if(UserHelper.isLoggedIn(adminDomain,adminEmail)) {
			if(UserHelper.isAdmin(adminDomain,adminEmail))
			return Database.getAllUsers().toArray(new UserBoundary[0]);
		} 
		//User is not logged in.
		return null;				
	}
	
	/*--------------------- DELETE APIS ------------------- */
	
	@RequestMapping(path = "/acs/admin/actions/{adminDomain}/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllActions() {
		
		Database.deleteAllActions();
	}

	@RequestMapping(path = "/acs/admin/elements/{adminDomain}/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllElements() {
		
		Database.deleteAllElements();
	}


}
