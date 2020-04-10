package acs.element;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ElementPOSTTests {
	

	private int port;
	private String url;
	private RestTemplate restTemplate;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.url = "http://localhost:" + port + "/elements";
		this.restTemplate = new RestTemplate();
	}
	
	@BeforeEach
	public void setup() {
		this.restTemplate
			.delete(this.url);
	}
	
	@AfterEach
	public void teardown() {
		this.restTemplate
			.delete(this.url);
	}

}
