package acs.logic.db;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import acs.action.ActionId;
import acs.boundaries.ActionBoundary;
import acs.boundaries.UserBoundary;
import acs.dal.ActionDao;
import acs.dal.ElementDao;
import acs.dal.UserDao;
import acs.data.ActionEntity;
import acs.data.ElementEntity;
import acs.data.UserRole;
import acs.logic.ActionService;
import acs.logic.ElementNotFoundException;
import acs.logic.EnhancedActionService;
import acs.logic.util.ActionConverter;
import acs.logic.util.ElementConverter;
import acs.logic.util.UserConverter;

@Service
public class DatabaseActionService implements EnhancedActionService {

	// finals
	private final String PARK = "park";
	private final String UNPARK = "unpark";
	private final String IS_TAKEN = "isTaken";

	private String projectName;
	private ActionConverter actionConverter;
	private ActionDao actionDao;
	private UserConverter userConverter;
	private UserDao userDao;
	private ElementDao elementDao;
	private ElementConverter elementConverter;

	@Autowired
	public DatabaseActionService(ActionConverter actionConverter, ActionDao actionDao, UserConverter userConverter,
			UserDao userDao, ElementDao elementDao, ElementConverter elementConverter) {
		super();
		this.actionConverter = actionConverter;
		this.actionDao = actionDao;
		this.userConverter = userConverter;
		this.userDao = userDao;
		this.elementDao = elementDao;
		this.elementConverter = elementConverter;
	}

	@PostConstruct
	public void init() {

	}

	// inject configuration value or inject default value
	@Value("${spring.application.name:demo}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	@Transactional // (readOnly = false)
	public Object invokeAction(ActionBoundary action) {
		
		System.out.println("im here 0");

		DatabaseUserService.checkRole(action.getInvokedBy().getUserId().getDomain(),
				action.getInvokedBy().getUserId().getEmail(), UserRole.PLAYER, userDao, userConverter);
		
		System.out.println("im here 1");
		
		System.out.println("im here 3");


		action.validation(); // if one of the important value is null, it will throw an exception
		
		System.out.println("im here 4");

		action.setActionId(new ActionId(this.projectName, UUID.randomUUID().toString()));
		
		System.out.println("im here 5");

		action.setCreatedTimestamp(new Date(System.currentTimeMillis()));
		
		System.out.println("im here 6");


		operateAction(action);
		
		System.out.println("im here 7");


		// Save action to DB
		actionDao.save(this.actionConverter.toEntity(action));
		return action;
	}

	private void operateAction(ActionBoundary action) {

		ElementEntity element = null;
		
		switch (action.getType()) {

		case PARK:
			
			System.out.println("im here 2");
			element = DatabaseElementService.findActiveElement(elementDao, this.elementConverter.convertToEntityId(
					action.getElement().getElementId().getDomain(), action.getElement().getElementId().getId()));
			
			System.out.println("im here 8");

			// update element attribute of isTaken to TRUE
			element.getElementAttributes().put(IS_TAKEN, true);
			
			System.out.println("im here 9");

			// update the element in the DB.
			elementDao.save(element);
			
			System.out.println("im here 10");

			break;

		case UNPARK:
			
			System.out.println("im here 11");

			// Hopefully this query works
			List<ActionEntity> actions = actionDao.findOneByInvokedBy(
					actionConverter.convertToEntityId(action.getInvokedBy().getUserId().getDomain(),
							action.getInvokedBy().getUserId().getEmail()),
					PageRequest.of(0, 1, Direction.DESC, "createdTimestamp"));
			
			System.out.println("im here 12");

			
			if (actions.size() == 0) {
				
				System.out.println("im here 13");
				throw new RuntimeException("Player did not parked yet.");
			}

			
			System.out.println("im here 14");

			String elementId = actions.get(0).getElement();
			element = elementDao.findById(elementId)
					.orElseThrow(() -> new ElementNotFoundException("could not find element"));

			
			System.out.println("im here 15");

			// update element attribute of isTaken to TRUE
			element.getElementAttributes().put(IS_TAKEN, false);
			
			System.out.println("im here 16");

			// update the element in the DB.
			elementDao.save(element);

			break;

		default:
			System.out.println("im here 17");

			throw new RuntimeException("Type of action is not valid");

		}

	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionBoundary> getAllActions(String adminDomain, String adminEmail) {

		DatabaseUserService.checkRole(adminDomain, adminEmail, UserRole.ADMIN, userDao, userConverter);

		return StreamSupport.stream(this.actionDao.findAll().spliterator(), false) // Stream<ElementEntity>
				.map(this.actionConverter::fromEntity) // Stream<ElementBoundary>
				.collect(Collectors.toList());
	}

	@Override
	@Transactional // (readOnly = false)
	public void deleteAllActions(String adminDomain, String adminEmail) {
		DatabaseUserService.checkRole(adminDomain, adminEmail, UserRole.ADMIN, userDao, userConverter);
		actionDao.deleteAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionBoundary> getAllActions(String adminDomain, String adminEmail, int size, int page) {
		DatabaseUserService.checkRole(adminDomain, adminEmail, UserRole.ADMIN, this.userDao, this.userConverter);
		return this.actionDao.findAll(PageRequest.of(page, size, Direction.ASC, "actionId")).getContent().stream()
				.map(this.actionConverter::fromEntity).collect(Collectors.toList());
	}

}
