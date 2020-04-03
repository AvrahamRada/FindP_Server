package acs.logic.mockups;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import acs.boundaries.ElementBoundary;
import acs.data.ElementEntity;
import acs.logic.ElementService;
import acs.logic.util.ElementConverter;
import acs.logic.util.UserConverter;

public class ElementServiceMockup implements ElementService {
	private String projectName;
	private List<ElementEntity> allElements;
	private ElementConverter elementConverter;
	
	
	@Autowired
	public ElementServiceMockup(ElementConverter elementConverter) {
		super();
		this.elementConverter = elementConverter;
	}
	
	@PostConstruct
	public void init() {
		// synchronized Java collection
		this.allElements = Collections.synchronizedList(new ArrayList<>());
	}
	
	//inject configuration value or inject default value 
	@Value("${spring.application.name:demo}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public ElementBoundary create(String managerDomain, String managerEmail, ElementBoundary element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElementBoundary update(String managerDomain, String managerEmail, String elementDomain, String elementId,
			ElementBoundary update) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ElementBoundary> getAll(String userDomain, String userEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElementBoundary getSpecificElement(String userDomain, String userEmail, String elementDomain,
			String elementId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllElements(String domainDomain, String domainEmail) {
		// TODO Auto-generated method stub
		
	}

}
