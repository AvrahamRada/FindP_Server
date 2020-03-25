package demo.boundaries;

import demo.element.UserId;

public class UserBoundary {
	
	private UserId userId;
	private String role;
	private String userName;
	private String avatar;
	
	public UserBoundary() {
		// TODO Auto-generated constructor stub
	}

	public UserBoundary(UserId userId, String role, String userName, String avatar) {
		super();
		this.userId = userId;
		this.role = role;
		this.userName = userName;
		this.avatar = avatar;
	}

	public UserId getUserId() {
		return userId;
	}

	public void setUserId(UserId userId) {
		this.userId = userId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	

}
