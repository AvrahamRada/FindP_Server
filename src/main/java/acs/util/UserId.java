package acs.util;

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
		this.domain = domain;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public void validation(){
		if(email == null)
			throw new RuntimeException("email was not instantiate");
		if(domain == null)
			throw new RuntimeException("domain was not instantiate");
	}
	
}
