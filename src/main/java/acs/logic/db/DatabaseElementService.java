package acs.logic.db;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import acs.boundaries.ElementBoundary;
import acs.boundaries.ElementIdBoundary;
import acs.dal.ElementDao;
import acs.data.ElementEntity;
import acs.logic.ElementNotFoundException;
import acs.logic.ElementService;
import acs.logic.EnhancedElementService;
import acs.logic.util.ElementConverter;
import acs.util.CreatedBy;
import acs.util.ElementId;
import acs.util.UserId;

@Service
public class DatabaseElementService implements EnhancedElementService {
	private String projectName;
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
	@Transactional // (readOnly = false)
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

		// Add to database
		this.elementDao.save(elementEntity);

		return elementBoundary;

	}

	@Override
	@Transactional // (readOnly = false)
	public ElementBoundary update(String managerDomain, String managerEmail, String elementDomain, String elementId,
			ElementBoundary update) {

		// Fetching the specific element from DB.
		ElementEntity foundedElement = findElement(this.elementConverter.convertToEntityId(elementDomain, elementId));

		// Convert the input to entity before update the values in element entity that
		// is in the DB.
		ElementEntity updateEntity = elementConverter.toEntity(update);

		// Update the element's values.
		updateElementValues(foundedElement, updateEntity);

		// save updated element to the database
		this.elementDao.save(foundedElement);

		// Convert the update entity to boundary and returns it.
		return elementConverter.fromEntity(foundedElement);

	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getAll(String userDomain, String userEmail) {
		return StreamSupport.stream(this.elementDao.findAll().spliterator(), false) // Stream<ElementEntity>
				.map(this.elementConverter::fromEntity) // Stream<ElementBoundary>
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public ElementBoundary getSpecificElement(String userDomain, String userEmail, String elementDomain,
			String elementId) {

		// Fetching the specific element from DB.
		ElementEntity foundedElement = findElement(this.elementConverter.convertToEntityId(elementDomain, elementId));

		return elementConverter.fromEntity(foundedElement);

	}

	@Override
	@Transactional // (readOnly = false)
	public void deleteAllElements(String adminDomain, String adminEmail) {
		// Clear all elements from DB.
		this.elementDao.deleteAll();

	}

	private ElementEntity findElement(String elementId) {
		return this.elementDao.findById(elementId)
				.orElseThrow(() -> new ElementNotFoundException("could not find user by userId"));
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

	@Override
	@Transactional // (readOnly = false)
	public void bindParentElementToChildElement(String managerDomain, String managerEmail, String elementDomain,
			String elementId, ElementIdBoundary elementIdBoundary) {
		
		
		ElementEntity originElement = this.elementDao.findById(this.elementConverter.convertToEntityId(elementDomain, elementId))
				.orElseThrow(() -> new ElementNotFoundException("could not find origin by id: " + elementId));

		ElementEntity childElement = this.elementDao
				.findById(this.elementConverter.convertToEntityId(elementIdBoundary.getDomain(), elementIdBoundary.getId()))
				.orElseThrow(() -> new ElementNotFoundException("could not find reply by id: " + elementIdBoundary.getId()));

		originElement.addChildElement(childElement);
		this.elementDao.save(originElement);
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<ElementBoundary> getAllChildrenElements(String userDomain, String userEmail, String elementDomain,
			String elementId, int size, int page) {
		return this.elementDao.findById(this.elementConverter.convertToEntityId(elementDomain, elementId))
				.orElseThrow(() -> new ElementNotFoundException("could not find origin by id: " + elementId))
				.getChildrenElements().stream().map(this.elementConverter::fromEntity).collect(Collectors.toSet());
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<ElementBoundary> getAllOriginsElements(String userDomain, String userEmail, String elementDomain,
			String elementId, int size, int page) {
		ElementEntity reply = this.elementDao.findById(this.elementConverter.convertToEntityId(elementDomain, elementId))
				.orElseThrow(() -> new ElementNotFoundException("could not find reply by id: " + elementId));

		ElementEntity origin = reply.getOrigin();
		Set<ElementBoundary> rv = new HashSet<>();

		if (origin != null) {
			rv.add(this.elementConverter.fromEntity(origin));
		}

		return rv;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getAll(String userDomain, String userEmail, int size, int page) {
		// TODO Auto-generated method stub & 
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getAllElementsByName(String name, int size, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getAllElementsByType(String type, int size, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getAllElementsByLocation(String lat, String lng, String distance, int size, int page) {
		// TODO Auto-generated method stub
		return null;
	}

}
