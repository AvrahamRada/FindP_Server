package acs.util;

import javax.management.RuntimeErrorException;

public class UserId {

	private String domain;
	private String email;

	public UserId() {
		// TODO Auto-generated constructor stub
	}

	public UserId(String domain, String email) {
		super();
		this.domain = domain;
		this.email = email;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		if (domain != null) {
			this.domain = domain;
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if(email != null) {
			this.email = email;
		}
	}

	public void validation() {

		if (this.domain == null) {
			throw new RuntimeException("UserId domain was not instantiate");
		}
		
		if (this.email == null) {
			throw new RuntimeException("UserId email was not instantiate");
		}
	}

}
