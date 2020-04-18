package acs.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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
import acs.util.TestUtil;





@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserPOSTTests {
	

	private int port;
	private String createUserUrl;
	private String loginUrl;
	private RestTemplate restTemplate;
	private String allUserUrl;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.allUserUrl = "http://localhost:" + port + "/acs/admin/users/{adminDomain}/{adminEmail}";
		this.loginUrl = "http://localhost:" + port + "/acs/users/login/{userDomain}/{userEmail}";
		this.createUserUrl = "http://localhost:" + port + "/acs/users";
		this.restTemplate = new RestTemplate();
	}
	
	@BeforeEach
	public void setup() {
		TestUtil.clearDB(port);
	}
	
	@AfterEach
	public void teardown() {
		
		TestUtil.clearDB(port);
	}
	
	@Test
	public void testContext() {
		
	}
	
	public UserBoundary createAdminAndLogin() {
		UserBoundary admin = this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("admin@gmail.com", UserRole.ADMIN, "Admin", "Avatar"),
				UserBoundary.class);
		
		this.restTemplate
		.getForObject(this.loginUrl, UserBoundary.class, admin.getUserId().getDomain()
				,admin.getUserId().getEmail());
		return admin;
	}
	
	
	@Test
	public void testPostAdd5UserAndAdminToEmptyDatabaseAndReturnDatabaseWith6Users() throws Exception {
		
		IntStream.range(0, 5)
		.forEach(i->this.restTemplate
				.postForObject(
			this.createUserUrl, 
			new NewUserDetails("user" + i + "@gmail.com",UserRole.PLAYER,"user",":)"), 
			UserBoundary.class));
		
		//create and login admin
		UserBoundary admin = createAdminAndLogin();
	// WHEN
	UserBoundary[] rv = 
		this.restTemplate
			.getForObject(this.allUserUrl, 
					UserBoundary[].class,admin.getUserId().getDomain(),admin.getUserId().getEmail());
	
	// THEN the server returns array of 5 users and 1 admin  = 6 users
	assertThat(rv)
		.hasSize(6);
	}
	
	
	@Test
	public void testPost10UsersAndAdminInDatabaseReturnsAllUsersStoredInDatabase() throws Exception {
		// GIVEN database contains specific 42 messages
		List<UserBoundary> storedUsers = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			storedUsers.add(
				this.restTemplate
				  .postForObject(
						this.createUserUrl, 
						new NewUserDetails("user" + i + "@gmail.com",UserRole.PLAYER,"user",":)"), 
						UserBoundary.class)
				  );
		}
		
		
		//create and login admin
		UserBoundary admin = createAdminAndLogin();
		
		storedUsers.add(admin);
		
		// WHEN
		UserBoundary[] usersArray = 
			this.restTemplate
				.getForObject(this.allUserUrl, 
						UserBoundary[].class,admin.getUserId().getDomain(),admin.getUserId().getEmail());
		
		// THEN the server returns the same 10 users and 1 admin = 11 users in the database
		assertThat(usersArray)
			.usingRecursiveFieldByFieldElementComparator()
			.containsExactlyInAnyOrderElementsOf(storedUsers);
	}
	


}
