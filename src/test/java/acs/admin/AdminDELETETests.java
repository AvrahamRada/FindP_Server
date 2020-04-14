package acs.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;
import java.util.HashMap;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import acs.action.ActionId;
import acs.action.InvokedBy;
import acs.boundaries.ActionBoundary;
import acs.boundaries.ElementBoundary;
import acs.boundaries.UserBoundary;
import acs.data.UserRole;
import acs.util.CreatedBy;
import acs.util.Element;
import acs.util.ElementId;
import acs.util.Location;
import acs.util.NewUserDetails;
import acs.util.UserId;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AdminDELETETests {
	
	private int port;
	//User
	private String createUserUrl;
	private String loginUrl;
	private String allUsersUrl;
	//Element
	private String createElementUrl;
	private String allElementsUrl;
	//Action
	private String invokeActionUrl;
	private String allActionsUrl;
	//Admin
	private String deleteActionsUrl;
	private String deleteElementsUrl;
	private String deleteUsersUrl;
	
	private RestTemplate restTemplate;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		//for user
		this.createUserUrl = "http://localhost:" + port + "/acs/users";
		this.loginUrl = "http://localhost:" + port + "/acs/users/login/{userDomain}/{userEmail}";
		this.allUsersUrl = "http://localhost:" + port + "/acs/admin/users/{adminDomain}/{adminEmail}";
		//for elements
		this.createElementUrl = "http://localhost:" + port + "/acs/elements/{managerDomain}/{managerEmail}";
		this.allElementsUrl = "http://localhost:" + port + "/acs/elements/{userDomain}/{userEmail}";
		//for invoke action
		this.invokeActionUrl = "http://localhost:" + port + "/acs/actions";
		this.allActionsUrl = "http://localhost:" + port + "/acs/admin/actions/{adminDomain}/{adminEmail}";
		//for DELETE of admin
		this.deleteActionsUrl = "http://localhost:" + port + "/acs/admin/actions/{adminDomain}/{adminEmail}";
		this.deleteElementsUrl = "http://localhost:" + port + "/acs/admin/elements/{adminDomain}/{adminEmail}";
		this.deleteUsersUrl = "http://localhost:" + port + "/acs/admin/users/{adminDomain}/{adminEmail}";
		this.restTemplate = new RestTemplate();
	}
	
	@BeforeEach
	public void setup() {
		// Create admin
		UserBoundary admin = this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("admin@gmail.com", UserRole.ADMIN, "Admin", "Avatar"), UserBoundary.class);
		// Admin login
		this.restTemplate.getForObject(this.loginUrl, UserBoundary.class, admin.getUserId().getDomain(),admin.getUserId().getEmail());
		//Delete all users from DB
		this.restTemplate.delete(this.deleteUsersUrl, admin.getUserId().getDomain(), admin.getUserId().getEmail());
		//Delete all elements from DB
		this.restTemplate.delete(this.deleteElementsUrl, admin.getUserId().getDomain(), admin.getUserId().getEmail());
		//Delete all actions from DB
		this.restTemplate.delete(this.deleteActionsUrl, admin.getUserId().getDomain(), admin.getUserId().getEmail());
	}
	
	@AfterEach
	public void teardown() {
		// Create admin
		UserBoundary admin = this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("admin@gmail.com", UserRole.ADMIN, "Admin", "Avatar"), UserBoundary.class);
		// Admin login
		this.restTemplate.getForObject(this.loginUrl, UserBoundary.class, admin.getUserId().getDomain(),admin.getUserId().getEmail());
		//Delete all users from DB
		this.restTemplate.delete(this.deleteUsersUrl, admin.getUserId().getDomain(), admin.getUserId().getEmail());
		//Delete all elements from DB
		this.restTemplate.delete(this.deleteElementsUrl, admin.getUserId().getDomain(), admin.getUserId().getEmail());
		//Delete all actions from DB
		this.restTemplate.delete(this.deleteActionsUrl, admin.getUserId().getDomain(), admin.getUserId().getEmail());
	}
	
	@Test
	public void testContext() {
		
	}
	
	@Test
	public void testDeleteAllActionsWithActionsAndAdmin() throws Exception {
		//create and login admin
		UserBoundary admin = this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("admin@gmail.com", UserRole.ADMIN, "Admin", "Avatar"),UserBoundary.class);
		
		this.restTemplate.getForObject(this.loginUrl, UserBoundary.class, admin.getUserId().getDomain(),admin.getUserId().getEmail());
		
		final int X = 10;
		// create X actions
		IntStream.range(0, X)
				.forEach(i -> this.restTemplate.postForObject(this.invokeActionUrl,
						new ActionBoundary(null, "type",
								new Element(new ElementId("2020b.lior.trachtman", "id " + i)),
								new Date(System.currentTimeMillis()),
								new InvokedBy(new UserId("2020b.lior.trachtman", "mor.soferian@s.afeka.ac.il")),
								new HashMap<>()),
						ActionBoundary.class, "2020b.lior.trachtman", "don't care"));

		// get all elements
		ActionBoundary[] rv = this.restTemplate.getForObject(this.allActionsUrl,
				ActionBoundary[].class, admin.getUserId().getDomain(),admin.getUserId().getEmail());

		// the server returns array of X element
		assertThat(rv).hasSize(X);
		
		//delete all elements from DB
		this.restTemplate.delete(this.deleteActionsUrl, admin.getUserId().getDomain(), admin.getUserId().getEmail());
		
		// get all elements after delete
		rv = this.restTemplate.getForObject(this.allActionsUrl,
				ActionBoundary[].class, admin.getUserId().getDomain(),admin.getUserId().getEmail());

		// the server returns an empty array
		assertThat(rv).isEmpty();
	}
	
	@Test
	public void testDeleteAllElementsWithElementsAndAdmin() throws Exception {
		//create and login admin
		UserBoundary admin = this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("admin@gmail.com", UserRole.ADMIN, "Admin", "Avatar"),UserBoundary.class);
		
		this.restTemplate.getForObject(this.loginUrl, UserBoundary.class, admin.getUserId().getDomain(),admin.getUserId().getEmail());
		
		final int X = 10;
		// create X elements
		IntStream.range(0, X)
				.forEach(i -> this.restTemplate.postForObject(this.createElementUrl,
						new ElementBoundary(new ElementId("2020b.lior.trachtman", "id " + i), "type", "name", true,
								new Date(System.currentTimeMillis()),
								new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
								new Location(40.730610, -73.935242), new HashMap<>()),
						ElementBoundary.class, "2020b.lior.trachtman", "don't care"));

		// get all elements
		ElementBoundary[] rv = this.restTemplate.getForObject(this.allElementsUrl,
				ElementBoundary[].class, "2020b.lior.trachtman", "don't care");

		// the server returns array of X element
		assertThat(rv).hasSize(X);
		
		//delete all elements from DB
		this.restTemplate.delete(this.deleteElementsUrl, admin.getUserId().getDomain(), admin.getUserId().getEmail());
		
		// get all elements after delete
		rv = this.restTemplate.getForObject(this.allElementsUrl,
				ElementBoundary[].class, "2020b.lior.trachtman", "don't care");

		// the server returns an empty array
		assertThat(rv).isEmpty();	
	}
	
	@Test
	public void testDeleteAllUsersWithUsersAndAdmin() throws Exception{
		final int X = 5;
		// create users
		IntStream.range(0, X).forEach(i->this.restTemplate.postForObject(this.createUserUrl, 
			new NewUserDetails("user" + i + "@gmail.com",UserRole.PLAYER,"user","(=)"), UserBoundary.class));
		
		//create and login admin
		UserBoundary admin = this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("admin@gmail.com", UserRole.ADMIN, "Admin", "Avatar"), UserBoundary.class);
				
		this.restTemplate.getForObject(this.loginUrl, UserBoundary.class, admin.getUserId().getDomain(),admin.getUserId().getEmail());
		
		// get all users
		UserBoundary[] rv = this.restTemplate
					.getForObject(this.allUsersUrl, UserBoundary[].class, admin.getUserId().getDomain(),admin.getUserId().getEmail());
		
		// the server returns array of 5 users and 1 admin  = 6 users
		assertThat(rv).hasSize(X +1);
		
		// delete all users from DB - include admin
		this.restTemplate.delete(this.deleteUsersUrl, admin.getUserId().getDomain(),admin.getUserId().getEmail());
		
		//create and login admin - again
		admin = this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("admin@gmail.com", UserRole.ADMIN, "Admin", "Avatar"), UserBoundary.class);
				
		this.restTemplate.getForObject(this.loginUrl, UserBoundary.class, admin.getUserId().getDomain(),admin.getUserId().getEmail());
		
		// get all users - only admin
		rv = this.restTemplate.getForObject(this.allUsersUrl, UserBoundary[].class, admin.getUserId().getDomain(),admin.getUserId().getEmail());
		
		// the server returns array of 1 admin  = 1 users
		assertThat(rv).hasSize(1);
	}
	
	@Test
	public void testDeleteAllActionsWithActionsAndNotAdmin() throws Exception {
		//create and login A NOT admin user
		UserBoundary notAdmin = this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("notAdmin@gmail.com", UserRole.PLAYER, "not Admin", "Avatar"),UserBoundary.class);
		
		this.restTemplate.getForObject(this.loginUrl, UserBoundary.class, notAdmin.getUserId().getDomain(),notAdmin.getUserId().getEmail());
		
		final int X = 10;
		// create X actions
		IntStream.range(0, X)
				.forEach(i -> this.restTemplate.postForObject(this.invokeActionUrl,
						new ActionBoundary(null, "type",
								new Element(new ElementId("2020b.lior.trachtman", "id " + i)),
								new Date(System.currentTimeMillis()),
								new InvokedBy(new UserId("2020b.lior.trachtman", "mor.soferian@s.afeka.ac.il")),
								new HashMap<>()),
						ActionBoundary.class, "2020b.lior.trachtman", "don't care"));
				
		// get all elements
		ActionBoundary[] rv = this.restTemplate.getForObject(this.allActionsUrl,
				ActionBoundary[].class, notAdmin.getUserId().getDomain(),notAdmin.getUserId().getEmail());

		// the server returns array of X element
		assertThat(rv).hasSize(X);
		
		//delete all elements from DB
		this.restTemplate.delete(this.deleteActionsUrl, notAdmin.getUserId().getDomain(), notAdmin.getUserId().getEmail());
		
		// get all elements after delete
		rv = this.restTemplate.getForObject(this.allActionsUrl,
				ActionBoundary[].class, notAdmin.getUserId().getDomain(),notAdmin.getUserId().getEmail());

		// the server returns an empty array
		assertThat(rv).isEmpty();
	}
	
	@Test
	public void testDeleteAllElementsWithElementsAndNotAdmin() {
		//create and login A NOT admin user
		UserBoundary notAdmin = this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("notAdmin@gmail.com", UserRole.PLAYER, "not Admin", "Avatar"),UserBoundary.class);
		
		this.restTemplate.getForObject(this.loginUrl, UserBoundary.class, notAdmin.getUserId().getDomain(),notAdmin.getUserId().getEmail());
		
		final int X = 10;
		// create X elements
		IntStream.range(0, X)
				.forEach(i -> this.restTemplate.postForObject(this.createElementUrl,
						new ElementBoundary(new ElementId("2020b.lior.trachtman", "id " + i), "type", "name", true,
								new Date(System.currentTimeMillis()),
								new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
								new Location(40.730610, -73.935242), new HashMap<>()),
						ElementBoundary.class, "2020b.lior.trachtman", "don't care"));
				
		// get all elements
		ElementBoundary[] rv = this.restTemplate.getForObject(this.allElementsUrl,
				ElementBoundary[].class, "2020b.lior.trachtman", "don't care");

		// the server returns array of X element
		assertThat(rv).hasSize(X);
		
		//delete all elements from DB
		this.restTemplate.delete(this.deleteElementsUrl, notAdmin.getUserId().getDomain(), notAdmin.getUserId().getEmail());
		
		// get all elements after delete
		rv = this.restTemplate.getForObject(this.allElementsUrl,
				ElementBoundary[].class, "2020b.lior.trachtman", "don't care");

		// the server returns an empty array
		assertThat(rv).isEmpty();	
	}
	
	@Test
	public void testDeleteAllUsersWithUsersAndNotAdmin() {
		//create and login A NOT admin user
		UserBoundary notAdmin = this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("notAdmin@gmail.com", UserRole.PLAYER, "not Admin", "Avatar"),UserBoundary.class);
		
		this.restTemplate.getForObject(this.loginUrl, UserBoundary.class, notAdmin.getUserId().getDomain(),notAdmin.getUserId().getEmail());
		
		final int X = 5;
		// create users
		IntStream.range(0, X).forEach(i->this.restTemplate.postForObject(this.createUserUrl, 
			new NewUserDetails("user" + i + "@gmail.com",UserRole.PLAYER,"user","(=)"), UserBoundary.class));
				
		//delete all users from DB - include ADMIN
		assertThrows(Exception.class, 
				() -> this.restTemplate.delete(this.deleteUsersUrl, notAdmin.getUserId().getDomain(), notAdmin.getUserId().getEmail()));
	}

}
