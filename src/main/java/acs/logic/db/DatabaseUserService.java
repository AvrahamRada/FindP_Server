package acs.logic.db;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import acs.boundaries.UserBoundary;
import acs.dal.UserDao;
import acs.data.UserEntity;
import acs.data.UserRole;
import acs.logic.UserService;
import acs.logic.util.UserConverter;

@Service
public class DatabaseUserService implements UserService {
	private String projectName;
//	private List<UserEntity> allUsers;
	private UserConverter userConverter;
	private UserDao userDao;

	@Autowired
	public DatabaseUserService(UserConverter userConverter, UserDao userDao) {
		super();
		this.userDao = userDao;
		this.userConverter = userConverter;
	}

	@PostConstruct
	public void init() {
		// synchronized Java collection
//		this.allUsers = Collections.synchronizedList(new ArrayList<>());
	}

	// inject configuration value or inject default value
	@Value("${spring.application.name:demo}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	@Transactional // (readOnly = false)
	public UserBoundary createUser(UserBoundary user) {

		user.validation();
		user.getUserId().setDomain(projectName);

		try {
			this.getUserEntityFromDatabase(
			userConverter.concatDomainEmail(user.getUserId().getDomain(), user.getUserId().getEmail()));
			// findUser(user.getUserId().getDomain(), user.getUserId().getEmail());
		} catch (RuntimeException re) {
			UserEntity newUser = userConverter.toEntity(user);
			this.userDao.save(newUser);
			return user;
		}
		throw new RuntimeException("user is already exists in the system");
	}

	@Override
	public UserBoundary login(String userDomain, String userEmail) {
		// mockup reads data from list
		UserEntity user = this.getUserEntityFromDatabase(userConverter.concatDomainEmail(userDomain, userEmail));
		return this.userConverter.fromEntity(user);
	}

	@Override
	public UserBoundary updateUser(String userDomain, String userEmail, UserBoundary update) {
		UserEntity updateUser = this.getUserEntityFromDatabase(userConverter.concatDomainEmail(userDomain, userEmail));
		// ---Inside the setters there are null checks---
		updateUser.setAvatar(update.getAvatar());
		updateUser.setRole(update.getRole());
		updateUser.setUsername(update.getUsername());
		this.userDao.save(updateUser);
		return this.userConverter.fromEntity(updateUser);
	}

	@Override
	public List<UserBoundary> getAllUsers(String adminDomain, String adminEmail) {
		checkAdmin(adminDomain, adminEmail);

		return StreamSupport.stream(this.userDao.findAll().spliterator(), false) // Stream<UserEntity>
				.map(this.userConverter::fromEntity) // Stream<UserBoundary>
				.collect(Collectors.toList()); // List<UserBoundary>
	}

	@Override
	public void deleteAllUsers(String adminDomain, String adminEmail) {
		checkAdmin(adminDomain, adminEmail);
		this.userDao.deleteAll();
	}

	// check if user exist in the system
//	public UserEntity findUser(String userDomain, String userEmail) {
//		return this.allUsers.stream()
//				.filter(userEntity -> userEntity.getUserId().getEmail().equals(userEmail)
//						&& userEntity.getUserId().getDomain().equals(userDomain))
//				.findFirst().orElseThrow(() -> new RuntimeException("Could not find user"));
//
//	}

	// check if user is admin and exists
	public void checkAdmin(String adminDomain, String adminEmail) {
		UserEntity userEntity = getUserEntityFromDatabase(userConverter.concatDomainEmail(adminDomain, adminEmail));
		if (userEntity.getRole() != UserRole.ADMIN) {
			throw new RuntimeException("User is not admin");
		}
	}

	private UserEntity getUserEntityFromDatabase(String userId) {
		return this.userDao.findById(userId).orElseThrow(() -> new RuntimeException("could not find user by userId"));
	}

}
