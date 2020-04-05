package acs.data;

import acs.util.UserId;

public class UserEntity {
	
	private UserId userId;
	private UserRole role;
	private String userName;
	private String avatar;
	
	public UserEntity() {
		// TODO Auto-generated constructor stub
	}

	public UserEntity(UserId userId, String role, String userName, String avatar) {
		super();
		this.userId = userId;
		this.role = UserRole.valueOf(role);
		this.userName = userName;
		this.avatar = avatar;
	}

	public UserId getUserId() {
		return userId;
	}

	public void setUserId(UserId userId) {
		this.userId = userId;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = UserRole.valueOf(role);
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
