package acs.element;

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

import acs.boundaries.ElementBoundary;
import acs.boundaries.UserBoundary;
import acs.data.UserRole;
import acs.util.CreatedBy;
import acs.util.ElementId;
import acs.util.Location;
import acs.util.NewUserDetails;
import acs.util.UserId;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ElementGETTests {

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
	public void testGetSingleElementWithDatabaseContatingThatElementRetreivesThatElement() throws Exception {

		// GIVEN the database contains a elementDomain 2020b.lior.trachtman with id x

		ElementBoundary newElement = this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
				new ElementBoundary(new ElementId("2020b.lior.trachtman", "x"), "type", "name", true,
						new Date(System.currentTimeMillis()),
						new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
						new Location(40.730610, -73.935242), new HashMap<>()),
				ElementBoundary.class, "2020b.lior.trachtman", "don't care");
		String id = newElement.getElementId().getId();

		// WHEN I GET /elements/{userDomain}/{userEmail}/2020b.lior.trachtman/x
		ElementBoundary actualElement = this.restTemplate.getForObject(
				this.url + "/{userDomain}/{userEmail}/{elementDomain}/{elementId}", ElementBoundary.class,
				"2020b.lior.trachtman", "don't care", "2020b.lior.trachtman", id);

		// THEN the server returns a element boundary with elementDomain : 2020b.lior.trachtman AND id: x
		assertThat(actualElement.getElementId().getId()).isEqualTo(id);
	}

	@Test
	public void testGetSingleElementFromServerWithEmptyDatabaseReturnStatusDifferenceFrom2xx() throws Exception {
		// GIVEN the server is up
		// do nothing

		String id = "anyId";

		// WHEN I GET /Element/anyId
		// THEN the server returns status != 2xx
		assertThrows(Exception.class,
				() -> this.restTemplate.getForObject(this.url + "/{userDomain}/{userEmail}/{elementDomain}/{elementId}",
						ElementBoundary.class, "2020b.lior.trachtman", "don't care", "2020b.lior.trachtman", id));
	}

	@Test
	public void testGetSingleElementFromServerWithDatabaseDoesNotContaintThatElementReturnStatusDifferenceFrom2xx()
			throws Exception {

		// GIVEN the database contains a elementDomain 2020b.lior.trachtman with id x
		this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
				new ElementBoundary(new ElementId("2020b.lior.trachtman", "x"), "type", "name", true,
						new Date(System.currentTimeMillis()),
						new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
						new Location(40.730610, -73.935242), new HashMap<>()),
				ElementBoundary.class, "2020b.lior.trachtman", "don't care");

		String id = "y";

		// WHEN I GET /elements/{userDomain}/{userEmail}/2020b.lior.trachtman/y
		// THEN the server returns status != 2xx
		assertThrows(Exception.class,
				() -> this.restTemplate.getForObject(this.url + "/{userDomain}/{userEmail}/{elementDomain}/{elementId}",
						ElementBoundary.class, "2020b.lior.trachtman", "don't care", "2020b.lior.trachtman", id));
	}

	@Test
	public void testGetSingleElementWithNoIdFromServerWithDatabaseReturnStatusDifferenceFrom2xx() {

		// GIVEN the database contains a elementDomain 2020b.lior.trachtman with id
		// "anyId"
		this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
				new ElementBoundary(new ElementId("2020b.lior.trachtman", "anyId"), "type", "name", true,
						new Date(System.currentTimeMillis()),
						new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
						new Location(40.730610, -73.935242), new HashMap<>()),
				ElementBoundary.class, "2020b.lior.trachtman", "don't care");

		// WHEN I GET /elements/{userDomain}/{userEmail}/2020b.lior.trachtman
		// THEN the server returns status != 2xx
		assertThrows(Exception.class,
				() -> this.restTemplate.getForObject(this.url + "/{userDomain}/{userEmail}/{elementDomain}",
						ElementBoundary.class, "2020b.lior.trachtman", "don't care", "2020b.lior.trachtman"));

	}

	@Test
	public void testGetAllElementsFromServerWithXMessagesInDatabaseReturnsAllMessagesStoredInDatabase()
			throws Exception {

		final int X = 10;

		// GIVEN database contains specific X messages
		List<ElementBoundary> storedMessages = new ArrayList<>();
		for (int i = 0; i < X; i++) {
			storedMessages.add(this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
					new ElementBoundary(new ElementId("2020b.lior.trachtman", "id " + i), "type", "name", true,
							new Date(System.currentTimeMillis()),
							new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
							new Location(40.730610, -73.935242), new HashMap<>()),
					ElementBoundary.class, "2020b.lior.trachtman", "don't care"));
		}

		// WHEN I GET /elements//{userDomain}/{userEmail}
		ElementBoundary[] actualElementsArray = this.restTemplate.getForObject(this.url + "/{userDomain}/{userEmail}",
				ElementBoundary[].class, "2020b.lior.trachtman", "any email");

		// THEN the server returns the same X messages in the database
		assertThat(actualElementsArray).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrderElementsOf(storedMessages);
	}

	@Test
	public void testGetAllElementsFromServerWithXMessagesInDatabaseReturnArraysOfXMessages() throws Exception {

		final int X = 10;
		// GIVEN the database contains X messages
		IntStream.range(0, X)
				.forEach(i -> this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
						new ElementBoundary(new ElementId("2020b.lior.trachtman", "id " + i), "type", "name", true,
								new Date(System.currentTimeMillis()),
								new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
								new Location(40.730610, -73.935242), new HashMap<>()),
						ElementBoundary.class, "2020b.lior.trachtman", "don't care"));

		// WHEN I GET /elements/{userDomain}/{userEmail}
		ElementBoundary[] actualElementsArray = this.restTemplate.getForObject(this.url + "/{userDomain}/{userEmail}",
				ElementBoundary[].class, "2020b.lior.trachtman", "don't care");

		// THEN the server returns array of X element boundaries
		assertThat(actualElementsArray).hasSize(X);

	}

	@Test
	public void testGetAllElementsFromServerWithEmptyDatabaseReturnAnEmptyArray() throws Exception {

		final int ZERO = 0;
		// GIVEN the server is up
		// do nothing

		// WHEN I GET /elements/{userDomain}/{userEmail}
		ElementBoundary[] actualElementsArray = this.restTemplate.getForObject(this.url + "/{userDomain}/{userEmail}",
				ElementBoundary[].class, "2020b.lior.trachtman", "don't care");
		
		// THEN the server returns array of 0 element boundaries
		assertThat(actualElementsArray).hasSize(ZERO);

	}
}
