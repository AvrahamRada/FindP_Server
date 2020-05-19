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
import acs.boundaries.ElementIdBoundary;
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
	private String createUserUrl;
	private String searchByNameUrl;
	private String searchByTypeUrl;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {

		this.url = "http://localhost:" + port + "/acs/elements";
		this.createUserUrl = "http://localhost:" + port + "/acs/users";
		this.searchByNameUrl = "http://localhost:" + port
				+ "/acs/elements/{userDomain}/{userEmail}/search/byName/{name}?page={page}&size={size}";
		this.searchByTypeUrl = "http://localhost:" + port
				+ "/acs/elements/{userDomain}/{userEmail}/search/byType/{type}?page={page}&size={size}";
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
	public void testGet10ChildrenOfExistingParent() throws Exception {

		final int X = 10;

		// GIVEN the database contains a Parent with X children

		/* Creating Parent */
		ElementBoundary elementParent = this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
				new ElementBoundary(new ElementId("2020b.lior.trachtman", "x"), "type", "name", true,
						new Date(System.currentTimeMillis()),
						new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
						new Location(40.730610, -73.935242), new HashMap<>()),
				ElementBoundary.class, "2020b.lior.trachtman", "don'tcare1");

		ElementBoundary[] elementChild = new ElementBoundary[X];

		IntStream.range(0, X).forEach(
				i -> elementChild[i] = this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
						new ElementBoundary(new ElementId("2020b.lior.trachtman", "id " + i), "type", "name", true,
								new Date(System.currentTimeMillis()),
								new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
								new Location(40.730610, -73.935242), new HashMap<>()),
						ElementBoundary.class, "2020b.lior.trachtman", "don't care" + i));

		IntStream.range(0, X)
				.forEach(i -> this.restTemplate.put(
						this.url + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
						new ElementIdBoundary(elementChild[i].getElementId().getDomain(),
								elementChild[i].getElementId().getId()),
						elementParent.getCreatedBy().getUserId().getDomain(),
						elementParent.getCreatedBy().getUserId().getEmail(), elementParent.getElementId().getDomain(),
						elementParent.getElementId().getId()));

		// WHEN I GET
		// /elements/{userDomain}/{userEmail}/{elementDomain}/{elementId}/children
		ElementBoundary[] actualElementsChildrenArray = this.restTemplate.getForObject(
				this.url + "/{userDomain}/{userEmail}/{elementDomain}/{elementId}/children", ElementBoundary[].class,
				elementParent.getCreatedBy().getUserId().getDomain(),
				elementParent.getCreatedBy().getUserId().getEmail(), elementParent.getElementId().getDomain(),
				elementParent.getElementId().getId());

		// THEN the server returns array of X element children boundaries
		assertThat(actualElementsChildrenArray).hasSize(X);
	}

	@Test
	public void testGet100ChildrenOfExistingParent() throws Exception {

		final int X = 100;

		// GIVEN the database contains a Parent with X children

		/* Creating Parent */
		ElementBoundary elementParent = this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
				new ElementBoundary(new ElementId("2020b.lior.trachtman", "x"), "type", "name", true,
						new Date(System.currentTimeMillis()),
						new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
						new Location(40.730610, -73.935242), new HashMap<>()),
				ElementBoundary.class, "2020b.lior.trachtman", "don'tcare1");

		ElementBoundary[] elementChild = new ElementBoundary[X];

		IntStream.range(0, X).forEach(
				i -> elementChild[i] = this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
						new ElementBoundary(new ElementId("2020b.lior.trachtman", "id " + i), "type", "name", true,
								new Date(System.currentTimeMillis()),
								new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
								new Location(40.730610, -73.935242), new HashMap<>()),
						ElementBoundary.class, "2020b.lior.trachtman", "don't care" + i));

		IntStream.range(0, X)
				.forEach(i -> this.restTemplate.put(
						this.url + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
						new ElementIdBoundary(elementChild[i].getElementId().getDomain(),
								elementChild[i].getElementId().getId()),
						elementParent.getCreatedBy().getUserId().getDomain(),
						elementParent.getCreatedBy().getUserId().getEmail(), elementParent.getElementId().getDomain(),
						elementParent.getElementId().getId()));

		// WHEN I GET
		// /elements/{userDomain}/{userEmail}/{elementDomain}/{elementId}/children
		ElementBoundary[] actualElementsChildrenArray = this.restTemplate.getForObject(
				this.url + "/{userDomain}/{userEmail}/{elementDomain}/{elementId}/children", ElementBoundary[].class,
				elementParent.getCreatedBy().getUserId().getDomain(),
				elementParent.getCreatedBy().getUserId().getEmail(), elementParent.getElementId().getDomain(),
				elementParent.getElementId().getId());

		// THEN the server returns array of X element children boundaries
		assertThat(actualElementsChildrenArray).hasSize(X);

	}

	@Test
	public void testGet1000ChildrenOfExistingParent() throws Exception {

		final int X = 1000;

		// GIVEN the database contains a Parent with X children

		/* Creating Parent */
		ElementBoundary elementParent = this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
				new ElementBoundary(new ElementId("2020b.lior.trachtman", "x"), "type", "name", true,
						new Date(System.currentTimeMillis()),
						new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
						new Location(40.730610, -73.935242), new HashMap<>()),
				ElementBoundary.class, "2020b.lior.trachtman", "don'tcare1");

		ElementBoundary[] elementChild = new ElementBoundary[X];

		IntStream.range(0, X).forEach(
				i -> elementChild[i] = this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
						new ElementBoundary(new ElementId("2020b.lior.trachtman", "id " + i), "type", "name", true,
								new Date(System.currentTimeMillis()),
								new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
								new Location(40.730610, -73.935242), new HashMap<>()),
						ElementBoundary.class, "2020b.lior.trachtman", "don't care" + i));

		IntStream.range(0, X)
				.forEach(i -> this.restTemplate.put(
						this.url + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
						new ElementIdBoundary(elementChild[i].getElementId().getDomain(),
								elementChild[i].getElementId().getId()),
						elementParent.getCreatedBy().getUserId().getDomain(),
						elementParent.getCreatedBy().getUserId().getEmail(), elementParent.getElementId().getDomain(),
						elementParent.getElementId().getId()));

		// WHEN I GET
		// /elements/{userDomain}/{userEmail}/{elementDomain}/{elementId}/children
		ElementBoundary[] actualElementsChildrenArray = this.restTemplate.getForObject(
				this.url + "/{userDomain}/{userEmail}/{elementDomain}/{elementId}/children", ElementBoundary[].class,
				elementParent.getCreatedBy().getUserId().getDomain(),
				elementParent.getCreatedBy().getUserId().getEmail(), elementParent.getElementId().getDomain(),
				elementParent.getElementId().getId());

		// THEN the server returns array of X element children boundaries
		assertThat(actualElementsChildrenArray).hasSize(X);

	}

	@Test
	public void testGet10000ChildrenOfExistingParent() throws Exception {

		final int X = 10000;

		// GIVEN the database contains a Parent with X children

		/* Creating Parent */
		ElementBoundary elementParent = this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
				new ElementBoundary(new ElementId("2020b.lior.trachtman", "x"), "type", "name", true,
						new Date(System.currentTimeMillis()),
						new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
						new Location(40.730610, -73.935242), new HashMap<>()),
				ElementBoundary.class, "2020b.lior.trachtman", "don'tcare1");

		ElementBoundary[] elementChild = new ElementBoundary[X];

		IntStream.range(0, X).forEach(
				i -> elementChild[i] = this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
						new ElementBoundary(new ElementId("2020b.lior.trachtman", "id " + i), "type", "name", true,
								new Date(System.currentTimeMillis()),
								new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
								new Location(40.730610, -73.935242), new HashMap<>()),
						ElementBoundary.class, "2020b.lior.trachtman", "don't care" + i));

		IntStream.range(0, X)
				.forEach(i -> this.restTemplate.put(
						this.url + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
						new ElementIdBoundary(elementChild[i].getElementId().getDomain(),
								elementChild[i].getElementId().getId()),
						elementParent.getCreatedBy().getUserId().getDomain(),
						elementParent.getCreatedBy().getUserId().getEmail(), elementParent.getElementId().getDomain(),
						elementParent.getElementId().getId()));

		// WHEN I GET
		// /elements/{userDomain}/{userEmail}/{elementDomain}/{elementId}/children
		ElementBoundary[] actualElementsChildrenArray = this.restTemplate.getForObject(
				this.url + "/{userDomain}/{userEmail}/{elementDomain}/{elementId}/children", ElementBoundary[].class,
				elementParent.getCreatedBy().getUserId().getDomain(),
				elementParent.getCreatedBy().getUserId().getEmail(), elementParent.getElementId().getDomain(),
				elementParent.getElementId().getId());

		// THEN the server returns array of X element children boundaries
		assertThat(actualElementsChildrenArray).hasSize(X);

	}

	@Test
	public void testGetExactlyOneParentOfExistingChildren() throws Exception {

		// GIVEN the database contains a Parent with X children

		/* Creating Parents */
		ElementBoundary elementParent1 = this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
				new ElementBoundary(new ElementId("2020b.lior.trachtman", "x"), "type", "name", true,
						new Date(System.currentTimeMillis()),
						new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
						new Location(40.730610, -73.935242), new HashMap<>()),
				ElementBoundary.class, "2020b.lior.trachtman", "don'tcare1");

		ElementBoundary elementParent2 = this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
				new ElementBoundary(new ElementId("2020b.lior.trachtman", "x"), "type", "name", true,
						new Date(System.currentTimeMillis()),
						new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
						new Location(40.730610, -73.935242), new HashMap<>()),
				ElementBoundary.class, "2020b.lior.trachtman", "don'tcare2");

		/* Creating Children */
		ElementBoundary elementsChildren = this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
				new ElementBoundary(new ElementId("2020b.lior.trachtman", "id "), "type", "name", true,
						new Date(System.currentTimeMillis()),
						new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
						new Location(40.730610, -73.935242), new HashMap<>()),
				ElementBoundary.class, "2020b.lior.trachtman", "don't care3");

		/* Bind Children to parent1 */
		this.restTemplate.put(this.url + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
				new ElementIdBoundary(elementsChildren.getElementId().getDomain(),
						elementsChildren.getElementId().getId()),
				elementParent1.getCreatedBy().getUserId().getDomain(),
				elementParent1.getCreatedBy().getUserId().getEmail(), elementParent1.getElementId().getDomain(),
				elementParent1.getElementId().getId());

		/* Bind Children to parent2 */
		this.restTemplate.put(this.url + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
				new ElementIdBoundary(elementsChildren.getElementId().getDomain(),
						elementsChildren.getElementId().getId()),
				elementParent2.getCreatedBy().getUserId().getDomain(),
				elementParent2.getCreatedBy().getUserId().getEmail(), elementParent2.getElementId().getDomain(),
				elementParent2.getElementId().getId());

		/* getting all parents of specific element children */
		ElementBoundary[] ElementsParentsArray = this.restTemplate.getForObject(
				this.url + "/{userDomain}/{userEmail}/{elementDomain}/{elementId}/parents", ElementBoundary[].class,
				elementsChildren.getCreatedBy().getUserId().getDomain(),
				elementsChildren.getCreatedBy().getUserId().getEmail(), elementsChildren.getElementId().getDomain(),
				elementsChildren.getElementId().getId());

		// THEN the server returns array of X element parent boundaries that contain
		// only 1 parent
		assertThat(ElementsParentsArray).hasSize(1);

	}

	@Test
	public void testGetExactlyZeroParentOfExistingChild() throws Exception {

		// GIVEN the database contains a Parent with X children
		/* Creating Children */
		ElementBoundary elementsChildren = this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
				new ElementBoundary(new ElementId("2020b.lior.trachtman", "id "), "type", "name", true,
						new Date(System.currentTimeMillis()),
						new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
						new Location(40.730610, -73.935242), new HashMap<>()),
				ElementBoundary.class, "2020b.lior.trachtman", "don't care3");

		/* getting all parents of specific element children */
		ElementBoundary[] ElementsParentsArray = this.restTemplate.getForObject(
				this.url + "/{userDomain}/{userEmail}/{elementDomain}/{elementId}/parents", ElementBoundary[].class,
				elementsChildren.getCreatedBy().getUserId().getDomain(),
				elementsChildren.getCreatedBy().getUserId().getEmail(), elementsChildren.getElementId().getDomain(),
				elementsChildren.getElementId().getId());

		// THEN the server returns array of X element parent boundaries that contain
		// only 1 parent
		assertThat(ElementsParentsArray).isEmpty();

	}

	@Test
	public void testGetZeroChildrenOfExistingParent() throws Exception {

		final int X = 0;

		// GIVEN the database contains a Parent with X children

		/* Creating Parent */
		ElementBoundary elementParent = this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
				new ElementBoundary(new ElementId("2020b.lior.trachtman", "x"), "type", "name", true,
						new Date(System.currentTimeMillis()),
						new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
						new Location(40.730610, -73.935242), new HashMap<>()),
				ElementBoundary.class, "2020b.lior.trachtman", "don'tcare1");

		// WHEN I GET
		// /elements/{userDomain}/{userEmail}/{elementDomain}/{elementId}/children
		ElementBoundary[] actualElementsChildrenArray = this.restTemplate.getForObject(
				this.url + "/{userDomain}/{userEmail}/{elementDomain}/{elementId}/children", ElementBoundary[].class,
				elementParent.getCreatedBy().getUserId().getDomain(),
				elementParent.getCreatedBy().getUserId().getEmail(), elementParent.getElementId().getDomain(),
				elementParent.getElementId().getId());

		// THEN the server returns array of X element children boundaries
		assertThat(actualElementsChildrenArray).hasSize(X);
	}

	///

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
	public void testGetSingleElementWithElementDomianDiffereceFromProjectNameReturnStatusDifferenceFrom2xx()
			throws Exception {

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
		// THEN the server returns status != 2xx
		assertThrows(Exception.class,
				() -> this.restTemplate.getForObject(this.url + "/{userDomain}/{userEmail}/{elementDomain}/{elementId}",
						ElementBoundary.class, "2020b.lior.trachtman", "don't care", "2020b.sarel.micha", id));

	}

	
	/// ------------------- name
	@Test
	public void testGetElementsByNameWithSize15AndReturns15Elements() throws Exception {
		int X = 15;
		this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("manager@gmail.com", UserRole.MANAGER, "manager", "Avatar"), UserBoundary.class);

		for (int i = 0; i < X; i++) {
			this.restTemplate.postForObject(this.url + "/2020b.lior.trachtman/manager@gmail.com",
					new ElementBoundary(new ElementId("2020b.lior.trachtman", "x"), "type", "sameName", true,
							new Date(System.currentTimeMillis()),
							new CreatedBy(new UserId("2020b.lior.trachtman", "manager@gmail.com")),
							new Location(40.730610, -73.935242), new HashMap<>()),
					ElementBoundary.class, "2020b.lior.trachtman", "don't care");
		}

		ElementBoundary[] elementsByName = this.restTemplate.getForObject(searchByNameUrl, ElementBoundary[].class,
				"2020b.lior.trachtman", "manager@gmail.com", "sameName",0,X);

		assertThat(elementsByName).hasSize(X);

	}
	
	@Test
	public void testGetElementsByNameAndSearchWithWrongNameReturnsEmptyArray() throws Exception {
		int X = 2;
		this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("manager@gmail.com", UserRole.MANAGER, "manager", "Avatar"), UserBoundary.class);

		for (int i = 0; i < X; i++) {
			this.restTemplate.postForObject(this.url + "/2020b.lior.trachtman/manager@gmail.com",
					new ElementBoundary(new ElementId("2020b.lior.trachtman", "x"), "type", "sameName", true,
							new Date(System.currentTimeMillis()),
							new CreatedBy(new UserId("2020b.lior.trachtman", "manager@gmail.com")),
							new Location(40.730610, -73.935242), new HashMap<>()),
					ElementBoundary.class, "2020b.lior.trachtman", "don't care");
		}


		assertThat(this.restTemplate.getForObject(searchByNameUrl, ElementBoundary[].class,
				"2020b.lior.trachtman", "manager@gmail.com", "wrongName")).hasSize(0);
		
	}
	
	@Test
	public void testGetElementsByNameAndCheckNameIsTheSame() throws Exception {
		this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("manager@gmail.com", UserRole.MANAGER, "manager", "Avatar"), UserBoundary.class);
		
		String name = "name";

		this.restTemplate.postForObject(this.url + "/2020b.lior.trachtman/manager@gmail.com",
				new ElementBoundary(new ElementId("2020b.lior.trachtman", "x"), "type", name, true,
						new Date(System.currentTimeMillis()),
						new CreatedBy(new UserId("2020b.lior.trachtman", "manager@gmail.com")),
						new Location(40.730610, -73.935242), new HashMap<>()),
						ElementBoundary.class, "2020b.lior.trachtman", "don't care");
		
		assertThat(this.restTemplate.getForObject(searchByNameUrl, ElementBoundary[].class,
				"2020b.lior.trachtman", "manager@gmail.com", "name")[0].getName()).isEqualTo(name);
	}
	
	

	
	///type
	@Test
	public void testGetElementsByTypeWithSize15AndReturns15Elements() throws Exception {
		int X = 15;
		this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("manager@gmail.com", UserRole.MANAGER, "manager", "Avatar"), UserBoundary.class);

		for (int i = 0; i < X; i++) {
			this.restTemplate.postForObject(this.url + "/2020b.lior.trachtman/manager@gmail.com",
					new ElementBoundary(new ElementId("2020b.lior.trachtman", "x"), "sameType", "name", true,
							new Date(System.currentTimeMillis()),
							new CreatedBy(new UserId("2020b.lior.trachtman", "manager@gmail.com")),
							new Location(40.730610, -73.935242), new HashMap<>()),
					ElementBoundary.class, "2020b.lior.trachtman", "don't care");
		}

		ElementBoundary[] elementsByName = this.restTemplate.getForObject(searchByTypeUrl, ElementBoundary[].class,
				"2020b.lior.trachtman", "manager@gmail.com", "sameType",0,X);

		assertThat(elementsByName).hasSize(X);

	}
	
	@Test
	public void testGetElementsByTypeAndSearchWithWrongTypeReturnsEmptyArray() throws Exception {
		int X = 2;
		this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("manager@gmail.com", UserRole.MANAGER, "manager", "Avatar"), UserBoundary.class);

		for (int i = 0; i < X; i++) {
			this.restTemplate.postForObject(this.url + "/2020b.lior.trachtman/manager@gmail.com",
					new ElementBoundary(new ElementId("2020b.lior.trachtman", "x"), "type", "sameName", true,
							new Date(System.currentTimeMillis()),
							new CreatedBy(new UserId("2020b.lior.trachtman", "manager@gmail.com")),
							new Location(40.730610, -73.935242), new HashMap<>()),
					ElementBoundary.class, "2020b.lior.trachtman", "don't care");
		}


		assertThat(this.restTemplate.getForObject(searchByNameUrl, ElementBoundary[].class,
				"2020b.lior.trachtman", "manager@gmail.com", "wrongName",0,X)).hasSize(0);
		
	}
	
	@Test
	public void testGetElementsByTypeAndCheckTypeIsTheSameType() throws Exception {
		this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("manager@gmail.com", UserRole.MANAGER, "manager", "Avatar"), UserBoundary.class);
		
		String type = "type";

		this.restTemplate.postForObject(this.url + "/2020b.lior.trachtman/manager@gmail.com",
				new ElementBoundary(new ElementId("2020b.lior.trachtman", "x"), type, "name", true,
						new Date(System.currentTimeMillis()),
						new CreatedBy(new UserId("2020b.lior.trachtman", "manager@gmail.com")),
						new Location(40.730610, -73.935242), new HashMap<>()),
						ElementBoundary.class, "2020b.lior.trachtman", "don't care");
		
		assertThat(this.restTemplate.getForObject(searchByTypeUrl, ElementBoundary[].class,
				"2020b.lior.trachtman", "manager@gmail.com", "type",0,10)[0].getType()).isEqualTo(type);
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
