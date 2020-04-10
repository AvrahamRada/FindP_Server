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
		rv.setUsername(entity.getUsername());
		rv.setRole(entity.getRole());
		return rv;
	}

	public UserEntity toEntity(UserBoundary boundary) {
		UserEntity rv = new UserEntity();
		rv.setUserId(boundary.getUserId());
		rv.setAvatar(boundary.getAvatar());
		rv.setUsername(boundary.getUsername());
		rv.setRole(boundary.getRole());
		return rv;
	}

}
