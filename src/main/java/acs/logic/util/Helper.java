package acs.logic.util;

import java.util.Map;

public class Helper {

	public static Map<String, Object> filterNullValuesFromMap(Map<String, Object> map) {
		
		//filter all the null key value from the map;

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			if (key == null || value == null) {
				map.remove(key);
			}
		}

		return map;

	}

}
