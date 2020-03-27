package demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<ActionBoundary> InvokeAction (
			@RequestBody ActionBoundary input) {
		if(input.getActionId() == null) {
			return ResponseEntity.ok(input);	
		} 	
		return new ResponseEntity<ActionBoundary>(HttpStatus.BAD_REQUEST);

	}
}
