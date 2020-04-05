package acs.logic.mockups;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import acs.boundaries.ElementBoundary;
import acs.data.ElementEntity;
import acs.logic.ElementService;
import acs.logic.util.ElementConverter;
import acs.logic.util.UserConverter;
import acs.util.CreatedBy;
import acs.util.ElementAttributes;
import acs.util.ElementId;
import acs.util.Location;

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
	public ElementBoundary create(String managerDomain, String managerEmail, ElementBoundary elementBoundary) {
		
		if(isBoundaryContainsLegalValues(elementBoundary)) {	
			
			//Set the element's domain to the project name.
			elementBoundary.getElementId().setDomain(getProjectName());
			
			//Set the element's creation date.
			elementBoundary.setCreatedTimeStamp(new Date(System.currentTimeMillis()));
			
			//Create  the unique id for the element.
			elementBoundary.getElementId().setId(UUID.randomUUID().toString());
			
			//Set element's manager details.
			elementBoundary.getCreatedBy().getUserId().setDomain(managerDomain);
			elementBoundary.getCreatedBy().getUserId().setEmail(managerEmail);
			
			//Convert the element boundary to element entity
			ElementEntity elementEntity = elementConverter.toEntity(elementBoundary);
			
			//Add the new element entity to DB.
			allElements.add(elementEntity);
			
			return elementBoundary;
		}
		
		throw new RuntimeException("Element Boundary contains illegal values."); 
		
	}
	
	//Check with the guys, maybe we can make this method generic to all and any service can use it.
	private boolean isBoundaryContainsLegalValues(ElementBoundary element) {
		
		if(isBoundaryValuesInstantiated(element)) {
			return true;
		}
		
		return false;
		
		
	}

	private boolean isBoundaryValuesInstantiated(ElementBoundary element) {
		
		return true;
	}

	@Override
	public ElementBoundary update(String managerDomain, String managerEmail, String elementDomain, String elementId,
			ElementBoundary update) {
		
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

	public String getProjectName() {
		return projectName;
	}
	
	

}
