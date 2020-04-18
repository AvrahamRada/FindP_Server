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
import acs.util.TestUtil;
import acs.util.UserId;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ElementGETTests {

	private int port;

	// Element URL
	private String url;

	private RestTemplate restTemplate;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {

		this.url = "http://localhost:" + port + "/acs/elements";
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
	public void testGetSingleElementWithDatabaseContatingThatElementRetreivesThatElement() throws Exception {

		// GIVEN the database contains a elementDomain 2020b.lior.trachtman with
		// generated id

		ElementBoundary newElement = this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
				new ElementBoundary(new ElementId("2020b.lior.trachtman", "x"), "type", "name", true,
						new Date(System.currentTimeMillis()),
						new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
						new Location(40.730610, -73.935242), new HashMap<>()),
				ElementBoundary.class, "2020b.lior.trachtman", "don't care");

		// Retrieve the generated ID
		String id = newElement.getElementId().getId();

		// WHEN I GET /elements/{userDomain}/{userEmail}/2020b.lior.trachtman/id
		ElementBoundary actualElement = this.restTemplate.getForObject(
				this.url + "/{userDomain}/{userEmail}/{elementDomain}/{elementId}", ElementBoundary.class,
				"2020b.lior.trachtman", "don't care", "2020b.lior.trachtman", id);

		// THEN the server returns a element boundary with elementDomain :
		// 2020b.lior.trachtman AND id: x
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

		// GIVEN the database contains a elementDomain 2020b.lior.trachtman with
		// generated id
		this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
				new ElementBoundary(new ElementId("2020b.lior.trachtman", "x"), "type", "name", true,
						new Date(System.currentTimeMillis()),
						new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
						new Location(40.730610, -73.935242), new HashMap<>()),
				ElementBoundary.class, "2020b.lior.trachtman", "don't care");

		String id = "SomeId";

		// WHEN I GET /elements/{userDomain}/{userEmail}/2020b.lior.trachtman/someId
		// THEN the server returns status != 2xx
		assertThrows(Exception.class,
				() -> this.restTemplate.getForObject(this.url + "/{userDomain}/{userEmail}/{elementDomain}/{elementId}",
						ElementBoundary.class, "2020b.lior.trachtman", "don't care", "2020b.lior.trachtman", id));
	}

	@Test
	public void testGetSingleElementWithNoIdFromServerWithDatabaseReturnStatusDifferenceFrom2xx() {

		// GIVEN the database contains a elementDomain 2020b.lior.trachtman with
		// generated id
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
	public void testGetAllElementsFromServerWith10ElementsInDatabaseReturnsAllElementsStoredInDatabase()
			throws Exception {

		final int X = 10;

		// GIVEN database contains specific X elements
		List<ElementBoundary> storedElements = new ArrayList<>();
		for (int i = 0; i < X; i++) {
			storedElements.add(this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
					new ElementBoundary(new ElementId("2020b.lior.trachtman", "id " + i), "type", "name", true,
							new Date(System.currentTimeMillis()),
							new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
							new Location(40.730610, -73.935242), new HashMap<>()),
					ElementBoundary.class, "2020b.lior.trachtman", "don't care"));
		}

		// WHEN I GET /elements//{userDomain}/{userEmail}
		ElementBoundary[] actualElementsArray = this.restTemplate.getForObject(this.url + "/{userDomain}/{userEmail}",
				ElementBoundary[].class, "2020b.lior.trachtman", "any email");

		// THEN the server returns the same X elements in the database
		assertThat(actualElementsArray).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrderElementsOf(storedElements);
	}

	@Test
	public void testGetAllElementsFromServerWith100ElementsInDatabaseReturnsAllElementsStoredInDatabase()
			throws Exception {

		final int X = 100;

		// GIVEN database contains specific X elements
		List<ElementBoundary> storedElements = new ArrayList<>();
		for (int i = 0; i < X; i++) {
			storedElements.add(this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
					new ElementBoundary(new ElementId("2020b.lior.trachtman", "id " + i), "type", "name", true,
							new Date(System.currentTimeMillis()),
							new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
							new Location(40.730610, -73.935242), new HashMap<>()),
					ElementBoundary.class, "2020b.lior.trachtman", "don't care"));
		}

		// WHEN I GET /elements//{userDomain}/{userEmail}
		ElementBoundary[] actualElementsArray = this.restTemplate.getForObject(this.url + "/{userDomain}/{userEmail}",
				ElementBoundary[].class, "2020b.lior.trachtman", "any email");

		// THEN the server returns the same X elements in the database
		assertThat(actualElementsArray).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrderElementsOf(storedElements);
	}

	@Test
	public void testGetAllElementsFromServerWith1000ElementsInDatabaseReturnsAllElementsStoredInDatabase()
			throws Exception {

		final int X = 1000;

		// GIVEN database contains specific X elements
		List<ElementBoundary> storedElements = new ArrayList<>();
		for (int i = 0; i < X; i++) {
			storedElements.add(this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
					new ElementBoundary(new ElementId("2020b.lior.trachtman", "id " + i), "type", "name", true,
							new Date(System.currentTimeMillis()),
							new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
							new Location(40.730610, -73.935242), new HashMap<>()),
					ElementBoundary.class, "2020b.lior.trachtman", "don't care"));
		}

		// WHEN I GET /elements//{userDomain}/{userEmail}
		ElementBoundary[] actualElementsArray = this.restTemplate.getForObject(this.url + "/{userDomain}/{userEmail}",
				ElementBoundary[].class, "2020b.lior.trachtman", "any email");

		// THEN the server returns the same X elements in the database
		assertThat(actualElementsArray).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrderElementsOf(storedElements);
	}

	@Test
	public void testGetAllElementsFromServerWith10000ElementsInDatabaseReturnsAllElementsStoredInDatabase()
			throws Exception {

		final int X = 10000;

		// GIVEN database contains specific X elements
		List<ElementBoundary> storedElements = new ArrayList<>();
		for (int i = 0; i < X; i++) {
			storedElements.add(this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
					new ElementBoundary(new ElementId("2020b.lior.trachtman", "id " + i), "type", "name", true,
							new Date(System.currentTimeMillis()),
							new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
							new Location(40.730610, -73.935242), new HashMap<>()),
					ElementBoundary.class, "2020b.lior.trachtman", "don't care"));
		}

		// WHEN I GET /elements//{userDomain}/{userEmail}
		ElementBoundary[] actualElementsArray = this.restTemplate.getForObject(this.url + "/{userDomain}/{userEmail}",
				ElementBoundary[].class, "2020b.lior.trachtman", "any email");

		// THEN the server returns the same X elements in the database
		assertThat(actualElementsArray).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrderElementsOf(storedElements);
	}

	@Test
	public void testGetAllElementsFromServerWith10ElementsInDatabaseReturnArraysOf10Elements() throws Exception {

		final int X = 10;

		// GIVEN the database contains X elements
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
	public void testGetAllElementsFromServerWith100ElementsInDatabaseReturnArraysOf100Elements() throws Exception {

		final int X = 100;

		// GIVEN the database contains X elements
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

	public void testGetAllElementsFromServerWith1000ElementsInDatabaseReturnArraysOf1000Elements() throws Exception {

		final int X = 1000;

		// GIVEN the database contains X elements
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

	public void testGetAllElementsFromServerWith10000ElementsInDatabaseReturnArraysOf10000Elements() throws Exception {

		final int X = 10000;

		// GIVEN the database contains X elements
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

	@Test
	public void testGetAllElementsWithUserDomianDiffereceFromProjectNameReturnsAllElementsStoredInDatabase()
			throws Exception {


		final int X = 10;

		// GIVEN database contains specific X elements
		List<ElementBoundary> storedElements = new ArrayList<>();
		for (int i = 0; i < X; i++) {
			storedElements.add(this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
					new ElementBoundary(new ElementId("2020b.lior.trachtman", "id " + i), "type", "name", true,
							new Date(System.currentTimeMillis()),
							new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
							new Location(40.730610, -73.935242), new HashMap<>()),
					ElementBoundary.class, "2020b.lior.trachtman", "don't care"));
		}

		// WHEN I GET /elements//{userDomain}/{userEmail}
		ElementBoundary[] actualElementsArray = this.restTemplate.getForObject(this.url + "/{userDomain}/{userEmail}",
				ElementBoundary[].class, "2020b.sarel.micha", "any email");

		// THEN the server returns the same X elements in the database
		assertThat(actualElementsArray).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrderElementsOf(storedElements);
	}

	@Test
	public void testGetSingleElementWithUserDomianDiffereceFromProjectNameRetreivesThatElement() throws Exception {

		// GIVEN the database contains a elementDomain 2020b.lior.trachtman with id
		// generated id

		ElementBoundary newElement = this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
				new ElementBoundary(new ElementId("2020b.lior.trachtman", "x"), "type", "name", true,
						new Date(System.currentTimeMillis()),
						new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
						new Location(40.730610, -73.935242), new HashMap<>()),
				ElementBoundary.class, "2020b.lior.trachtman", "don't care");

		// Retrieve the generated ID
		String id = newElement.getElementId().getId();

		// WHEN I GET /elements/{userDomain}/{userEmail}/2020b.lior.trachtman/id
		ElementBoundary actualElement = this.restTemplate.getForObject(
				this.url + "/{userDomain}/{userEmail}/{elementDomain}/{elementId}", ElementBoundary.class,
				"2020b.sarel.micha", "don't care", "2020b.lior.trachtman", id);

		// THEN the server returns a element boundary with elementDomain :
		// 2020b.lior.trachtman AND id: x
		assertThat(actualElement.getElementId().getId()).isEqualTo(id);

	}

	@Test
	public void testGetSingleElementWithElementDomianDiffereceFromProjectNameRetreivesThatElement() throws Exception {

		// GIVEN the database contains a elementDomain 2020b.lior.trachtman with id
		// generated id

		ElementBoundary newElement = this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
				new ElementBoundary(new ElementId("2020b.lior.trachtman", "x"), "type", "name", true,
						new Date(System.currentTimeMillis()),
						new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
						new Location(40.730610, -73.935242), new HashMap<>()),
				ElementBoundary.class, "2020b.lior.trachtman", "don't care");

		// Retrieve the generated ID
		String id = newElement.getElementId().getId();

		// WHEN I GET /elements/{userDomain}/{userEmail}/2020b.lior.trachtman/id
		ElementBoundary actualElement = this.restTemplate.getForObject(
				this.url + "/{userDomain}/{userEmail}/{elementDomain}/{elementId}", ElementBoundary.class,
				"2020b.lior.trachtman", "don't care", "2020b.sarel.micha", id);

		// THEN the server returns a element boundary with elementDomain :
		// 2020b.lior.trachtman AND id: x
		assertThat(actualElement.getElementId().getId()).isEqualTo(id);

	}

}
