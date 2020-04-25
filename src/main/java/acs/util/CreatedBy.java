package acs.util;

import javax.persistence.Embeddable;

public class CreatedBy {

	private UserId userId;

	public CreatedBy() {
		// TODO Auto-generated constructor stub
	}

	public CreatedBy(UserId userId) {
		super();
		this.userId = userId;
	}

	public UserId getUserId() {
		return userId;
	}

	public void setUserId(UserId userId) {
		this.userId = userId;
	}

	public void validation() {

		if (userId == null) {
			throw new RuntimeException("userId was not instantiate");
		}
		userId.validation();

	}
}
