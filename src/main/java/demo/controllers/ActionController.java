package demo.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import demo.boundaries.ActionBoundary;

@RestController
public class ActionController {
	
	@RequestMapping(path = "/acs/actions",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary invokeAction (@RequestBody ActionBoundary input) {
		if(input.getActionId() == null) {
			return input;	
		}
		return new ActionBoundary();

	}
}
