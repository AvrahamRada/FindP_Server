package acs.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import acs.boundaries.ElementBoundary;
import acs.boundaries.ElementIdBoundary;
import acs.logic.EnhancedElementService;


@RestController
public class ElementController {

	private EnhancedElementService enhancedElementService;

	public ElementController(EnhancedElementService elementService) {
		this.enhancedElementService = elementService;
	}

	/*--------------------- GET APIS ------------------- */

	// Retrieve Specific Element
	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/{elementDomain}/{elementId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary getElement(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @PathVariable("elementDomain") String elementDomain,
			@PathVariable("elementId") String elementId) {

		return enhancedElementService.getSpecificElement(userDomain, userEmail, elementDomain, elementId);

	}
	
	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getAllElements(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {

		return enhancedElementService.getAll(userDomain,userEmail,size,page).toArray(new ElementBoundary[0]);

	}
	
	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/{elementDomain}/{elementId}/children", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getAllChildrenElements(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @PathVariable("elementDomain") String elementDomain,
			@PathVariable("elementId") String elementId,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {

		return enhancedElementService.getAllChildrenElements(userDomain,userEmail,elementDomain,elementId,size,page)
				.toArray(new ElementBoundary[0]);

	}
	
	
	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/{elementDomain}/{elementId}/parents", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getAllParentsElements(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @PathVariable("elementDomain") String elementDomain,
			@PathVariable("elementId") String elementId,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {

		return enhancedElementService.getAllOriginsElements(userDomain,userEmail,elementDomain,elementId,size,page).toArray(new ElementBoundary[0]);

	}
	
	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/search/byName/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getAllElementsByName(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @PathVariable("name") String name,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		
		System.out.println("im here1");

		return enhancedElementService.getAllElementsByName(userDomain,userEmail,name,size,page).toArray(new ElementBoundary[0]);

	}
	
	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/search/byType/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getAllElementsByType(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @PathVariable("type") String type,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		
		return enhancedElementService.getAllElementsByType(userDomain,userEmail,type,size,page).toArray(new ElementBoundary[0]);

	}
	
	@RequestMapping(path = "/acs/elements/{userDomain}/{userEmail}/search/near/{lat}/{lng}/{distance}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getAllElementsByLocation(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @PathVariable("lat") String lat,
			@PathVariable("lng") String lng, @PathVariable("distance") String distance,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		
		return enhancedElementService.getAllElementsByLocation(userDomain,userEmail,lat,lng,distance,size,page).toArray(new ElementBoundary[0]);

	}
	

	/*--------------------- POST APIS ------------------- */

	@RequestMapping(path = "/acs/elements/{managerDomain}/{managerEmail}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary createNewElement(@PathVariable("managerDomain") String managerDomain,
			@PathVariable("managerEmail") String managerEmail, @RequestBody ElementBoundary input) {

		return enhancedElementService.create(managerDomain, managerEmail, input);

	}

	/*--------------------- PUT APIS ------------------- */
	// Update an element
	@RequestMapping(path = "/acs/elements/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateElement(@PathVariable("managerDomain") String managerDomain,
			@PathVariable("managerEmail") String managerEmail, @PathVariable("elementDomain") String elementDomain,
			@PathVariable("elementId") String elementId, @RequestBody ElementBoundary input) {

		enhancedElementService.update(managerDomain, managerEmail, elementDomain, elementId, input);

	}
	
	@RequestMapping(path = "/acs/elements/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children", 
			method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void bindParentElementToChildElement(@PathVariable("managerDomain") String managerDomain,
			@PathVariable("managerEmail") String managerEmail, @PathVariable("elementDomain") String elementDomain,
			@PathVariable("elementId") String elementId, @RequestBody ElementIdBoundary input) {

		enhancedElementService.bindParentElementToChildElement(managerDomain, managerEmail, elementDomain, elementId, input);
	}
	

}