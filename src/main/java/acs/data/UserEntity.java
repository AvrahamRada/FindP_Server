package acs.data;

import acs.element.UserId;

public class UserEntity {
	
	private UserId userId;
	private String role;
	private String userName;
	private String avatar;
	
	public UserEntity() {
		// TODO Auto-generated constructor stub
	}

	public UserEntity(UserId userId, String role, String userName, String avatar) {
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
