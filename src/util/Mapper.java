package util;

import org.json.JSONObject;

public class Mapper {

	public static JSONObject map(String value){
		JSONObject jsObject = new JSONObject();
		String input[] = value.split("&");
		for (String str : input) {
			String arr[] = str.split("=");
			jsObject.put(arr[0], arr[1]);
		}
		return jsObject;
	}
}
