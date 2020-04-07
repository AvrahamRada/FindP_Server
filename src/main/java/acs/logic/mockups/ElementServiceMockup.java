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

		// Validate the element boundary fields;
		elementBoundary.validation();

		// Set the element's domain to the project name.
		elementBoundary.getElementId().setDomain(getProjectName());

		// Set the element's creation date.
		elementBoundary.setCreatedTimeStamp(new Date(System.currentTimeMillis()));

		// Create the unique id for the element.
		elementBoundary.getElementId().setId(UUID.randomUUID().toString());

		// Set element's manager details.
		elementBoundary.getCreatedBy().getUserId().setDomain(managerDomain);
		elementBoundary.getCreatedBy().getUserId().setEmail(managerEmail);

		// Convert the element boundary to element entity
		ElementEntity elementEntity = elementConverter.toEntity(elementBoundary);

		// Add the new element entity to DB.
		allElements.add(elementEntity);

		return elementBoundary;

	}

	@Override
	public ElementBoundary update(String managerDomain, String managerEmail, String elementDomain, String elementId,
			ElementBoundary update) {

		// Validate the element boundary's fields
		update.validation();

		// Fetching the specific element from DB.
		ElementEntity foundedElement = searchElement(elementId);

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

		return this.allElements.stream().map(this.elementConverter::fromEntity).collect(Collectors.toList());

	}

	@Override
	public ElementBoundary getSpecificElement(String userDomain, String userEmail, String elementDomain,
			String elementId) {

		// Fetching the specific element from DB.
		ElementEntity foundedElement = searchElement(elementId);

		return elementConverter.fromEntity(foundedElement);

	}

	@Override
	public void deleteAllElements(String adminDomain, String adminEmail) {

		// Clear all elements from DB.
		allElements.clear();

	}

	private ElementEntity searchElement(String elementId) {

		ElementEntity foundedElement = allElements.stream()
				.filter(elementEntity -> elementEntity.getElementId().getId().equals(elementId)).findFirst()
				.orElseThrow(() -> new RuntimeException("could not find element"));
		return foundedElement;

	}

	private void updateElementValues(ElementEntity toBeUpdatedEntity, ElementEntity updatedEntity) {

		// Copy the RELEVANT values from update entity to toBeUpdateEntity.
		toBeUpdatedEntity.setActive(updatedEntity.getActive());
		toBeUpdatedEntity.setElementAttributes(updatedEntity.getElementAttributes());
		toBeUpdatedEntity.setLocation(updatedEntity.getLocation());
		toBeUpdatedEntity.setName(updatedEntity.getName());
		toBeUpdatedEntity.setType(updatedEntity.getType());

	}

}
