package acs.data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="USERS")
public class UserEntity {//USERS

	private String userId; // USER_ID PK VARCHAR(255)
	private UserRole role; // ROLE VARCHAR(255) 
	private String username; // USERNAME VARCHAR(255)
	private String avatar; //AVATAR VARCHAR(255)

	public UserEntity() {
//		this.userId = new UserId();
	}

	public UserEntity(String userId, String role, String username, String avatar) {
		super();
		this.userId = userId;
		this.role = UserRole.valueOf(role);
		this.username = username;
		this.avatar = avatar;
	}

	@Id
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Enumerated(EnumType.STRING)
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
		if (avatar != null && avatar != "") {
			this.avatar = avatar;
		}
	}
}
