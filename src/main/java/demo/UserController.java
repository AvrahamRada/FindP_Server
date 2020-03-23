package demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	
	//login user get request
	@RequestMapping(path = "/acs/users/login/{userDomain}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary login(@PathVariable("userDomain") String userDomain, @PathVariable("userEmail") String userEmail) {
		System.out.println("userDoamin = " + userDomain);
		System.out.println("userEmail = " + userEmail);
		return new UserBoundary(userEmail, "userName", userDomain);
	}
	
	@RequestMapping(path = "/acs/elements/{adminDomain}/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserBoundary> getAllUsers(@PathVariable("adminDomain") String adminDomain,@PathVariable("adminEmail") String adminEmail) {
		
		if(UserLogin.isLoggedIn(adminDomain,adminEmail)) {
			if(UserLogin.isAdmin(adminDomain,adminEmail))
			
			//Some tests
			System.out.println("userDoamin = " + adminDomain);
			System.out.println("userEmail = " + adminEmail);
			return getAllUsersFromDB();
		} 
		//User is not logged in.
		return null;				
	}

	private List<UserBoundary> getAllUsersFromDB() {
		List<UserBoundary> list=new ArrayList<>();
		list.add(new UserBoundary("1@1.com", "Mor", "1"));
		list.add(new UserBoundary("2@2.com", "Hod", "2"));
		list.add(new UserBoundary("3@3.com", "Lior", "3"));
		
		return list;
	}

}