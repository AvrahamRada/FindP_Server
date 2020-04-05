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
	
	//inject configuration value or inject default value 
	@Value("${spring.application.name:demo}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public UserBoundary createUser(UserBoundary user) {
		user.validation();
		user.getUserId().setDomain(projectName);
		UserEntity newUser = userConverter.toEntity(user);
		this.allUsers.add(newUser);
		return user;
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
		update.validation();
		update.getUserId().validationDomain(projectName);
		UserEntity updateUser = findUser(userDomain, userEmail);
		Collections.replaceAll(allUsers, updateUser, userConverter.toEntity(update));
		return update;
	}

	@Override
	public List<UserBoundary> getAllUsers(String adminDomain, String adminEmail) {
		if(!findAdmin(adminDomain, adminEmail))
			throw new RuntimeException("User is not admin");
		else {
			return this.allUsers
					.stream()
					.map(this.userConverter::fromEntity)
					.collect(Collectors.toList());
		}	
	}

	@Override
	public void deleteAllUsers(String adminDomain, String adminEmail) {
		if(!findAdmin(adminDomain, adminEmail))
			throw new RuntimeException("User is not admin");
		else {
			this.allUsers.clear();
			this.loginUsers.clear();
		}
	}
	
	//check if user exist in the system
	public UserEntity findUser(String userDomain, String userEmail) {
		UserEntity user = this.allUsers.stream().
				filter(userEntity -> userEntity.getUserId().getEmail().equals(userEmail) 
						&& userEntity.getUserId().getDomain().equals(userDomain)).
				findFirst().orElseThrow(() -> new RuntimeException("could not find user"));
		return user;
	}
	
	//check if user is admin and login
	public boolean findAdmin(String adminDomain, String adminEmail) {
		try {
			this.loginUsers.stream().
			filter(userEntity -> userEntity.getUserId().getEmail().equals(adminEmail) 
				&& userEntity.getUserId().getDomain().equals(adminDomain) 
				&& userEntity.getRole().name().equals("ADMIN")).
				findFirst().orElseThrow(()-> new RuntimeException("user is not admin"));
			return true;
		}catch (RuntimeException e) {
			return false;
		}
	}
}
