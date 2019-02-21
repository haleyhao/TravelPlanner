package entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Plan {
	private String plan_Id;
	private List<String> placeIdList;
	
	public Plan(String id) {
		plan_Id = id;
		placeIdList = new ArrayList<String>();
	}
	public void addNewPlace(String POI_id) {
		placeIdList.add(POI_id);
	}
	public void updatePlaceIdList(List<String> list) {
		placeIdList = new ArrayList<String>(list);
	}
	
	public List<String> getPlaceList() {
		return placeIdList;
	}
	
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("plan_id", plan_Id);
			obj.put("number", placeIdList.size());
			JSONArray array = new JSONArray();
			for (String id : placeIdList) {
				array.put(id);
			}
			obj.put("place_ids", array);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
