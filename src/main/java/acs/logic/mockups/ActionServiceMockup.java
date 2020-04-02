package acs.logic.mockups;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;

import acs.boundaries.ActionBoundary;
import acs.data.ActionEntity;
import acs.logic.ActionService;
import acs.logic.util.ActionConverter;

public class ActionServiceMockup implements ActionService {
	private String projectName;
	private List<ActionEntity> allActions;
	private ActionConverter actionConverter;
	
	@PostConstruct
	public void init() {
		// synchronized Java collection
		this.allActions = Collections.synchronizedList(new ArrayList<>());
	}
	
	//inject configuration value or inject default value 
	@Value("${spring.application.name:demo}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public Object invokeAction(ActionBoundary action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ActionBoundary> getAllActions(String adminDomain, String adminEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllActions(String adminDomain, String adminEmail) {
		// TODO Auto-generated method stub
		
	}

}
