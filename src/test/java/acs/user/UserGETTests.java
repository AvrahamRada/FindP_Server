package acs.user;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
import acs.util.UserId;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserGETTests {
	

	private int port;
	private String createUserUrl;
	private String loginUrl;
	private RestTemplate restTemplate;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.createUserUrl = "http://localhost:" + port + "/acs/users";
		this.loginUrl = "http://localhost:" + port + "/acs/users/login/{userDomain}/{userEmail}";
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
	public void testGetLoginUserToDatabaseAndReturnASpecificUserThatExistsInDatabase() throws Exception{
		UserBoundary user= this.restTemplate.postForObject(this.createUserUrl, 
				new NewUserDetails("user@gmail.com",UserRole.PLAYER,"user",":)"), UserBoundary.class);
		
		UserBoundary newUser= 
				this.restTemplate
				.getForObject(this.loginUrl, UserBoundary.class, user.getUserId().getDomain()
						,user.getUserId().getEmail());
		
		assertThat(newUser).usingRecursiveComparison().isEqualTo(user);
		
		
	}
	
	
	@Test
	public void testGetWithEmptyDatabaseAndTryLoginUserReturnsStatusDifferentThan2xx() throws Exception{
		UserBoundary user= new UserBoundary(new UserId(" ","user@gmail.com"), UserRole.PLAYER, "user", ":)");						
		assertThrows(Exception.class, () -> this.restTemplate
				.getForObject(this.loginUrl, UserBoundary.class, user.getUserId().getDomain()
						,user.getUserId().getEmail()));
		
	}
	
	//---
	@Test
	public void testGetDatabaseNotEmptyAndTryLoginUserThatDoesntExistsReturnStatusDifferentThan2xx() throws Exception{
		IntStream.range(0, 5)
		.forEach(i->this.restTemplate
				.postForObject(
			this.createUserUrl, 
			new NewUserDetails("user" + i + "@gmail.com",UserRole.PLAYER,"user",":)"), 
			UserBoundary.class));
		
		assertThrows(Exception.class, () -> this.restTemplate
				.getForObject(this.loginUrl, UserBoundary.class, "userNoExists"
						,"userNoExists@gmail.com"));
		
	}
	
	@Test
	public void testGetDatabaseNotEmptyAndTryLoginUserWithWrongUserDomainReturnStatusDifferentThan2xx() throws Exception{
		IntStream.range(0, 5)
		.forEach(i->this.restTemplate
				.postForObject(
			this.createUserUrl, 
			new NewUserDetails("user" + i + "@gmail.com",UserRole.PLAYER,"user",":)"), 
			UserBoundary.class));
		
		assertThrows(Exception.class, () -> this.restTemplate
				.getForObject(this.loginUrl, UserBoundary.class, "wrongDomain"
						,"user1@gmail.com"));
		
	}
	
	

}
