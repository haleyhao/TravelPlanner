package entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Plan {
	private String plan_Id;
	private List<Place> placeList;
	
	public Plan(String id) {
		plan_Id = id;
		placeList = new ArrayList<Place>();
	}
	public void addNewPlace(Place place) {
		placeList.add(place);
	}
	public void updatePlaceList(List<Place> list) {
		placeList = new ArrayList<Place>(list);
	}
	
	public List<Place> getPlaceList() {
		return placeList;
	}
	
	//JSONObj {'plan_id':plan_id, 'number': num, 'place_ids': [], 'place_names': [], 'place_geos': [{'lat':lat 'lon':lon}]
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("plan_id", plan_Id);
			obj.put("number", placeList.size());
			
			JSONArray idArray = new JSONArray();
			JSONArray nameArray = new JSONArray();
			JSONArray geoArray = new JSONArray();

			for (Place place : placeList) {
				idArray.put(place.getPlaceId());
				nameArray.put(place.getName());
				JSONObject geoObj = new JSONObject();
				geoObj.put("lat", place.getLat());
				geoObj.put("lon", place.getLon());
				geoArray.put(geoObj);
			}
			obj.put("place_ids", idArray);
			obj.put("place_names", nameArray);
			obj.put("place_geos", geoArray);
			
			return obj;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
