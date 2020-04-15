package acs.user;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import acs.boundaries.UserBoundary;
import acs.data.UserRole;
import acs.util.NewUserDetails;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserPUTTests {

	private int port;
	private String url;
	private String createUserUrl;
	private String deleteUrl;
	private RestTemplate restTemplate;
	private String loginUrl;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {

		this.url = "http://localhost:" + port + "/acs/users";
		this.deleteUrl = "http://localhost:" + port + "/acs/admin/users/{adminDomain}/{adminEmail}";
		this.createUserUrl = "http://localhost:" + port + "/acs/users";
		this.loginUrl = "http://localhost:" + port + "/acs/users/login/{userDomain}/{userEmail}";
		this.restTemplate = new RestTemplate();
	}

	@BeforeEach
	public void setup() {

		UserBoundary admin = this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("admin@gmail.com", UserRole.ADMIN, "Admin", "Avatar"), UserBoundary.class);

		this.restTemplate.getForObject(this.loginUrl, UserBoundary.class, admin.getUserId().getDomain(),
				admin.getUserId().getEmail());

		// Delete all users from DB
		this.restTemplate.delete(this.deleteUrl, admin.getUserId().getDomain(), admin.getUserId().getEmail());

	}

	@AfterEach
	public void teardown() {

		UserBoundary admin = this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("admin@gmail.com", UserRole.ADMIN, "Admin", "Avatar"), UserBoundary.class);

		this.restTemplate.getForObject(this.loginUrl, UserBoundary.class, admin.getUserId().getDomain(),
				admin.getUserId().getEmail());

		// Delete all users from DB
		this.restTemplate.delete(this.deleteUrl, admin.getUserId().getDomain(), admin.getUserId().getEmail());
	}

	@Test
	public void testContext() {

	}

}
