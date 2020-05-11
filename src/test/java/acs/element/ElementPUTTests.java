package acs.element;

import static org.assertj.core.api.Assertions.assertThat;



import java.util.Date;
import java.util.HashMap;


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
import acs.util.CreatedBy;
import acs.util.ElementId;
import acs.util.Location;
import acs.util.TestUtil;
import acs.util.UserId;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ElementPUTTests {
	

	private int port;
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
	public void testBindParentToChild() throws Exception{
		// GIVEN the database contains a elementDomain 2020b.lior.trachtman with generated id
		ElementBoundary elementParent = this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
				new ElementBoundary(new ElementId("2020b.lior.trachtman", "x"), "type", "name", true,
						new Date(System.currentTimeMillis()),
						new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
						new Location(40.730610, -73.935242), new HashMap<>()),
				ElementBoundary.class,"2020b.lior.trachtman", "don'tcare1");
		
		
		// GIVEN the database contains a elementDomain 2020b.lior.trachtman with generated id
		ElementBoundary elementChild = this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
				new ElementBoundary(new ElementId("2020b.lior.trachtman", "y"), "type", "name", true,
						new Date(System.currentTimeMillis()),
						new CreatedBy(new UserId("2020b.lior.trachtman", "test.micha@s.afeka.ac.il")),
						new Location(40.730610, 73.935242), new HashMap<>()),
				ElementBoundary.class,"2020b.lior.trachtman", "don'tcare2");
			
		this.restTemplate.put(this.url + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
				new ElementIdBoundary(elementChild.getElementId().getDomain(), elementChild.getElementId().getId()),
				elementParent.getCreatedBy().getUserId().getDomain(),elementParent.getCreatedBy().getUserId().getEmail(),
				elementParent.getElementId().getDomain(),elementParent.getElementId().getId());
		
		ElementBoundary[] ElementsArray = this.restTemplate.getForObject(this.url + "/{userDomain}/{userEmail}/{elementDomain}/{elementId}/parents",
				ElementBoundary[].class, 
				elementChild.getCreatedBy().getUserId().getDomain(), elementChild.getCreatedBy().getUserId().getEmail(),
				elementChild.getElementId().getDomain(),elementChild.getElementId().getId());
		
		assertThat(ElementsArray[0]).usingRecursiveComparison().isEqualTo(elementParent);
	}
	
	@Test
	public void testBindChildToParent() throws Exception{
		// GIVEN the database contains a elementDomain 2020b.lior.trachtman with generated id
		ElementBoundary elementParent = this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
				new ElementBoundary(new ElementId("2020b.lior.trachtman", "x"), "type", "name", true,
						new Date(System.currentTimeMillis()),
						new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
						new Location(40.730610, -73.935242), new HashMap<>()),
				ElementBoundary.class,"2020b.lior.trachtman", "don'tcare1");
		
		
		// GIVEN the database contains a elementDomain 2020b.lior.trachtman with generated id
		ElementBoundary elementChild = this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
				new ElementBoundary(new ElementId("2020b.lior.trachtman", "y"), "type", "name", true,
						new Date(System.currentTimeMillis()),
						new CreatedBy(new UserId("2020b.lior.trachtman", "test.micha@s.afeka.ac.il")),
						new Location(40.730610, 73.935242), new HashMap<>()),
				ElementBoundary.class,"2020b.lior.trachtman", "don'tcare2");
			
		this.restTemplate.put(this.url + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}/children",
				new ElementIdBoundary(elementChild.getElementId().getDomain(), elementChild.getElementId().getId()),
				elementParent.getCreatedBy().getUserId().getDomain(),elementParent.getCreatedBy().getUserId().getEmail(),
				elementParent.getElementId().getDomain(),elementParent.getElementId().getId());
		
		ElementBoundary[] ElementsArray = this.restTemplate.getForObject(this.url + "/{userDomain}/{userEmail}/{elementDomain}/{elementId}/children",
				ElementBoundary[].class, 
				elementParent.getCreatedBy().getUserId().getDomain(), elementParent.getCreatedBy().getUserId().getEmail(),
				elementParent.getElementId().getDomain(),elementParent.getElementId().getId());
		
		assertThat(ElementsArray[0]).usingRecursiveComparison().isEqualTo(elementChild);
	}
	
	@Test
	public void testPutSingleElementWithDatabaseAndUpdateTypeReturnsASpecificUpdatedElement() throws Exception {

		// GIVEN the database contains a elementDomain 2020b.lior.trachtman with
		// generated id

		ElementBoundary element = this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
				new ElementBoundary(new ElementId("2020b.lior.trachtman", "x"), "type", "name", true,
						new Date(System.currentTimeMillis()),
						new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
						new Location(40.730610, -73.935242), new HashMap<>()),
				
				ElementBoundary.class,"2020b.lior.trachtman", "don't care");
		
		ElementBoundary newElement = new ElementBoundary(new ElementId("2020b.lior.trachtman", "anyId"), "type1", "name", true,
				new Date(System.currentTimeMillis()),
				new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
				new Location(40.730610, -73.935242), new HashMap<>());

		// THEN the server returns a element boundary with elementDomain :
		// 2020b.lior.trachtman AND id: x
		this.restTemplate.put(this.url + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}", newElement, element.getCreatedBy().getUserId().getDomain(),
				element.getCreatedBy().getUserId().getEmail(),element.getElementId().getDomain(),element.getElementId().getId());
		
		assertThat(this.restTemplate.getForObject(this.url + "/{userDomain}/{userEmail}/{elementDomain}/{elementId}", ElementBoundary.class,
				element.getCreatedBy().getUserId().getDomain(),
				element.getCreatedBy().getUserId().getEmail(),element.getElementId().getDomain(),element.getElementId().getId()).getType())
		.isEqualTo(newElement.getType());
		
		}
	
	@Test
	public void testPutSingleElementWithDatabaseAndUpdateNameReturnsASpecificUpdatedElement() throws Exception {

		// GIVEN the database contains a elementDomain 2020b.lior.trachtman with
		// generated id

		ElementBoundary element = this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
				new ElementBoundary(new ElementId("2020b.lior.trachtman", "x"), "type", "name", true,
						new Date(System.currentTimeMillis()),
						new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
						new Location(40.730610, -73.935242), new HashMap<>()),
				
				ElementBoundary.class,"2020b.lior.trachtman", "don't care");
		
		ElementBoundary newElement = new ElementBoundary(new ElementId("2020b.lior.trachtman", "anyId"), "type", "anotherName", true,
				new Date(System.currentTimeMillis()),
				new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
				new Location(40.730610, -73.935242), new HashMap<>());
	
		this.restTemplate.put(this.url + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}", newElement, element.getCreatedBy().getUserId().getDomain(),
				element.getCreatedBy().getUserId().getEmail(),element.getElementId().getDomain(),element.getElementId().getId());
		
		assertThat(this.restTemplate.getForObject(this.url + "/{userDomain}/{userEmail}/{elementDomain}/{elementId}", ElementBoundary.class,
				element.getCreatedBy().getUserId().getDomain(),
				element.getCreatedBy().getUserId().getEmail(),element.getElementId().getDomain(),element.getElementId().getId()).getName())
		.isEqualTo(newElement.getName());
		
		}
	
	@Test
	public void testPutSingleElementWithDatabaseAndUpdateLocationReturnsASpecificUpdatedElement() throws Exception {

		// GIVEN the database contains a elementDomain 2020b.lior.trachtman with
		// generated id

		ElementBoundary element = this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
				new ElementBoundary(new ElementId("2020b.lior.trachtman", "x"), "type", "name", true,
						new Date(System.currentTimeMillis()),
						new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
						new Location(40.730610, -73.935242), new HashMap<>()),
				
				ElementBoundary.class,"2020b.lior.trachtman", "don't care");
		
		ElementBoundary newElement = new ElementBoundary(new ElementId("2020b.lior.trachtman", "anyId"), "type", "name", true,
				new Date(System.currentTimeMillis()),
				new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
				new Location(50.730610, -22.935242), new HashMap<>());
	
		this.restTemplate.put(this.url + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}", newElement, element.getCreatedBy().getUserId().getDomain(),
				element.getCreatedBy().getUserId().getEmail(),element.getElementId().getDomain(),element.getElementId().getId());
		
		assertThat(this.restTemplate.getForObject(this.url + "/{userDomain}/{userEmail}/{elementDomain}/{elementId}", ElementBoundary.class,
				element.getCreatedBy().getUserId().getDomain(),
				element.getCreatedBy().getUserId().getEmail(),element.getElementId().getDomain(),element.getElementId().getId()).getLocation().getLat())
		.isEqualTo(newElement.getLocation().getLat());
		
		assertThat(this.restTemplate.getForObject(this.url + "/{userDomain}/{userEmail}/{elementDomain}/{elementId}", ElementBoundary.class,
				element.getCreatedBy().getUserId().getDomain(),
				element.getCreatedBy().getUserId().getEmail(),element.getElementId().getDomain(),element.getElementId().getId()).getLocation().getLng())
		.isEqualTo(newElement.getLocation().getLng());
		
		}

	@Test
	public void testPutSingleElementWithDatabaseAndUpdateActiveReturnsASpecificUpdatedElement() throws Exception {

		// GIVEN the database contains a elementDomain 2020b.lior.trachtman with
		// generated id

		ElementBoundary element = this.restTemplate.postForObject(this.url + "/{managerDomain}/{managerEmail}",
				new ElementBoundary(new ElementId("2020b.lior.trachtman", "x"), "type", "name", true,
						new Date(System.currentTimeMillis()),
						new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
						new Location(40.730610, -73.935242), new HashMap<>()),
				
				ElementBoundary.class,"2020b.lior.trachtman", "don't care");
		
		ElementBoundary newElement = new ElementBoundary(new ElementId("2020b.lior.trachtman", "anyId"), "type", "name", false,
				new Date(System.currentTimeMillis()),
				new CreatedBy(new UserId("2020b.lior.trachtman", "sarel.micha@s.afeka.ac.il")),
				new Location(40.730610, -73.935242), new HashMap<>());
		
		this.restTemplate.put(this.url + "/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}", newElement, element.getCreatedBy().getUserId().getDomain(),
				element.getCreatedBy().getUserId().getEmail(),element.getElementId().getDomain(),element.getElementId().getId());
		
		assertThat(this.restTemplate.getForObject(this.url + "/{userDomain}/{userEmail}/{elementDomain}/{elementId}", ElementBoundary.class,
				element.getCreatedBy().getUserId().getDomain(),
				element.getCreatedBy().getUserId().getEmail(),element.getElementId().getDomain(),element.getElementId().getId()).getActive())
		.isEqualTo(newElement.getActive());
		
		}

}
