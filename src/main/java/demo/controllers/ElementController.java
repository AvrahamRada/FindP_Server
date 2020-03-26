package demo.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import demo.boundaries.ElementBoundary;
import demo.database.Database;
import demo.element.CreatedBy;
import demo.element.ElementAttributes;
import demo.element.ElementId;
import demo.element.Location;
import demo.element.UserId;
import demo.helpers.UserHelper;

@RestController
public class ElementController {
	
		/*--------------------- GET APIS ------------------- */
	
		//Retreive Specific Element
		@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/{elementDomain}/{elementId}",
				method = RequestMethod.GET,
				produces = MediaType.APPLICATION_JSON_VALUE)
		public ElementBoundary getElement(@PathVariable("userDomain") String userDomain, @PathVariable("userEmail") String userEmail,
				@PathVariable("elementDomain") String elementDomain,@PathVariable("elementId") String elementId) {
			
			if(UserHelper.isLoggedIn(userDomain,userEmail)) {
				
				//Some tests
				System.out.println("userDoamin = " + userDomain);
				System.out.println("userEmail = " + userEmail);
				System.out.println("elementDomain = " + elementDomain);
				System.out.println("elementId = " + elementId);
		
				return new ElementBoundary(new ElementId(userDomain, elementId),"type","name",
						true,new Date(System.currentTimeMillis()),new CreatedBy(new UserId(userDomain,userEmail)),new Location(40.730610,-73.935242),new ElementAttributes(true));
				
			} 
			
			//User is not logged in.
			return null;
							
		}
		
		
		@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}",
				method = RequestMethod.GET,
				produces = MediaType.APPLICATION_JSON_VALUE)
		public ElementBoundary[] getAllElements(@PathVariable("userDomain") String userDomain,
				@PathVariable("userEmail") String userEmail) {
			
			if(UserHelper.isLoggedIn(userDomain,userEmail)) {
				
				//Some tests
				System.out.println("userDoamin = " + userDomain);
				System.out.println("userEmail = " + userEmail);
				
				return Database.getAllElements().toArray(new ElementBoundary[0]);
				
			} 
			
			//User is not logged in.
			return null;
							
		}
		
		/*--------------------- POST APIS ------------------- */
		
				@RequestMapping(path = "/acs/elements/{managerDomain}/{managerEmail}",
						method = RequestMethod.POST,
						consumes = MediaType.APPLICATION_JSON_VALUE,
						produces = MediaType.APPLICATION_JSON_VALUE)
				public ElementBoundary createNewElement (
						@RequestBody ElementBoundary input) {
					
						input.getElementId().setId(Database.generateUniqueId());
						Database.saveElement(input);
						
						return input;	
			
		}
}