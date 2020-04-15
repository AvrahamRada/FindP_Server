package acs.action;

import acs.util.UserId;

public class InvokedBy {
	
	private UserId userId;
	
	public InvokedBy() {
		// TODO Auto-generated constructor stub
	}

	public InvokedBy(UserId userId) {
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
		if(this.userId == null) {
			throw new RuntimeException("userId was not instantiate");
		}
		this.userId.validation();
	}
	
	
}
