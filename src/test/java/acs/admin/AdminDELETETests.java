package acs.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;
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
import acs.util.TestUtil;
import acs.util.UserId;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AdminDELETETests {
	
	private int port;
	//User
	private String createUserUrl;
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
		//TestUtil.clearDB(port);
	}
	
	@AfterEach
	public void teardown() {
		TestUtil.clearDB(port);
	}
		
	
	@Test
	public void testContext() {
		
	}
	
	//helpful methods
	public UserBoundary createNewUserByChoice(boolean create) {
		UserBoundary user = null;
		if(create == true) {
			user = this.restTemplate.postForObject(this.createUserUrl,
					new NewUserDetails("admin@gmail.com", UserRole.ADMIN, "Admin", "Avatar"),UserBoundary.class);
		}
		else {
			user = this.restTemplate.postForObject(this.createUserUrl,
					new NewUserDetails("notAdmin@gmail.com", UserRole.PLAYER, "not Admin", "Avatar"),UserBoundary.class);
		}
		return user;
	}
	
	public void createActionOrElementsOrUsersByX(String option,int X)
	{
		switch(option) {
			case "action":
				IntStream.range(0, X)
				.forEach(i -> this.restTemplate.postForObject(this.invokeActionUrl,
						new ActionBoundary(null, "type",
								new Element(new ElementId("2020b.lior.trachtman", "id " + i)),
								new Date(System.currentTimeMillis()),
								new InvokedBy(new UserId("2020b.lior.trachtman", "mor.soferian@s.afeka.ac.il")),
								new HashMap<>()),
						ActionBoundary.class, "2020b.lior.trachtman", "don't care"));
				break;
			case "element":
				IntStream.range(0, X)
				.forEach(i -> this.restTemplate.postForObject(this.createElementUrl,
						new ElementBoundary(new ElementId("2020b.lior.trachtman", "id " + i), "type", "name", true,
								new Date(System.currentTimeMillis()),
								new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
								new Location(40.730610, -73.935242), new HashMap<>()),
						ElementBoundary.class, "2020b.lior.trachtman", "don't care"));
				break;
			case "user":
				IntStream.range(0, X).forEach(i->this.restTemplate.postForObject(this.createUserUrl, 
						new NewUserDetails("user" + i + "@gmail.com",UserRole.PLAYER,"user","(=)"), UserBoundary.class));
				break;
		}
	}
	
	// Test with Admin
	@Test
	public void testDeleteAllActionsWith10ActionsAndAdmin() throws Exception {
		//create admin
		UserBoundary admin = createNewUserByChoice(true);
		
		//create X actions
		final int X = 10;
		createActionOrElementsOrUsersByX("action",X);
		
		// get all actions
		ActionBoundary[] rv = this.restTemplate.getForObject(this.allActionsUrl,
				ActionBoundary[].class, admin.getUserId().getDomain(),admin.getUserId().getEmail());

		// the server returns array of X actions
		assertThat(rv).hasSize(X);
		
		//delete all actions from DB
		this.restTemplate.delete(this.deleteActionsUrl, admin.getUserId().getDomain(), admin.getUserId().getEmail());
		
		// get all actions after delete
		rv = this.restTemplate.getForObject(this.allActionsUrl,
				ActionBoundary[].class, admin.getUserId().getDomain(),admin.getUserId().getEmail());

		// the server returns an empty array
		assertThat(rv).isEmpty();
	}
	
	@Test
	public void testDeleteAllActionsWith100ActionsAndAdmin() throws Exception {
		//create admin
		UserBoundary admin = createNewUserByChoice(true);
		
		//create X actions
		final int X = 100;
		createActionOrElementsOrUsersByX("action",X);
		
		// get all actions
		ActionBoundary[] rv = this.restTemplate.getForObject(this.allActionsUrl,
				ActionBoundary[].class, admin.getUserId().getDomain(),admin.getUserId().getEmail());

		// the server returns array of X actions
		assertThat(rv).hasSize(X);
		
		//delete all actions from DB
		this.restTemplate.delete(this.deleteActionsUrl, admin.getUserId().getDomain(), admin.getUserId().getEmail());
		
		// get all actions after delete
		rv = this.restTemplate.getForObject(this.allActionsUrl,
				ActionBoundary[].class, admin.getUserId().getDomain(),admin.getUserId().getEmail());

		// the server returns an empty array
		assertThat(rv).isEmpty();
	}
	
	@Test
	public void testDeleteAllElementsWith10ElementsAndAdmin() throws Exception {
		//create admin
		UserBoundary admin = createNewUserByChoice(true);
		
		// create X elements
		final int X = 10;
		createActionOrElementsOrUsersByX("element",X);

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
	public void testDeleteAllElementsWith100ElementsAndAdmin() throws Exception {
		//create admin
		UserBoundary admin = createNewUserByChoice(true);
		
		// create X elements
		final int X = 100;
		createActionOrElementsOrUsersByX("element",X);

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
	public void testDeleteAllUsersWith10UsersAndAdmin() throws Exception{
		//create admin
		UserBoundary admin = createNewUserByChoice(true);
		
		// create X users
		final int X = 10;
		createActionOrElementsOrUsersByX("user",X);
		
		// get all users
		UserBoundary[] rv = this.restTemplate
					.getForObject(this.allUsersUrl, UserBoundary[].class, admin.getUserId().getDomain(),admin.getUserId().getEmail());
		
		// the server returns array of 10 users and 1 admin  = 11 users
		assertThat(rv).hasSize(X +1);
		
		// delete all users from DB - include admin
		this.restTemplate.delete(this.deleteUsersUrl, admin.getUserId().getDomain(),admin.getUserId().getEmail());
		
		//recreate admin
		admin = createNewUserByChoice(true);
		
		// get all users - only admin
		rv = this.restTemplate.getForObject(this.allUsersUrl, UserBoundary[].class, admin.getUserId().getDomain(),admin.getUserId().getEmail());
		
		// the server returns array of 1 admin  = 1 users
		assertThat(rv).hasSize(1);
	}
	
	@Test
	public void testDeleteAllUsersWith100UsersAndAdmin() throws Exception{
		//create admin
		UserBoundary admin = createNewUserByChoice(true);
		
		// create X users
		final int X = 100;
		createActionOrElementsOrUsersByX("user",X);
		
		// get all users
		UserBoundary[] rv = this.restTemplate
					.getForObject(this.allUsersUrl, UserBoundary[].class, admin.getUserId().getDomain(),admin.getUserId().getEmail());
		
		// the server returns array of 100 users and 1 admin  = 101 users
		assertThat(rv).hasSize(X +1);
		
		// delete all users from DB - include admin
		this.restTemplate.delete(this.deleteUsersUrl, admin.getUserId().getDomain(),admin.getUserId().getEmail());
		
		//recreate admin
		admin = createNewUserByChoice(true);
		
		// get all users - only admin
		rv = this.restTemplate.getForObject(this.allUsersUrl, UserBoundary[].class, admin.getUserId().getDomain(),admin.getUserId().getEmail());
		
		// the server returns array of 1 admin  = 1 users
		assertThat(rv).hasSize(1);
	}
	
	// Test with Not Admin
	@Test
	public void testDeleteAllActionsWithActionsAndNotAdmin() throws Exception {
		//create A NOT admin user
		UserBoundary notAdmin = createNewUserByChoice(false);
		
		// create X actions
		final int X = 10;
		createActionOrElementsOrUsersByX("action",X);
				
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
	public void testDeleteAllElementsWithElementsAndNotAdmin() throws Exception {
		//create A NOT admin user
		UserBoundary notAdmin = createNewUserByChoice(false);
		
		// create X elements
		final int X = 10;
		createActionOrElementsOrUsersByX("element",X);
				
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
		//create A NOT admin user
		UserBoundary notAdmin = createNewUserByChoice(false);
		
		// create X users
		final int X = 10;
		createActionOrElementsOrUsersByX("user",X);
				
		//delete all users from DB
		assertThrows(Exception.class, 
				() -> this.restTemplate.delete(this.deleteUsersUrl, notAdmin.getUserId().getDomain(), notAdmin.getUserId().getEmail()));
	}
	
	// Test with wrong path
	@Test
	public void testDeleteAllActionsWithWrongPath() throws Exception{
		//create and login admin
		UserBoundary admin = createNewUserByChoice(true);
		
		//create X actions
		final int X = 10;
		createActionOrElementsOrUsersByX("action",X);
		
		// get all actions
		ActionBoundary[] rv = this.restTemplate.getForObject(this.allActionsUrl,
				ActionBoundary[].class, admin.getUserId().getDomain(),admin.getUserId().getEmail());

		// the server returns array of X actions
		assertThat(rv).hasSize(X);
		
		// not delete all elements from DB
		this.restTemplate.delete(this.deleteElementsUrl, admin.getUserId().getDomain(), admin.getUserId().getEmail());
		
		// get all actions after delete elements
		rv = this.restTemplate.getForObject(this.allActionsUrl,
				ActionBoundary[].class, admin.getUserId().getDomain(),admin.getUserId().getEmail());

		// the server returns an isn't empty array
		assertThat(rv).isNotEmpty();
	}
	
	@Test
	public void testDeleteAllElementsWithWrongPath() throws Exception{
		//create and login admin
		UserBoundary admin = createNewUserByChoice(true);
		
		// create X elements
		final int X = 10;
		createActionOrElementsOrUsersByX("element",X);

		// get all elements
		ElementBoundary[] rv = this.restTemplate.getForObject(this.allElementsUrl,
				ElementBoundary[].class, "2020b.lior.trachtman", "don't care");

		// the server returns array of X element
		assertThat(rv).hasSize(X);
		
		// not delete all elements from DB
		this.restTemplate.delete(this.deleteActionsUrl, admin.getUserId().getDomain(), admin.getUserId().getEmail());
		
		// get all elements after delete
		rv = this.restTemplate.getForObject(this.allElementsUrl,
				ElementBoundary[].class, "2020b.lior.trachtman", "don't care");

		// the server returns an empty array
		assertThat(rv).isNotEmpty();
	}
	
	@Test
	public void testDeleteAllUsersWithWrongPath() throws Exception{
		//create and login admin
		UserBoundary admin = createNewUserByChoice(true);
		
		// create X users
		final int X = 10;
		createActionOrElementsOrUsersByX("user",X);
		
		// get all users
		UserBoundary[] rv = this.restTemplate
					.getForObject(this.allUsersUrl, UserBoundary[].class, admin.getUserId().getDomain(),admin.getUserId().getEmail());
		
		// not delete all users from DB - include admin
		this.restTemplate.delete(this.deleteActionsUrl, admin.getUserId().getDomain(),admin.getUserId().getEmail());
		
		// get all users - only admin
		rv = this.restTemplate.getForObject(this.allUsersUrl, UserBoundary[].class, admin.getUserId().getDomain(),admin.getUserId().getEmail());
		
		// the server returns array of 10 users and 1 admin  = 11 users
		assertThat(rv).isNotEmpty();
	}
	 
}
