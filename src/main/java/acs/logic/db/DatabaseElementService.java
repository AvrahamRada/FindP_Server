package acs.logic.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import acs.boundaries.ElementBoundary;
import acs.dal.ElementDao;
import acs.data.ElementEntity;
import acs.data.UserEntity;
import acs.logic.ElementService;
import acs.logic.util.ElementConverter;
import acs.util.CreatedBy;
import acs.util.ElementId;
import acs.util.UserId;

@Service
public class DatabaseElementService implements ElementService {
	private String projectName;
//	private List<ElementEntity> allElements;
	private ElementConverter elementConverter;
	private ElementDao elementDao;

	@Autowired
	public DatabaseElementService(ElementConverter elementConverter, ElementDao elementDao) {
		super();
		this.elementConverter = elementConverter;
		this.elementDao = elementDao;
	}

	@PostConstruct
	public void init() {
		// synchronized Java collection
//		this.allElements = Collections.synchronizedList(new ArrayList<>());
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

		// Validate that the important element boundary fields are not null;
		// TODO Check in the future if the user is exists and manager
		elementBoundary.validation();

		// Set the element's domain to the project name and create the unique id for the
		// element.
		elementBoundary.setElementId(new ElementId(getProjectName(), UUID.randomUUID().toString()));

		// Set the element's creation date.
		elementBoundary.setCreatedTimestamp(new Date(System.currentTimeMillis()));

		// Set element's manager details.
		elementBoundary.setCreatedBy(new CreatedBy(new UserId(managerDomain, managerEmail)));

		// Convert the element boundary to element entity
		ElementEntity elementEntity = elementConverter.toEntity(elementBoundary);

		// Add the new element entity to DB.
//		allElements.add(elementEntity);

		return elementBoundary;

	}

	@Override
	public ElementBoundary update(String managerDomain, String managerEmail, String elementDomain, String elementId,
			ElementBoundary update) {

		// Fetching the specific element from DB.
		ElementEntity foundedElement = findElement(this.elementConverter.
				concatElementDomainElementId(elementDomain, elementId));

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
		return StreamSupport.stream(this.elementDao.findAll().spliterator(), false) // Stream<ElementEntity>
				.map(this.elementConverter::fromEntity) // Stream<ElementBoundary>
				.collect(Collectors.toList());
//		return this.allElements.stream().map(this.elementConverter::fromEntity).collect(Collectors.toList());

	}

	@Override
	public ElementBoundary getSpecificElement(String userDomain, String userEmail, String elementDomain,
			String elementId) {

		// Fetching the specific element from DB.
		ElementEntity foundedElement = findElement(this.elementConverter.
				concatElementDomainElementId(elementDomain, elementId));

		return elementConverter.fromEntity(foundedElement);

	}

	@Override
	public void deleteAllElements(String adminDomain, String adminEmail) {
		
		// Clear all elements from DB.
		this.elementDao.deleteAll();
//		allElements.clear();

	}
	

	private ElementEntity findElement(String elementId) {
		return this.elementDao.findById(elementId).orElseThrow(() -> new RuntimeException("could not find user by userId"));

//		ElementEntity foundedElement = allElements.stream()
//				.filter(elementEntity -> elementEntity.getElementId().getDomain().equals(elementDomain)
//						&& elementEntity.getElementId().getId().equals(elementId))
//				.findFirst().orElseThrow(() -> new RuntimeException("could not find element"));
//		return foundedElement;

	}

	private void updateElementValues(ElementEntity toBeUpdatedEntity, ElementEntity inputEntity) {

		// Copy the important values from update entity to toBeUpdateEntity only if they
		// are not null
		toBeUpdatedEntity.setActive(inputEntity.getActive());
		toBeUpdatedEntity.setElementAttributes(inputEntity.getElementAttributes());
		toBeUpdatedEntity.setLocation(inputEntity.getLocation());
		toBeUpdatedEntity.setName(inputEntity.getName());
		toBeUpdatedEntity.setType(inputEntity.getType());

	}

}
