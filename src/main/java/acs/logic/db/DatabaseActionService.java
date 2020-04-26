package acs.logic.db;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import acs.action.ActionId;
import acs.boundaries.ActionBoundary;
import acs.dal.ActionDao;
import acs.logic.ActionService;
import acs.logic.util.ActionConverter;

@Service
public class DatabaseActionService implements ActionService {
	private String projectName;
	private ActionConverter actionConverter;
	private ActionDao actionDao;

	@Autowired
	public DatabaseActionService(ActionConverter actionConverter,ActionDao actionDao) {
		super();
		this.actionConverter = actionConverter;
		this.actionDao = actionDao;
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
		
		action.validation(); // if one of the important value is null, it will throw an exception
		action.setActionId(new ActionId(this.projectName, UUID.randomUUID().toString()));
		action.setCreatedTimestamp(new Date(System.currentTimeMillis()));
		actionDao.save(this.actionConverter.toEntity(action));
		return action;
	}

	@Override
	@Transactional (readOnly = true)
	public List<ActionBoundary> getAllActions(String adminDomain, String adminEmail) {
		// TODO Find if user is Admin- Eyal told us to not check it in this sprint (sprint 3)
		
		return StreamSupport.stream(this.actionDao.findAll().spliterator(), false) // Stream<ElementEntity>
				.map(this.actionConverter::fromEntity) // Stream<ElementBoundary>
				.collect(Collectors.toList());		
	}

	@Override
	@Transactional //(readOnly = false)
	public void deleteAllActions(String adminDomain, String adminEmail) {
		// TODO Find if user is Admin- Eyal told us to not check it in this sprint (sprint 3)
		actionDao.deleteAll();
	}

}
