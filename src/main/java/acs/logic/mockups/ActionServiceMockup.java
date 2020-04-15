package acs.logic.mockups;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import acs.action.ActionId;
import acs.boundaries.ActionBoundary;
import acs.data.ActionEntity;
import acs.logic.ActionService;
import acs.logic.util.ActionConverter;

@Service
public class ActionServiceMockup implements ActionService {
	private String projectName;
	private List<ActionEntity> allActions;
	private ActionConverter actionConverter;

	@Autowired
	public ActionServiceMockup(ActionConverter actionConverter) {
		super();
		this.actionConverter = actionConverter;
	}

	@PostConstruct
	public void init() {
		// synchronized Java collection
		this.allActions = Collections.synchronizedList(new ArrayList<>());
	}

	// inject configuration value or inject default value
	@Value("${spring.application.name:demo}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public Object invokeAction(ActionBoundary action) {
		action.validation(); // if one of the important value is null, it will throw an exception
		action.setActionId(new ActionId(this.projectName, UUID.randomUUID().toString()));
		action.setCreatedTimestamp(new Date());
		this.allActions.add(this.actionConverter.toEntity(action));
		return action;
	}

	@Override
	public List<ActionBoundary> getAllActions(String adminDomain, String adminEmail) {
		// TODO Find if user is Admin- Eyal told us to not check it in this sprint (sprint 3)
		return this.allActions.stream().map(this.actionConverter::fromEntity).collect(Collectors.toList());
	}

	@Override
	public void deleteAllActions(String adminDomain, String adminEmail) {
		// TODO Find if user is Admin- Eyal told us to not check it in this sprint (sprint 3)
		this.allActions.clear();
	}

}
