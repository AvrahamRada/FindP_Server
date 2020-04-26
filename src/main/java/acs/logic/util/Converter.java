package acs.logic.util;
import acs.util.UserId;

public class Converter {
	
	protected final String DELIMITER = "#";
	
	public String concat(String domain, String id) {
		return domain + DELIMITER + id;
	}
	
	public UserId convertToUserId(String userId) {
		String userIdIdSplit[] = userId.split(DELIMITER);
		return new UserId(userIdIdSplit[0], userIdIdSplit[1]);
	}

}
