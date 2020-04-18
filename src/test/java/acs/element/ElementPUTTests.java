package acs.element;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import acs.boundaries.UserBoundary;
import acs.data.UserRole;
import acs.util.NewUserDetails;
import acs.util.TestUtil;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ElementPUTTests {
	

	private int port;
	private String url;

	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.url = "http://localhost:" + port + "/acs/elements";		
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

}
