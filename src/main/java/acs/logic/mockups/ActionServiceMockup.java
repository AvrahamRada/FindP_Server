package acs.logic.mockups;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import acs.action.ActionAttributes;
import acs.action.ActionId;
import acs.action.InvokedBy;
import acs.boundaries.ActionBoundary;
import acs.data.ActionEntity;
import acs.logic.ActionService;
import acs.logic.util.ActionConverter;
import acs.logic.util.ElementConverter;
import acs.util.Element;

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
		if (action.getActionId() == null) {
			ActionEntity entity = this.actionConverter.toEntity(action);
			this.allActions.add(entity);
			System.out.println("here");
			return new ActionEntity();
		}
		throw new RuntimeException("actionId must be null");
	}

	@Override
	public List<ActionBoundary> getAllActions(String adminDomain, String adminEmail) {
		// TODO Find if user is admin
		return this.allActions.stream().map(this.actionConverter::fromEntity).collect(Collectors.toList());
	}

	@Override
	public void deleteAllActions(String adminDomain, String adminEmail) {
		// TODO Find if user is admin
		this.allActions.clear();
	}

}

