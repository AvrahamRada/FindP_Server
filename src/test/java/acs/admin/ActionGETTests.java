package acs.admin;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import acs.util.TestUtil;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ActionGETTests {
	

	private int port;
	private String url;
	private RestTemplate restTemplate;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.url = "http://localhost:" + port + "/acs/admin";
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

}
