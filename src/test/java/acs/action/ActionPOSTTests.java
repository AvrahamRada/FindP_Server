package acs.action;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

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
public class ActionPOSTTests {

	private int port;
	private String url;
	private String createUserUrl;
	private String getUrl;
	private RestTemplate restTemplate;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		this.url = "http://localhost:" + port + "/acs/actions";
		this.getUrl = "http://localhost:" + port + "/acs/admin/actions/{adminDomain}/{adminEmail}";
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

	@Test
	public void testPostSingleActionWithNoActionIdServerSaveToDBNewActionEntityWithGeneratedID() throws Exception {

		// GIVEN the server is up
		// do nothing

		// WHEN I POST /acs/actions with Action Boundary with NO Action Id.

		ActionBoundary newAction = this.restTemplate.postForObject(this.url,
				new ActionBoundary(null, "type", new Element(new ElementId("2020b.lior.trachtman", "don't care")),
						new Date(System.currentTimeMillis()),
						new InvokedBy(new UserId("2020b.lior.trachtman", "mor@gmail.com")), new HashMap<String, Object>()),
				ActionBoundary.class);

		// THEN the server save the new action boundary with
		// elementDomain : 2020b.lior.trachtman AND
		// generated UUID and returns it.

		// Create admin for get all actions from DB.
		UserBoundary admin = this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("admin@gmail.com", UserRole.ADMIN, "Admin", "Avatar"), UserBoundary.class);

		ActionBoundary[] actualActionsArray = this.restTemplate.getForObject(this.getUrl, ActionBoundary[].class,
				admin.getUserId().getDomain(), admin.getUserId().getEmail());

		assertThat(actualActionsArray).usingRecursiveFieldByFieldElementComparator().contains(newAction);

	}

	@Test
	public void testPostSingleActionServerSaveToDBNewActionEntityWithGeneratedID() throws Exception {

		// GIVEN the server is up
		// do nothing

		// WHEN I POST /acs/actions with Action Boundary with Action Id = "X".

		final String id = "X";

		ActionBoundary newAction = this.restTemplate.postForObject(this.url,
				new ActionBoundary(new ActionId("2020b.lior.trachtman", id), "type",
						new Element(new ElementId("2020b.lior.trachtman", "don't care")),
						new Date(System.currentTimeMillis()),
						new InvokedBy(new UserId("2020b.lior.trachtman", "mor@gmail.com")), new HashMap<String, Object>()),
				ActionBoundary.class);

		// THEN the server save the new action boundary and set
		// elementDomain to 2020b.lior.trachtman AND
		// set the id to generated UUID and returns it.

		// Create admin for get all actions from DB.
		UserBoundary admin = this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("admin@gmail.com", UserRole.ADMIN, "Admin", "Avatar"), UserBoundary.class);

		ActionBoundary[] actualActionsArray = this.restTemplate.getForObject(this.getUrl, ActionBoundary[].class,
				admin.getUserId().getDomain(), admin.getUserId().getEmail());

		assertThat(actualActionsArray).usingRecursiveFieldByFieldElementComparator().contains(newAction);

	}

	@Test
	public void testPostSingleActionWithNoElementDatabaseReturnStatusDifferenceFrom2xx() throws Exception {

		// GIVEN the server is up
		// do nothing

		// WHEN I POST /acs/actions with Action Boundary with no element

		// THEN the server returns status != 2xx
		assertThrows(Exception.class, () -> this.restTemplate.postForObject(this.url,
				new ActionBoundary(null, "type", null, new Date(System.currentTimeMillis()),
						new InvokedBy(new UserId("2020b.lior.trachtman", "don't care")), new HashMap<String, Object>()),
				ActionBoundary.class));

	}

	@Test
	public void testPostSingleActionWithNoElementIdDatabaseReturnStatusDifferenceFrom2xx() throws Exception {

		// GIVEN the server is up
		// do nothing

		// WHEN I POST /acs/actions with Action Boundary with no element

		// THEN the server returns status != 2xx
		assertThrows(Exception.class, () -> this.restTemplate.postForObject(this.url,
				new ActionBoundary(null, "type", new Element(null), new Date(System.currentTimeMillis()),
						new InvokedBy(new UserId("2020b.lior.trachtman", "don't care")), new HashMap<String, Object>()),
				ActionBoundary.class));

	}

	@Test
	public void testPostSingleActionWithNoTypeDatabaseReturnStatusDifferenceFrom2xx() throws Exception {

		// GIVEN the server is up
		// do nothing

		// WHEN I POST /acs/actions with Action Boundary with no type

		// THEN the server returns status != 2xx
		assertThrows(Exception.class, () -> this.restTemplate.postForObject(this.url,
				new ActionBoundary(null, null, new Element(new ElementId("2020b.lior.trachtman", "don't care")),
						new Date(System.currentTimeMillis()),
						new InvokedBy(new UserId("2020b.lior.trachtman", "don't care")), new HashMap<String, Object>()),
				ActionBoundary.class));

	}

	@Test
	public void testPostSingleActionWithNoCreatedTimestampDatabaseStoreActionEntityWithGenereatedTimestamp()
			throws Exception {

		// GIVEN the server is up
		// do nothing

		// WHEN I POST /acs/actions with Action Boundary with no element
		ActionBoundary newAction = this.restTemplate.postForObject(this.url,
				new ActionBoundary(null, "type", new Element(new ElementId("2020b.lior.trachtman", "don't care")), null,
						new InvokedBy(new UserId("2020b.lior.trachtman", "mor@gmail.com")), new HashMap<String, Object>()),
				ActionBoundary.class);

		// Create admin for get all actions from DB.
		UserBoundary admin = this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("admin@gmail.com", UserRole.ADMIN, "Admin", "Avatar"), UserBoundary.class);

		// THEN the server save the new action boundary and set
		// the timestamp to current time

		ActionBoundary[] actualActionsArray = this.restTemplate.getForObject(this.getUrl, ActionBoundary[].class,
				admin.getUserId().getDomain(), admin.getUserId().getEmail());

		assertThat(actualActionsArray).usingRecursiveFieldByFieldElementComparator().contains(newAction);

	}

	@Test
	public void testPostSingleActionWithNoInvokedByDatabaseReturnStatusDifferenceFrom2xx() throws Exception {

		// GIVEN the server is up
		// do nothing

		// WHEN I POST /acs/actions with Action Boundary with no invoked by

		// THEN the server returns status != 2xx
		assertThrows(Exception.class, () -> this.restTemplate.postForObject(this.url,
				new ActionBoundary(null, "type", new Element(new ElementId("2020b.lior.trachtman", "don't care")),
						new Date(System.currentTimeMillis()), null, new HashMap<String, Object>()),
				ActionBoundary.class));

	}

	@Test
	public void testPostSingleActionWithNoActionAttributesByDatabaseReturnStatusDifferenceFrom2xx() throws Exception {

		// GIVEN the server is up
		// do nothing

		// WHEN I POST /acs/actions with Action Boundary with no invoked by

		// THEN the server returns status != 2xx
		assertThrows(Exception.class, () -> this.restTemplate.postForObject(this.url,
				new ActionBoundary(null, "type", new Element(new ElementId("2020b.lior.trachtman", "don't care")),
						new Date(System.currentTimeMillis()),
						new InvokedBy(new UserId("2020b.lior.trachtman", "don't care")), null),
				ActionBoundary.class));

	}

	@Test
	public void testPost10ValidActionServerSaveToDBAllEntitesWithGeneratedID() throws Exception {

		final int X = 10;

		// GIVEN the server is up
		// do nothing

		List<ActionBoundary> storedActions = new ArrayList<>();

		for (int i = 0; i < X; i++) {
			storedActions.add(this.restTemplate.postForObject(this.url,
					new ActionBoundary(null, "type", new Element(new ElementId("2020b.lior.trachtman", "don't care")),
							new Date(System.currentTimeMillis()),
							new InvokedBy(new UserId("2020b.lior.trachtman", "mor@gmail.com")),
							new HashMap<String, Object>()),
					ActionBoundary.class));
		}

		// Create admin for get all actions from DB.
		UserBoundary admin = this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("admin@gmail.com", UserRole.ADMIN, "Admin", "Avatar"), UserBoundary.class);

		// WHEN I POST X action boundaries to the server
		ActionBoundary[] actualActionsArray = this.restTemplate.getForObject(this.getUrl, ActionBoundary[].class,
				admin.getUserId().getDomain(), admin.getUserId().getEmail());

		// THEN the server returns the same X actions in the database (which mean DB
		// saved the action entites
		assertThat(actualActionsArray).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrderElementsOf(storedActions);

	}

	@Test
	public void testPost100ValidActionServerSaveToDBAllEntitesWithGeneratedID() throws Exception {

		final int X = 100;

		// GIVEN the server is up
		// do nothing

		List<ActionBoundary> storedActions = new ArrayList<>();

		for (int i = 0; i < X; i++) {
			storedActions.add(this.restTemplate.postForObject(this.url,
					new ActionBoundary(null, "type", new Element(new ElementId("2020b.lior.trachtman", "don't care")),
							new Date(System.currentTimeMillis()),
							new InvokedBy(new UserId("2020b.lior.trachtman", "mor@gmail.com")),
							new HashMap<String, Object>()),
					ActionBoundary.class));
		}

		// Create admin for get all actions from DB.
		UserBoundary admin = this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("admin@gmail.com", UserRole.ADMIN, "Admin", "Avatar"), UserBoundary.class);

		// WHEN I POST X action boundaries to the server
		ActionBoundary[] actualActionsArray = this.restTemplate.getForObject(this.getUrl, ActionBoundary[].class,
				admin.getUserId().getDomain(), admin.getUserId().getEmail());

		// THEN the server returns the same X actions in the database (which mean DB
		// saved the action entites
		assertThat(actualActionsArray).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrderElementsOf(storedActions);

	}

	@Test
	public void testPost1000ValidActionServerSaveToDBAllEntitesWithGeneratedID() throws Exception {

		final int X = 1000;

		// GIVEN the server is up
		// do nothing

		List<ActionBoundary> storedActions = new ArrayList<>();

		for (int i = 0; i < X; i++) {
			storedActions.add(this.restTemplate.postForObject(this.url,
					new ActionBoundary(null, "type", new Element(new ElementId("2020b.lior.trachtman", "don't care")),
							new Date(System.currentTimeMillis()),
							new InvokedBy(new UserId("2020b.lior.trachtman", "mor@gmail.com")),
							new HashMap<String, Object>()),
					ActionBoundary.class));
		}

		// Create admin for get all actions from DB.
		UserBoundary admin = this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("admin@gmail.com", UserRole.ADMIN, "Admin", "Avatar"), UserBoundary.class);

		// WHEN I POST X action boundaries to the server
		ActionBoundary[] actualActionsArray = this.restTemplate.getForObject(this.getUrl, ActionBoundary[].class,
				admin.getUserId().getDomain(), admin.getUserId().getEmail());

		// THEN the server returns the same X actions in the database (which mean DB
		// saved the action entites
		assertThat(actualActionsArray).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrderElementsOf(storedActions);

	}

	@Test
	public void testPost10000ValidActionServerSaveToDBAllEntitesWithGeneratedID() throws Exception {

		final int X = 10000;

		// GIVEN the server is up
		// do nothing

		List<ActionBoundary> storedActions = new ArrayList<>();

		for (int i = 0; i < X; i++) {
			storedActions.add(this.restTemplate.postForObject(this.url,
					new ActionBoundary(null, "type", new Element(new ElementId("2020b.lior.trachtman", "don't care")),
							new Date(System.currentTimeMillis()),
							new InvokedBy(new UserId("2020b.lior.trachtman", "mor@gmail.com")),
							new HashMap<String, Object>()),
					ActionBoundary.class));
		}

		// Create admin for get all actions from DB.
		UserBoundary admin = this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("admin@gmail.com", UserRole.ADMIN, "Admin", "Avatar"), UserBoundary.class);

		// WHEN I POST X action boundaries to the server
		ActionBoundary[] actualActionsArray = this.restTemplate.getForObject(this.getUrl, ActionBoundary[].class,
				admin.getUserId().getDomain(), admin.getUserId().getEmail());

		// THEN the server returns the same X actions in the database (which mean DB
		// saved the action entites
		assertThat(actualActionsArray).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrderElementsOf(storedActions);

	}
}
