package acs.logic.mockups;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import acs.boundaries.UserBoundary;
import acs.data.UserEntity;
import acs.logic.UserService;
import acs.logic.util.UserConverter;

@Service
public class UserServiceMockup implements UserService {
	private String projectName;
	private List<UserEntity> allUsers;
	private UserConverter userConverter;
	
	
	@Autowired
	public UserServiceMockup(UserConverter userConverter) {
		super();
		this.userConverter = userConverter;
	}

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
		user.getUserId().setDomain(projectName);
		UserEntity newUser = userConverter.toEntity(user);
		allUsers.add(newUser);
		return user;
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
		UserEntity updateUser = allUsers.stream().
				filter(userEntity -> userEntity.getUserId().getEmail().equals(userEmail) 
						&& userEntity.getUserId().getDomain().equals(userDomain)).
				findFirst().orElseThrow(() -> new RuntimeException("could not find user"));
		Collections.replaceAll(allUsers, updateUser, userConverter.toEntity(update));
		return update;
	}

	@Override
	public List<UserBoundary> getAllUsers(String adminDomain, String adminEmail) {
		//check if admin is logged in
		//...
		return this.allUsers
				.stream()
				.map(this.userConverter::fromEntity)
				.collect(Collectors.toList());
	}

	@Override
	public void deleteAllUsers(String adminDomain, String adminEmail) {
		// TODO Auto-generated method stub
		//check if admin is logged in
		//...
		this.allUsers.clear();
	}

}
