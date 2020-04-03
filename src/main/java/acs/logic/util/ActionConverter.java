package acs.logic.util;

import org.springframework.stereotype.Component;

import acs.boundaries.ActionBoundary;
import acs.data.ActionEntity;
@Component
public class ActionConverter {
	
	public ActionBoundary fromEntity(ActionEntity entity) {
		ActionBoundary rv = new ActionBoundary();
		rv.setActionAttributes(entity.getActionAttributes());
		rv.setActionId(entity.getActionId());
		rv.setCreatedTimestamp(entity.getCreatedTimestamp());
		rv.setElement(entity.getElement());
		rv.setInvokedBy(entity.getInvokedBy());
		rv.setType(entity.getType());
		return rv;
	}

	public ActionEntity toEntity(ActionBoundary boundary) {
		ActionEntity rv = new ActionEntity();
		rv.setActionAttributes(boundary.getActionAttributes());
		rv.setActionId(boundary.getActionId());
		rv.setCreatedTimestamp(boundary.getCreatedTimestamp());
		rv.setElement(boundary.getElement());
		rv.setInvokedBy(boundary.getInvokedBy());
		rv.setType(boundary.getType());
		return rv;
	}

}
