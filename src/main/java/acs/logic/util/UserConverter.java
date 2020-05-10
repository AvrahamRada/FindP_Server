package acs.logic.util;

import org.springframework.stereotype.Component;

import acs.boundaries.UserBoundary;
import acs.data.UserEntity;
import acs.util.UserId;
@Component
public class UserConverter extends Converter {
	
	public UserBoundary fromEntity(UserEntity entity) {
		UserBoundary rv = new UserBoundary();
		rv.setUserId(convertToUserId(entity.getUserId()));
		rv.setAvatar(entity.getAvatar());
		rv.setUsername(entity.getUsername());
		rv.setRole(entity.getRole());
		return rv;
	}

	public UserEntity toEntity(UserBoundary boundary) {
		UserEntity rv = new UserEntity();
		rv.setUserId(convertToEntityId(boundary.getUserId().getDomain(), boundary.getUserId().getEmail()));
		rv.setAvatar(boundary.getAvatar());
		rv.setUsername(boundary.getUsername());
		rv.setRole(boundary.getRole());
		return rv;
	}
	
	
}
