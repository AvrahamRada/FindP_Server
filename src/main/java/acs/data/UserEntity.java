package acs.data;

import acs.util.UserId;

public class UserEntity {
	
	private UserId userId;
	private UserRole role;
	private String username;
	private String avatar;
	
	public UserEntity() {
		// TODO Auto-generated constructor stub
	}

	public UserEntity(UserId userId, String role, String username, String avatar) {
		super();
		this.userId = userId;
		this.role = UserRole.valueOf(role);
		this.username = username;
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

	public void setRole(UserRole role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
}
