package acs.logic.util;

import org.springframework.stereotype.Component;

import acs.boundaries.ElementBoundary;
import acs.data.ElementEntity;
@Component
public class ElementConverter {
	
	public ElementBoundary fromEntity(ElementEntity entity) {
		ElementBoundary rv = new ElementBoundary();
		rv.setActive(entity.getActive());
		rv.setCreatedBy(entity.getCreatedBy());
		rv.setCreatedTimestamp(entity.getCreatedTimestamp());
		rv.setElementAttributes(entity.getElementAttributes());
		rv.setElementId(entity.getElementId());
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
		rv.setElementId(boundary.getElementId());
		rv.setLocation(boundary.getLocation());
		rv.setName(boundary.getName());
		rv.setType(boundary.getType());
		return rv;
	}

}
