package acs.logic.util;

import org.springframework.stereotype.Component;

import acs.boundaries.UserBoundary;
import acs.data.UserEntity;
@Component
public class UserConverter {
	
	public UserBoundary fromEntity(UserEntity entity) {
		UserBoundary rv = new UserBoundary();
		rv.setUserId(entity.getUserId());
		rv.setAvatar(entity.getAvatar());
		rv.setUserName(entity.getUserName());
		rv.setRole(entity.getRole().name());
		return rv;
	}

	public UserEntity toEntity(UserBoundary boundary) {
		UserEntity rv = new UserEntity();
		rv.setUserId(boundary.getUserId());
		rv.setAvatar(boundary.getAvatar());
		rv.setUserName(boundary.getUserName());
		rv.setRole(boundary.getRole());
		return rv;
	}

}
