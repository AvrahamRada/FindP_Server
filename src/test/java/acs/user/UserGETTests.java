package acs.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.annotation.PostConstruct;

import org.assertj.core.internal.FieldByFieldComparator;
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
public class UserGETTests {
	

	private int port;
	private String createUserUrl;
	private String deleteUrl;
	private String loginUrl;
	private RestTemplate restTemplate;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.deleteUrl = "http://localhost:" + port + "/acs/admin/users/{adminDomain}/{adminEmail}";
		this.createUserUrl = "http://localhost:" + port + "/acs/users";
		this.loginUrl = "http://localhost:" + port + "/acs/users/login/{userDomain}/{userEmail}";
		this.restTemplate = new RestTemplate();
	}
	
	@BeforeEach
	public void setup() {
		// Create admin for clear DB
				UserBoundary admin = this.restTemplate.postForObject(this.createUserUrl,
						new NewUserDetails("admin@gmail.com", UserRole.ADMIN, "Admin", "Avatar"),
						UserBoundary.class);
				
				this.restTemplate
						.getForObject(this.loginUrl, UserBoundary.class, admin.getUserId().getDomain()
								,admin.getUserId().getEmail());
				
				//Delete all users from DB
				this.restTemplate.delete(this.deleteUrl, admin.getUserId().getDomain(), admin.getUserId().getEmail());
	}
	
	@AfterEach
	public void teardown() {
		// Create admin for clear DB
		UserBoundary admin = this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("admin@gmail.com", UserRole.ADMIN, "Admin", "Avatar"),
				UserBoundary.class);
		
		this.restTemplate
				.getForObject(this.loginUrl, UserBoundary.class, admin.getUserId().getDomain()
						,admin.getUserId().getEmail());
		
		//Delete all users from DB
		this.restTemplate.delete(this.deleteUrl, admin.getUserId().getDomain(), admin.getUserId().getEmail());
	}
	
	@Test
	public void testContext() {
		
	}
	
	@Test
	public void testGetAddUserToDatabaseAndRetrieveUserBoundary() throws Exception{
		UserBoundary user= this.restTemplate.postForObject(this.createUserUrl, 
				new NewUserDetails("user@gmail.com",UserRole.PLAYER,"user",":)"), UserBoundary.class);
		
		UserBoundary newUser= 
				this.restTemplate
				.getForObject(this.loginUrl, UserBoundary.class, user.getUserId().getDomain()
						,user.getUserId().getEmail());
		
		assertThat(newUser).usingRecursiveComparison().isEqualTo(user);
		
	}
	
	
	

}
