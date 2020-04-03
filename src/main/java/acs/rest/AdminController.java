package acs.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import acs.boundaries.ActionBoundary;
import acs.boundaries.UserBoundary;
import acs.database.Database;
import acs.helpers.UserHelper;
import acs.logic.ActionService;
import acs.logic.UserService;

@RestController
public class AdminController {
	
	private UserService userService;
	private ActionService actionService;
	
	@Autowired
	public AdminController(UserService userService, ActionService actionService) {
		this.userService = userService;
		this.actionService = actionService;

	}
	

	
	/*--------------------- GET all users APIS ------------------- */
	
	@RequestMapping(path = "/acs/admin/users/{adminDomain}/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] getAllUsers(@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
		return this.userService.getAllUsers(adminDomain, adminEmail).toArray(new UserBoundary[0]);				
	}
	
	/*--------------------- GET all actions APIS ------------------- */
	
	@RequestMapping(path = "/acs/admin/actions/{adminDomain}/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary[] getAllActions(
			@PathVariable("adminDomain") String adminDomain,
			@PathVariable("adminEmail") String adminEmail) {
		System.out.println("here");
		return this.actionService.getAllActions(adminDomain, adminEmail).toArray(new ActionBoundary[0]);				

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
		
		this.userService.deleteAllUsers(adminDomain, adminEmail);
	}

}