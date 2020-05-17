package acs.logic;

import java.util.List;

import acs.boundaries.UserBoundary;

public interface EnhancedUserService extends UserService{

	List<UserBoundary> getAllUsers(String adminDomain, String adminEmail, int size, int page);

}
