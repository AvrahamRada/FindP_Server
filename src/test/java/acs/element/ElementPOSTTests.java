package acs.element;

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
public class ElementPOSTTests {
	

	private int port;
	private String url;
	private String createUserUrl;
	private String deleteUrl;
	private RestTemplate restTemplate;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.url = "http://localhost:" + port + "/acs/elements";
		this.deleteUrl = "http://localhost:" + port + "/acs/admin/elements/{adminDomain}/{adminEmail}";
		this.createUserUrl = "http://localhost:" + port + "/acs/users";
		this.restTemplate = new RestTemplate();
	}
	
	@BeforeEach
	public void setup() {
		
		// Create admin for clear DB
				UserBoundary admin = this.restTemplate.postForObject(this.createUserUrl,
						new NewUserDetails("admin@gmail.com", UserRole.ADMIN, "Admin", "Avatar"),
						UserBoundary.class);

				//Delete all elements from DB
				this.restTemplate.delete(this.deleteUrl, admin.getUserId().getDomain(), admin.getUserId().getEmail());
	}
	
	@AfterEach
	public void teardown() {
		
		// Create admin for clear DB
				UserBoundary admin = this.restTemplate.postForObject(this.createUserUrl,
						new NewUserDetails("admin@gmail.com", UserRole.ADMIN, "Admin", "Avatar"),
						UserBoundary.class);

				//Delete all elements from DB
				this.restTemplate.delete(this.deleteUrl, admin.getUserId().getDomain(), admin.getUserId().getEmail());
	}
	
	@Test
	public void testContext() {
		
	}

}
