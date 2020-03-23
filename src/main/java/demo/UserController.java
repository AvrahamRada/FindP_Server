package demo;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	@RequestMapping(path = "/acs/users/login/{userDomain}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary login(@PathVariable("userDomain") String userDomain, @PathVariable("userEmail") String userEmail) {
		System.out.println("userDoamin = " + userDomain);
		System.out.println("userEmail = " + userEmail);
		return new UserBoundary(userEmail, "userName", userDomain);
	}
}