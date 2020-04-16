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

import acs.boundaries.ElementBoundary;
import acs.data.ElementEntity;
import acs.logic.ElementService;
import acs.logic.util.ElementConverter;
import acs.util.CreatedBy;
import acs.util.ElementId;
import acs.util.UserId;

@Service
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

	// inject configuration value or inject default value
	@Value("${spring.application.name:demo}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectName() {
		return projectName;
	}

	@Override
	public ElementBoundary create(String managerDomain, String managerEmail, ElementBoundary elementBoundary) {
		
		if(!managerDomain.equals(getProjectName())) {
			throw new RuntimeException("Invalid manager domain");
		}
		
		//In the future manager email has to be be checked too.

		// Validate that the important element boundary fields are not null;
		elementBoundary.validation();

		// Set the element's domain to the project name and create the unique id for the element.
		elementBoundary.setElementId(new ElementId(getProjectName(), UUID.randomUUID().toString()));
		
		// Set the element's creation date.
		elementBoundary.setCreatedTimestamp(new Date(System.currentTimeMillis()));

		// Set element's manager details.
		elementBoundary.setCreatedBy(new CreatedBy(new UserId(managerDomain, managerEmail)));

		// Convert the element boundary to element entity
		ElementEntity elementEntity = elementConverter.toEntity(elementBoundary);

		// Add the new element entity to DB.
		allElements.add(elementEntity);

		return elementBoundary;

	}

	@Override
	public ElementBoundary update(String managerDomain, String managerEmail, String elementDomain, String elementId,
			ElementBoundary update) {
		
		if(!managerDomain.equals(getProjectName())){
			throw new RuntimeException("Invalid manager domain");
		}
		
		if(!elementDomain.equals(getProjectName())) {
			throw new RuntimeException("Invalid element domain");
		}

		// Fetching the specific element from DB.
		ElementEntity foundedElement = findElement(elementId);

		// Convert the input to entity before update the values in element entity that
		// is in the DB.
		ElementEntity updateEntity = elementConverter.toEntity(update);

		// Update the element's values.
		updateElementValues(foundedElement, updateEntity);

		// Convert the update entity to boundary and returns it.
		return elementConverter.fromEntity(foundedElement);

	}

	@Override
	public List<ElementBoundary> getAll(String userDomain, String userEmail) {
		
		if(!userDomain.equals(getProjectName())) {
			throw new RuntimeException("Invalid user domain");
		}

		return this.allElements.stream().map(this.elementConverter::fromEntity).collect(Collectors.toList());

	}

	@Override
	public ElementBoundary getSpecificElement(String userDomain, String userEmail, String elementDomain,
			String elementId) {
		
		if(!userDomain.equals(getProjectName())) {
			throw new RuntimeException("Invalid admin domain");
		}
		
		if(!elementDomain.equals(getProjectName())) {
			throw new RuntimeException("Invalid element domain");
		}

		// Fetching the specific element from DB.
		ElementEntity foundedElement = findElement(elementId);

		return elementConverter.fromEntity(foundedElement);

	}

	@Override
	public void deleteAllElements(String adminDomain, String adminEmail) {
		
		if(!adminDomain.equals(getProjectName())) {
			throw new RuntimeException("Invalid admin domain");
		}

		// Clear all elements from DB.
		allElements.clear();

	}

	private ElementEntity findElement(String elementId) {

		ElementEntity foundedElement = allElements.stream()
				.filter(elementEntity -> elementEntity.getElementId().getId().equals(elementId)).findFirst()
				.orElseThrow(() -> new RuntimeException("could not find element"));
		return foundedElement;

	}

	private void updateElementValues(ElementEntity toBeUpdatedEntity, ElementEntity inputEntity) {

		// Copy the important values from update entity to toBeUpdateEntity only if they are not null
		toBeUpdatedEntity.setActive(inputEntity.getActive());
		toBeUpdatedEntity.setElementAttributes(inputEntity.getElementAttributes());
		toBeUpdatedEntity.setLocation(inputEntity.getLocation());
		toBeUpdatedEntity.setName(inputEntity.getName());
		toBeUpdatedEntity.setType(inputEntity.getType());

	}

}
