package acs.logic.mockups;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import acs.boundaries.UserBoundary;
import acs.data.UserEntity;
import acs.helpers.UserHelper;
import acs.logic.UserService;
import acs.logic.util.UserConverter;

@Service
public class UserServiceMockup implements UserService {
	private String projectName;
	private List<UserEntity> allUsers;
	private UserConverter userConverter;

	@PostConstruct
	public void init() {
		// synchronized Java collection
		this.allUsers = Collections.synchronizedList(new ArrayList<>());
	}
	
	//inject configuration value or inject default value 
	@Value("${spring.application.name:demo}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public UserBoundary createUser(UserBoundary user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserBoundary login(String userDomain, String userEmail) {
		// mockup reads data from list
		UserEntity user = allUsers.stream()
				.filter(userEntity ->userEntity.getUserId().getDomain() !=null &&
				userEntity.getUserId().getEmail() != null && userEntity.getUserId().getDomain().equals(userDomain) &&
						userEntity.getUserId().getEmail().equals(userEmail))
				.findFirst().orElseThrow(() -> new RuntimeException("could not find user"));
		
		return this.userConverter.fromEntity(user);
	}

	@Override
	public UserBoundary updateUser(String userDomain, String userEmail, UserBoundary update) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserBoundary> getAllUsers(String adminDomain, String adminEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllUser(String adminDomain, String adminEmail) {
		// TODO Auto-generated method stub

	}

}
