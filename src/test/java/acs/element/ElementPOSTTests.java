package acs.element;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import acs.util.Element;
import acs.util.ElementId;
import acs.util.Location;
import acs.util.NewUserDetails;
import acs.util.TestUtil;
import acs.util.UserId;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ElementPOSTTests {

	private int port;
	private String url;
	private String createUserUrl;
	private String getAllElementsUrl;
	private RestTemplate restTemplate;
	private String createNewElement;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {

		// Elements url's
		this.url = "http://localhost:" + port + "/acs/elements";
		this.createNewElement = "http://localhost:" + port + "/acs/elements/{managerDomain}/{managerEmail}";
		this.getAllElementsUrl = "http://localhost:" + port + "/acs/elements/{userDomain}/{userEmail}";

		// User url's
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
	public void testPostSingleElementWithNoElementIdServerSaveToDBNewElementEntityWithGeneratedID() throws Exception {

		// GIVEN the server is up
		// do nothing

		// WHEN I POST /acs/elements/2020b.lior.trachtman/morsof48@gmail.com with
		// ElementBoundary with NO entity Id.

		ElementBoundary newElement = this.restTemplate.postForObject(this.createNewElement,
				new ElementBoundary(null, "type", "mor", true, null,
						new CreatedBy(new UserId("2020b.lior.trachtman", "morsof48@gmail.com")),
						new Location(32.11111, 33.11111), new HashMap<String, Object>()),
				ElementBoundary.class, "2020b.lior.trachtman", "morsof48@gmail.com");

		// THEN the server save the new element entity with
		// elementDomain : 2020b.lior.trachtman AND
		// generated UUID and returns it.

		// Create user for get all Elements from DB.
		UserBoundary user = this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("admin@gmail.com", UserRole.PLAYER, "user", "Avatar"), UserBoundary.class);

		ElementBoundary[] actualElementArray = this.restTemplate.getForObject(this.getAllElementsUrl,
				ElementBoundary[].class, user.getUserId().getDomain(), user.getUserId().getEmail());

		assertThat(actualElementArray).usingRecursiveFieldByFieldElementComparator().contains(newElement);

	}

	@Test
	public void testPostSingleElementWithElementIdServerSaveToDBNewElementEntityWithGeneratedID() throws Exception {

		// GIVEN the server is up
		// do nothing

		// GIVEN the server is up do nothing
		// WHEN I POST /acs/elements/2020b.lior.trachtman/morsof48@gmail.com with
		// Element Boundary with entity Id = "X".

		final String id = "X";

		ElementBoundary newElement = this.restTemplate.postForObject(this.createNewElement,
				new ElementBoundary(new ElementId("koko", id), "type", "mor", true, null,
						new CreatedBy(new UserId("2020b.lior.trachtman", "morsof48@gmail.com")),
						new Location(32.11111, 33.11111), new HashMap<String, Object>()),
				ElementBoundary.class, "2020b.lior.trachtman", "morsof48@gmail.com");

		// THEN the server save the new element boundary and set
		// elementDomain to 2020b.lior.trachtman AND
		// set the id to generated UUID and returns it.

		// Create user for get all Elements from DB.
		UserBoundary user = this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("admin@gmail.com", UserRole.PLAYER, "user", "Avatar"), UserBoundary.class);

		ElementBoundary[] actualElementArray = this.restTemplate.getForObject(this.getAllElementsUrl,
				ElementBoundary[].class, user.getUserId().getDomain(), user.getUserId().getEmail());

		assertThat(actualElementArray).usingRecursiveFieldByFieldElementComparator().contains(newElement);

	}

	@Test
	public void testPostSingleElementWithNullTypeDatabaseReturnStatusDifferenceFrom2xx() throws Exception {

		// GIVEN the server is up
		// do nothing

		// WHEN I POST /acs/element//2020b.lior.trachtman/morsof48@gmail.com with
		// Element Boundary with no Type

		// THEN the server returns status != 2xx
		assertThrows(Exception.class,
				() -> this.restTemplate.postForObject(this.createNewElement,
						new ElementBoundary(null, null, "mor", true, null,
								new CreatedBy(new UserId("2020b.lior.trachtman", "morsof48@gmail.com")),
								new Location(32.11111, 33.11111), new HashMap<String, Object>()),
						ElementBoundary.class, "2020b.lior.trachtman", "morsof48@gmail.com"));
	}

	@Test
	public void testPostSingleElementWithNullNameDatabaseReturnStatusDifferenceFrom2xx() throws Exception {

		// GIVEN the server is up
		// do nothing

		// WHEN I POST /acs/element//2020b.lior.trachtman/morsof48@gmail.com with
		// Element Boundary with no Name

		// THEN the server returns status != 2xx
		assertThrows(Exception.class,
				() -> this.restTemplate.postForObject(this.createNewElement,
						new ElementBoundary(null, "type", null, true, null,
								new CreatedBy(new UserId("2020b.lior.trachtman", "morsof48@gmail.com")),
								new Location(32.11111, 33.11111), new HashMap<String, Object>()),
						ElementBoundary.class, "2020b.lior.trachtman", "morsof48@gmail.com"));

	}

	@Test
	public void testPostSingleElementWithNullActiveDatabaseReturnStatusDifferenceFrom2xx() throws Exception {

		// GIVEN the server is up
		// do nothing

		// WHEN I POST /acs/element/2020b.lior.trachtman/morsof48@gmail.com with Element
		// Boundary with null active

		// THEN the server returns status != 2xx
		assertThrows(Exception.class,
				() -> this.restTemplate.postForObject(this.createNewElement,
						new ElementBoundary(null, "type", "Joe", null, null,
								new CreatedBy(new UserId("2020b.lior.trachtman", "morsof48@gmail.com")),
								new Location(32.11111, 33.11111), new HashMap<String, Object>()),
						ElementBoundary.class, "2020b.lior.trachtman", "morsof48@gmail.com"));

	}

	@Test
	public void testPostSingleElementWithNoCreatedByDatabaseReturnStatusDifferenceFrom2xx() throws Exception {

		// GIVEN the server is up
		// do nothing

		// WHEN I POST /acs/element/2020b.lior.trachtman/morsof48@gmail.com with Element
		// Boundary with no CreatedBy

		// THEN the server returns status != 2xx
		assertThrows(Exception.class,
				() -> this.restTemplate.postForObject(this.createNewElement,
						new ElementBoundary(new ElementId("koko", "momo"), "type", "mor", true, null, null,
								new Location(32.11111, 33.11111), new HashMap<String, Object>()),
						ElementBoundary.class, "2020b.lior.trachtman", "morsof48@gmail.com"));

	}

	@Test
	public void testPostSingleElementWithNullUserIdDatabaseReturnStatusDifferenceFrom2xx() throws Exception {

		// GIVEN the server is up
		// do nothing

		// WHEN I POST /acs/element//2020b.lior.trachtman/morsof48@gmail.com with
		// Element Boundary with null UserId

		// THEN the server returns status != 2xx
		assertThrows(Exception.class,
				() -> this.restTemplate.postForObject(this.createNewElement,
						new ElementBoundary(null, "type", "Joe", false, null, new CreatedBy(null),
								new Location(32.11111, 33.11111), new HashMap<String, Object>()),
						ElementBoundary.class, "2020b.lior.trachtman", "morsof48@gmail.com"));

	}

	@Test
	public void testPostSingleElementWithNullUserIdDomainDatabaseReturnStatusDifferenceFrom2xx() throws Exception {

		// GIVEN the server is up
		// do nothing

		// WHEN I POST /acs/element//2020b.lior.trachtman/morsof48@gmail.com with
		// Element Boundary with null UserId - Domain

		// THEN the server returns status != 2xx
		assertThrows(Exception.class,
				() -> this.restTemplate.postForObject(this.createNewElement,
						new ElementBoundary(null, "type", "Joe", false, null,
								new CreatedBy(new UserId(null, "morsof48@gmail.com")), new Location(32.11111, 33.11111),
								new HashMap<String, Object>()),
						ElementBoundary.class, "2020b.lior.trachtman", "morsof48@gmail.com"));

	}

	@Test
	public void testPostSingleElementWithNullUserIdEmailDatabaseReturnStatusDifferenceFrom2xx() throws Exception {

		// GIVEN the server is up
		// do nothing

		// WHEN I POST /acs/element//2020b.lior.trachtman/morsof48@gmail.com with
		// Element Boundary with null UserId - Email

		// THEN the server returns status != 2xx
		assertThrows(Exception.class,
				() -> this.restTemplate.postForObject(this.createNewElement,
						new ElementBoundary(null, "type", "Joe", false, null,
								new CreatedBy(new UserId("2020b.lior.trachtman", null)),
								new Location(32.11111, 33.11111), new HashMap<String, Object>()),
						ElementBoundary.class, "2020b.lior.trachtman", "morsof48@gmail.com"));

	}

	@Test
	public void testPostSingleElementWithNullLocationEmailDatabaseReturnStatusDifferenceFrom2xx() throws Exception {

		// GIVEN the server is up
		// do nothing

		// WHEN I POST /acs/element//2020b.lior.trachtman/morsof48@gmail.com with
		// Element Boundary with null Location

		// THEN the server returns status != 2xx
		assertThrows(Exception.class,
				() -> this.restTemplate.postForObject(this.createNewElement,
						new ElementBoundary(null, "type", "Joe", false, null,
								new CreatedBy(new UserId("2020b.lior.trachtman", "morsof48@gmail.com")), null,
								new HashMap<String, Object>()),
						ElementBoundary.class, "2020b.lior.trachtman", "morsof48@gmail.com"));

	}

	@Test
	public void testPostSingleElementWithNullLocationLatEmailDatabaseReturnStatusDifferenceFrom2xx() throws Exception {

		// GIVEN the server is up
		// do nothing

		// WHEN I POST /acs/element//2020b.lior.trachtman/morsof48@gmail.com with
		// Element Boundary with null lat Location

		// THEN the server returns status != 2xx
		assertThrows(Exception.class,
				() -> this.restTemplate.postForObject(this.createNewElement,
						new ElementBoundary(null, "type", "Joe", false, null,
								new CreatedBy(new UserId("2020b.lior.trachtman", "morsof48@gmail.com")),
								new Location(null, 33.11111), new HashMap<String, Object>()),
						ElementBoundary.class, "2020b.lior.trachtman", "morsof48@gmail.com"));

	}

	@Test
	public void testPostSingleElementWithNullLocationLngEmailDatabaseReturnStatusDifferenceFrom2xx() throws Exception {

		// GIVEN the server is up
		// do nothing

		// WHEN I POST /acs/element//2020b.lior.trachtman/morsof48@gmail.com with
		// Element Boundary with null lng Location

		// THEN the server returns status != 2xx
		assertThrows(Exception.class,
				() -> this.restTemplate.postForObject(this.createNewElement,
						new ElementBoundary(null, "type", "Joe", false, null,
								new CreatedBy(new UserId("2020b.lior.trachtman", "morsof48@gmail.com")),
								new Location(33.11111, null), new HashMap<String, Object>()),
						ElementBoundary.class, "2020b.lior.trachtman", "morsof48@gmail.com"));

	}

	@Test
	public void testPostSingleElementWithNullElementAttributesLngEmailDatabaseReturnStatusDifferenceFrom2xx()
			throws Exception {

		// GIVEN the server is up
		// do nothing

		// WHEN I POST /acs/element//2020b.lior.trachtman/morsof48@gmail.com with
		// Element Boundary with null ElementAttributes

		// THEN the server returns status != 2xx
		assertThrows(Exception.class,
				() -> this.restTemplate.postForObject(this.createNewElement,
						new ElementBoundary(null, "type", "Joe", false, null,
								new CreatedBy(new UserId("2020b.lior.trachtman", "morsof48@gmail.com")),
								new Location(33.123456, 32.123456), null),
						ElementBoundary.class, "2020b.lior.trachtman", "morsof48@gmail.com"));

	}

	@Test
	public void testPostSingleElementWithNullElementAttributeValueLngEmailDatabaseReturnStatusDifferenceFrom2xx()
			throws Exception {

		// GIVEN the server is up
		// do nothing

		// WHEN I POST /acs/element//2020b.lior.trachtman/morsof48@gmail.com with
		// Element Boundary with null ElementAttributes value

		// THEN the server returns status != 2xx

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("key", null);

		assertThrows(Exception.class,
				() -> this.restTemplate.postForObject(this.createNewElement,
						new ElementBoundary(null, "type", "Joe", false, null,
								new CreatedBy(new UserId("2020b.lior.trachtman", "morsof48@gmail.com")),
								new Location(33.123456, 32.123456), map),
						ElementBoundary.class, "2020b.lior.trachtman", "morsof48@gmail.com"));

	}

	@Test
	public void testPostSingleElementWithNullElementAttributeKeyLngEmailDatabaseReturnStatusDifferenceFrom2xx()
			throws Exception {

		// GIVEN the server is up
		// do nothing

		// WHEN I POST /acs/element//2020b.lior.trachtman/morsof48@gmail.com with
		// Element Boundary with null ElementAttributes key

		// THEN the server returns status != 2xx

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(null, "value");

		assertThrows(Exception.class,
				() -> this.restTemplate.postForObject(this.createNewElement,
						new ElementBoundary(null, "type", "Joe", false, null,
								new CreatedBy(new UserId("2020b.lior.trachtman", "morsof48@gmail.com")),
								new Location(33.123456, 32.123456), map),
						ElementBoundary.class, "2020b.lior.trachtman", "morsof48@gmail.com"));

	}

	@Test
	public void test2019YearOnCreatedTimestampDatabaseStoreElementEntityWithGenereatedTimestamp()
			throws ParseException {

		// GIVEN the server is up
		// do nothing

		// WHEN I POST /acs/elements/2020b.lior.trachtman/morsof48@gmail.com with
		// Element Boundary with NO Element Id.
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = formatter.parse("2019-03-01");

		ElementBoundary newElement = this.restTemplate.postForObject(this.createNewElement,
				new ElementBoundary(null, "type", "mor", true, date,
						new CreatedBy(new UserId("2020b.lior.trachtman", "morsof48@gmail.com")),
						new Location(32.11111, 33.11111), new HashMap<String, Object>()),
				ElementBoundary.class, "2020b.lior.trachtman", "morsof48@gmail.com");

		// THEN the server save the new element entity with
		// elementDomain : 2020b.lior.trachtman AND
		// generated UUID and returns it.

		// Create user for get all Elements from DB.
		UserBoundary user = this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("admin@gmail.com", UserRole.PLAYER, "user", "Avatar"), UserBoundary.class);

		ElementBoundary[] actualElementArray = this.restTemplate.getForObject(this.getAllElementsUrl,
				ElementBoundary[].class, user.getUserId().getDomain(), user.getUserId().getEmail());

		assertThat(actualElementArray).usingRecursiveFieldByFieldElementComparator().contains(newElement);

	}

	@Test
	public void testPost10ValidElementsServerSaveToDBAllEntitesWithGeneratedID() throws Exception {

		final int X = 10;

		// GIVEN the server is up
		// do nothing

		List<ElementBoundary> storedElements = new ArrayList<>();

		for (int i = 0; i < X; i++) {
			storedElements.add(this.restTemplate.postForObject(this.createNewElement,
					new ElementBoundary(null, "type", "mor", true, null,
							new CreatedBy(new UserId("2020b.lior.trachtman", "morsof48@gmail.com")),
							new Location(32.11111, 33.11111), new HashMap<String, Object>()),
					ElementBoundary.class, "2020b.lior.trachtman", "morsof48@gmail.com"));
		}

		// Create user for get all Elements from DB.
		UserBoundary user = this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("admin@gmail.com", UserRole.PLAYER, "user", "Avatar"), UserBoundary.class);

		// WHEN I POST X elements boundaries to the server
		ElementBoundary[] actualElementArray = this.restTemplate.getForObject(this.getAllElementsUrl,
				ElementBoundary[].class, user.getUserId().getDomain(), user.getUserId().getEmail());

		// THEN the server returns the same X elements in the database (which mean DB
		// saved the element entities
		assertThat(actualElementArray).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrderElementsOf(storedElements);

	}

	@Test
	public void testPost10000ValidElementsServerSaveToDBAllEntitesWithGeneratedID() throws Exception {

		final int X = 10000;

		// GIVEN the server is up
		// do nothing

		List<ElementBoundary> storedElements = new ArrayList<>();

		for (int i = 0; i < X; i++) {
			storedElements.add(this.restTemplate.postForObject(this.createNewElement,
					new ElementBoundary(null, "type", "mor", true, null,
							new CreatedBy(new UserId("2020b.lior.trachtman", "morsof48@gmail.com")),
							new Location(32.11111, 33.11111), new HashMap<String, Object>()),
					ElementBoundary.class, "2020b.lior.trachtman", "morsof48@gmail.com"));
		}

		// Create user for get all Elements from DB.
		UserBoundary user = this.restTemplate.postForObject(this.createUserUrl,
				new NewUserDetails("admin@gmail.com", UserRole.PLAYER, "user", "Avatar"), UserBoundary.class);

		// WHEN I POST X elements boundaries to the server
		ElementBoundary[] actualElementArray = this.restTemplate.getForObject(this.getAllElementsUrl,
				ElementBoundary[].class, user.getUserId().getDomain(), user.getUserId().getEmail());

		// THEN the server returns the same X elements in the database (which mean DB
		// saved the element entities
		assertThat(actualElementArray).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrderElementsOf(storedElements);

	}

}
