package acs.logic.util;

import org.springframework.stereotype.Component;

import acs.boundaries.ElementBoundary;
import acs.data.ElementEntity;
import acs.util.ElementId;

@Component
public class ElementConverter {

	public ElementBoundary fromEntity(ElementEntity entity) {
		ElementBoundary rv = new ElementBoundary();
		rv.setActive(entity.getActive());
		rv.setCreatedBy(entity.getCreatedBy());
		rv.setCreatedTimestamp(entity.getCreatedTimestamp());
		rv.setElementAttributes(entity.getElementAttributes());
		rv.setElementId(splitDomainEmail(entity.getElementId()));
		rv.setLocation(entity.getLocation());
		rv.setName(entity.getName());
		rv.setType(entity.getType());
		return rv;
	}

	public ElementEntity toEntity(ElementBoundary boundary) {
		ElementEntity rv = new ElementEntity();

		rv.setActive(boundary.getActive());
		rv.setCreatedBy(boundary.getCreatedBy());
		rv.setCreatedTimestamp(boundary.getCreatedTimestamp());
		rv.setElementAttributes(boundary.getElementAttributes());
		rv.setElementId(concatElementDomainElementId(boundary.getElementId().getDomain(),
				boundary.getElementId().getId()));
		rv.setLocation(boundary.getLocation());
		rv.setName(boundary.getName());
		rv.setType(boundary.getType());

		return rv;
	}
	
	public String concatElementDomainElementId(String domain, String id) {
		return domain + "#" + id;
	}
	
	public ElementId splitDomainEmail(String elementId) {
		String elementIdSplit[] = elementId.split("#");
		return new ElementId(elementIdSplit[0], elementIdSplit[1]);
	}

}
