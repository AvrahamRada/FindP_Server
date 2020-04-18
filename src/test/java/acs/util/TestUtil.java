package acs.util;

import org.springframework.web.client.RestTemplate;

import acs.boundaries.UserBoundary;
import acs.data.UserRole;

public class TestUtil {

	public static void clearDB(int port) {

		// User URL
		String createUserUrl = "http://localhost:" + port + "/acs/users";
		String loginUrl = "http://localhost:" + port + "/acs/users/login/{userDomain}/{userEmail}";

		// Admin URL
		String deleteElementsUrl = "http://localhost:" + port + "/acs/admin/elements/{adminDomain}/{adminEmail}";
		String deleteUsersUrl = "http://localhost:" + port + "/acs/admin/users/{adminDomain}/{adminEmail}";
		String deleteActionsUrl = "http://localhost:" + port + "/acs/admin/actions/{adminDomain}/{adminEmail}";

		RestTemplate restTemplate = new RestTemplate();

		// Create admin for clear DB
		UserBoundary admin = restTemplate.postForObject(createUserUrl,
				new NewUserDetails("adminspecial@gmail.com", UserRole.ADMIN, "Admin", "Avatar"), UserBoundary.class);

		// Admin login
		restTemplate.getForObject(loginUrl, UserBoundary.class, admin.getUserId().getDomain(),
				admin.getUserId().getEmail());
		
		// Delete all elements from DB
		restTemplate.delete(deleteElementsUrl, admin.getUserId().getDomain(), admin.getUserId().getEmail());

		// Delete all users from DB
		restTemplate.delete(deleteUsersUrl, admin.getUserId().getDomain(), admin.getUserId().getEmail());

		// Delete all actions from DB
		restTemplate.delete(deleteActionsUrl, admin.getUserId().getDomain(), admin.getUserId().getEmail());

	}

}
