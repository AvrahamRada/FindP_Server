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
import acs.data.UserRole;
import acs.logic.UserService;
import acs.logic.util.UserConverter;

@Service
public class UserServiceMockup implements UserService {
	private String projectName;
	private List<UserEntity> allUsers;
	private List<UserEntity> loginUsers;
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
		this.loginUsers = Collections.synchronizedList(new ArrayList<>());
	}

	// inject configuration value or inject default value
	@Value("${spring.application.name:demo}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public UserBoundary createUser(UserBoundary user) {
		//check if exists before we are adding new user to list????????
		
		user.validation();
		user.getUserId().setDomain(projectName);
		try {
			findUser(user.getUserId().getDomain(), user.getUserId().getEmail()); //if it wont find it will throw an exception
		}catch(RuntimeException re) {
			UserEntity newUser = userConverter.toEntity(user);
			this.allUsers.add(newUser);
			return user;
		}
		throw new RuntimeException("user is already exists in the system");
	}

	@Override
	public UserBoundary login(String userDomain, String userEmail) {
		// mockup reads data from list
		UserEntity user = findUser(userDomain, userEmail);
		this.loginUsers.add(user);
		return this.userConverter.fromEntity(user);
	}

	@Override
	public UserBoundary updateUser(String userDomain, String userEmail, UserBoundary update) {
		UserEntity updateUser = findUser(userDomain, userEmail);
		
		//---Inside the setters there are null checks---
		updateUser.setAvatar(update.getAvatar());
		updateUser.setRole(update.getRole());
		updateUser.setUsername(update.getUsername());
		
		return this.userConverter.fromEntity(updateUser);
	}

	@Override
	public List<UserBoundary> getAllUsers(String adminDomain, String adminEmail) {
		checkAdmin(adminDomain, adminEmail);
		return this.allUsers.stream().map(this.userConverter::fromEntity).collect(Collectors.toList());
		
	}

	@Override
	public void deleteAllUsers(String adminDomain, String adminEmail) {
		checkAdmin(adminDomain, adminEmail);
		this.allUsers.clear();
		this.loginUsers.clear();
		
	}

	// check if user exist in the system
	public UserEntity findUser(String userDomain, String userEmail) {
		return this.allUsers.stream()
				.filter(userEntity -> userEntity.getUserId().getEmail().equals(userEmail)
						&& userEntity.getUserId().getDomain().equals(userDomain))
				.findFirst().orElseThrow(() -> new RuntimeException("Could not find user"));
		
	}

	// check if user is admin and login
	public void checkAdmin(String adminDomain, String adminEmail) {
		this.loginUsers.stream()
				.filter(userEntity -> userEntity.getUserId().getEmail().equals(adminEmail)
						&& userEntity.getUserId().getDomain().equals(adminDomain)
						&& userEntity.getRole() == UserRole.ADMIN)
				.findFirst().orElseThrow(() -> new RuntimeException("User is not admin"));
	}
}
