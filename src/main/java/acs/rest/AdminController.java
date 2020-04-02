package acs.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import acs.boundaries.ActionBoundary;
import acs.boundaries.UserBoundary;
import acs.database.Database;
import acs.helpers.UserHelper;

@RestController
public class AdminController {
	
	/*--------------------- GET all users APIS ------------------- */
	
	@RequestMapping(path = "/acs/admin/users/{adminDomain}/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] getAllUsers(@PathVariable("adminDomain") String adminDomain,@PathVariable("adminEmail") String adminEmail) {
		
		if(UserHelper.isLoggedIn(adminDomain,adminEmail)) {
			if(UserHelper.isAdmin(adminDomain,adminEmail))
			return Database.getAllUsers().toArray(new UserBoundary[0]);
		} 
		UserBoundary[] arr = {new UserBoundary()};
		return arr;				
	}
	
	/*--------------------- GET all actions APIS ------------------- */
	
	@RequestMapping(path = "/acs/admin/actions/{adminDomain}/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary[] getAllActions(
			@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
		
		List<ActionBoundary> listOfAction = new ArrayList<>();
		
		//Test values
		listOfAction.add(new ActionBoundary());
		listOfAction.add(new ActionBoundary());
		listOfAction.add(new ActionBoundary());
		
		return listOfAction.toArray(new ActionBoundary[0]);
	}		
	
	
	/*--------------------- DELETE APIS ------------------- */
	
	@RequestMapping(path = "/acs/admin/actions/{adminDomain}/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllActions(@PathVariable ("adminDomain") String adminDomain,
			@PathVariable ("adminEmail") String adminEmail) {
		
		Database.deleteAllActions();
	}

	@RequestMapping(path = "/acs/admin/elements/{adminDomain}/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllElements(@PathVariable ("adminDomain") String adminDomain,
			@PathVariable ("adminEmail") String adminEmail) {
		
		// TODO Complete the method below 'deleteAllElements()'
		Database.deleteAllElements();
	}
	
	@RequestMapping(path = "/acs/admin/users/{adminDomain}/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllUsers(@PathVariable ("adminDomain") String adminDomain,
			@PathVariable ("adminEmail") String adminEmail) {
		
		// TODO Complete the method below 'deleteAllUsers()'
		Database.deleteAllUsers();
	}

}