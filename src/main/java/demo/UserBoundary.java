package demo;

public class UserBoundary {
	private String email;
	private String userName;
	private String userDomain;
	
	public UserBoundary() {
		
	}
		
	
	public UserBoundary(String email, String userName, String userDomain) {
		super();
		this.email = email;
		this.userName = userName;
		this.userDomain = userDomain;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
	public String getUserDomain() {
		return this.userDomain;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void setUserDomain(String userDomain) {
		this.userDomain = userDomain;
	}
	
}
