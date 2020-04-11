package acs.action;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
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
import acs.util.Element;
import acs.util.ElementId;
import acs.util.NewUserDetails;
import acs.util.UserId;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ActionPOSTTests {

	private int port;
	private String url;
	private String createUserUrl;
	private String deleteUrl;
	private String getUrl;
	private RestTemplate restTemplate;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		this.url = "http://localhost:" + port + "/acs/actions";
		this.deleteUrl = "http://localhost:" + port + "/acs/admin/actions/{adminDomain}/{adminEmail}";
		this.getUrl = "http://localhost:" + port + "/acs/admin/actions/{adminDomain}/{adminEmail}";
		this.createUserUrl = "http://localhost:" + port + "/acs/users";
		this.restTemplate = new RestTemplate();
	}

	@BeforeEach
	public void setup() {
		// Create admin for clear DB
		UserBoundary admin = this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("admin@gmail.com", UserRole.ADMIN, "Admin", "Avatar"), UserBoundary.class);

		// Delete all elements from DB
		this.restTemplate.delete(this.deleteUrl, admin.getUserId().getDomain(), admin.getUserId().getEmail());
	}

	@AfterEach
	public void teardown() {
		// Create admin for clear DB
		UserBoundary admin = this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("admin@gmail.com", UserRole.ADMIN, "Admin", "Avatar"), UserBoundary.class);

		// Delete all elements from DB
		this.restTemplate.delete(this.deleteUrl, admin.getUserId().getDomain(), admin.getUserId().getEmail());
	}

	@Test
	public void testContext() {

	}

	@Test
	public void testPostSingleActionWithNoActionIdServerSaveToDBNewActionBoundaryWithGeneratedIDAndReturnsIt()
			throws Exception {

		// GIVEN the server is up
		// do nothing

		// WHEN I POST /acs/actions with Action Boundary with NO Action Id.

		ActionBoundary newAction = this.restTemplate.postForObject(this.url,
				new ActionBoundary(null, "type", new Element(new ElementId("2020b.lior.trachtman", "don't care")),
						new Date(System.currentTimeMillis()),
						new InvokedBy(new UserId("2020b.lior.trachtman", "don't care")), null),
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
	public void testPostSingleActionWithActionIdServerSaveToDBNewActionBoundaryWithGeneratedIDAndReturnsIt()
			throws Exception {

		// GIVEN the server is up
		// do nothing

		// WHEN I POST /acs/actions with Action Boundary with Action Id = "X".

		final String id = "X";

		ActionBoundary newAction = this.restTemplate.postForObject(this.url,
				new ActionBoundary(new ActionId("2020b.lior.trachtman", id), "type",
						new Element(new ElementId("2020b.lior.trachtman", "don't care")),
						new Date(System.currentTimeMillis()),
						new InvokedBy(new UserId("2020b.lior.trachtman", "don't care")), null),
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

}
