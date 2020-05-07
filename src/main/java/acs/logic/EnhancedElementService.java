package acs.logic;

import java.util.Collection;
import java.util.Set;

import acs.boundaries.ElementBoundary;
import acs.boundaries.ElementIdBoundary;

public interface EnhancedElementService extends ElementService {
	void bindParentElementToChildElement(String managerDomain, String managerEmail, String elementDomain,
			String elementId, ElementIdBoundary input);

	public Set<ElementBoundary> getAllChildrenElements(String userDomain,String userEmail,String elementDomain,String elementId);

	public Collection<ElementBoundary> getAllOriginsElements(String userDomain,String userEmail,String elementDomain,String elementId);
}
