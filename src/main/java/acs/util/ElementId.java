package acs.util;

public class ElementId {

	private String domain;
	private String id;

	public ElementId() {
		// TODO Auto-generated constructor stub
	}

	public ElementId(String domain, String id) {
		super();
		this.domain = domain;
		this.id = id;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void validation() {

		if (domain == null) {
			throw new RuntimeException("domain was not instantiate");
		}

		if (id == null) {
			throw new RuntimeException("id was not instantiate");
		}
	}

}
