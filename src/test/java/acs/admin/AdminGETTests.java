package acs.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
public class AdminGETTests {
	

	private int port;
	private String url;
	private String allUsersUrl;
	private String createUserUrl;
	private String allActionsUrl;
	private String invokeActionUrl;
	private RestTemplate restTemplate;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.createUserUrl = "http://localhost:" + port + "/acs/users";
		this.invokeActionUrl = "http://localhost:" + port + "/acs/actions";
		this.url = "http://localhost:" + port + "/acs/admin";
		this.allUsersUrl = "http://localhost:" + port + "/acs/admin/users/{adminDomain}/{adminEmail}";
		this.allActionsUrl = "http://localhost:" + port + "/acs/admin/actions/{adminDomain}/{adminEmail}";
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
				case "user":
					IntStream.range(0, X).forEach(i->this.restTemplate.postForObject(this.createUserUrl, 
							new NewUserDetails("user" + i + "@gmail.com",UserRole.PLAYER,"user","(=)"), UserBoundary.class));
					break;
			}
		}
	
	@Test
	public void testGetAllActionsWith10ActionsReturnsDatabaseWith10Actions() throws Exception {
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
	}
	
	@Test
	public void testGetAllActionsWith100ActionsReturnDatabaseWith100Actions() throws Exception {
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
		
		
	}
	
	@Test
	public void testGetAllUsersWith10UsersAndAdmin() throws Exception{
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
		
	}
	
	@Test
	public void testGetAllUsersWith100UsersAndAdmin() throws Exception{
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
		
	}
	
	@Test
	public void testGetAllActionsWithActionsAndNotAdmin() throws Exception {
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
	}
	
	@Test
	public void testGetAllUsersWithUsersAndNotAdmin() {
		//create A NOT admin user
		UserBoundary notAdmin = createNewUserByChoice(false);
		
		// create X users
		final int X = 10;
		createActionOrElementsOrUsersByX("user",X);
				
		//delete all users from DB
		assertThrows(Exception.class, 
				() -> this.restTemplate.getForObject(this.allUsersUrl, UserBoundary[].class, notAdmin.getUserId().getDomain(), notAdmin.getUserId().getEmail()));
	}
	
	
	@Test
	public void testGet10UsersAndAdminInDatabaseReturnAllUsersStoredInDatabase() throws Exception {
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
		UserBoundary admin = createNewUserByChoice(true);
		
		storedUsers.add(admin);
		
		// WHEN
		UserBoundary[] usersArray = 
			this.restTemplate
				.getForObject(this.allUsersUrl, 
						UserBoundary[].class,admin.getUserId().getDomain(),admin.getUserId().getEmail());
		
		// THEN the server returns the same 10 users and 1 admin = 11 users in the database
		assertThat(usersArray)
			.usingRecursiveFieldByFieldElementComparator()
			.containsExactlyInAnyOrderElementsOf(storedUsers);
	}
	
	
	
	

}
