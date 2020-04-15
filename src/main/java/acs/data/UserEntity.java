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
		if (userId != null) {
			this.userId.setDomain(userId.getDomain());
			this.userId.setEmail(userId.getEmail());
		}
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		if (role != null) {
			this.role = role;
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		if (username != null) {
			this.username = username;
		}
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		if (avatar != null) {
			this.avatar = avatar;
		}
	}
}
