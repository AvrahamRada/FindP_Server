package acs.logic.util;

import acs.boundaries.ElementBoundary;
import acs.data.ElementEntity;

public class ElementConverter {
	
	public ElementBoundary fromEntity(ElementEntity entity) {
		ElementBoundary rv = new ElementBoundary();
		rv.setActive(entity.getActive());
		rv.setCreatedBy(entity.getCreatedBy());
		rv.setCreatedTimeStamp(entity.getCreatedTimeStamp());
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
		rv.setCreatedTimeStamp(boundary.getCreatedTimeStamp());
		rv.setElementAttributes(boundary.getElementAttributes());
		rv.setElementId(boundary.getElementId());
		rv.setLocation(boundary.getLocation());
		rv.setName(boundary.getName());
		rv.setType(boundary.getType());
		return rv;
	}

}
