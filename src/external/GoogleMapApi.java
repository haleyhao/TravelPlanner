package external;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entity.Place;
import entity.Place.PlaceBuilder;

public class GoogleMapApi {
	private static final String URL = "https://maps.googleapis.com/maps/api/place/textsearch/json";
	private static final String URL2 = "https://maps.googleapis.com/maps/api/place/details/json";
	private static final String DEFAULT_KEYWORD = "point of interest";
	private static final String API_KEY = "AIzaSyB7CnE8tQMPEt_kEYdoT_vSkYTNcMCrjiI"; //Wenhan's API_KEY
	
	public List<Place> search(String city, String keyword, int maxNum) {
		
		if (keyword == null) {
			keyword = DEFAULT_KEYWORD;
		}
		
		String text = city + " " + keyword;
		
		try {
			text = URLEncoder.encode(text,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String query = String.format("query=%s&language=en&key=%s",text, API_KEY);
		String url = URL + "?" + query;
		
		List<Place> placeList = new ArrayList<>();
		String nextToken = "";
		while (placeList.size() < maxNum) {
			nextToken = getNext20(placeList, url + (nextToken.isEmpty() ? "" : "&pagetoken=" + nextToken), city);
			if(nextToken.isEmpty()) break;
		}
		return placeList;
	}
	
	public String getNext20(List<Place> placeList, String url, String city) {
		System.out.println(url);
		String nextToken = "";
		try {
			Thread.sleep(5000);
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			int code = connection.getResponseCode();
			if (code != 200) {
				System.out.println("Connection failed");
				return nextToken;
			}
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder response = new StringBuilder();

			String line = "";
			while((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();
			
			JSONObject obj = new JSONObject(response.toString());
			System.out.println(obj);
			if (!obj.isNull("next_page_token")) {
				nextToken = obj.getString("next_page_token");
			}

			//{"results", jsonArray[]}
			if (!obj.isNull("results")) {
				placeList.addAll(getPlaceList(obj.getJSONArray("results"), city));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return new String(nextToken);
	}
	
	// Convert JSONArray to a list of item objects.
	private List<Place> getPlaceList(JSONArray places, String city) throws JSONException {
		
		List<Place> placeList = new ArrayList<>();
		
		for (int i = 0; i < places.length(); ++i) {
			
			JSONObject place = places.getJSONObject(i);
			
			PlaceBuilder builder = new PlaceBuilder();
			if (!place.isNull("place_id")) {
				builder.setPlace_id(place.getString("place_id"));
			}
			if (!place.isNull("name")) {
				builder.setName(place.getString("name"));
			}
			if (!place.isNull("formatted_address")) {
				builder.setAddress(place.getString("formatted_address"));
			}
			if (!place.isNull("rating")) {
				builder.setRating(place.getDouble("rating"));
			}
			if (!place.isNull("user_ratings_total")) {
				builder.setRaingTotal(place.getInt("user_ratings_total"));
			}
			
			if (!place.isNull("geometry")) {
				builder.setLocation(getLat(place), getLon(place));
			}
			
			builder.setCity(city);
			
			placeList.add(builder.build());
		}

		return placeList;
	}
	
	public double getLat(JSONObject place) throws JSONException {
		if (place.isNull("geometry") || place.getJSONObject("geometry").isNull("location")) {
			return 0;
		}
		JSONObject location = place.getJSONObject("geometry").getJSONObject("location");
		return location.getDouble("lat");
		
	}
	
	public double getLon(JSONObject place) throws JSONException {
		if (place.isNull("geometry") || place.getJSONObject("geometry").isNull("location")) {
			return 0;
		}
		JSONObject location = place.getJSONObject("geometry").getJSONObject("location");
		return location.getDouble("lng");
	}
	
	public Place getPlaceDetail(String placeId) {
		String query = String.format("placeid=%s&key=%s",placeId, API_KEY);
		String url = URL2 + "?" + query;
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			int code = connection.getResponseCode();
			if (code != 200) {
				System.out.println("Connection failed");
				return null;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder response = new StringBuilder();

			String line = "";
			while((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();
			
			JSONObject obj = new JSONObject(response.toString());
			
			if (obj.isNull("result")) {
				return null;
			}
			
			JSONObject resultObj = obj.getJSONObject("result");

			PlaceBuilder builder = new PlaceBuilder();
			builder.setPlace_id(placeId);
			if (!resultObj.isNull("place_id")) {
				builder.setPlace_id(resultObj.getString("place_id"));
			}
			if (!resultObj.isNull("name")) {
				builder.setName(resultObj.getString("name"));
			}
			if (!resultObj.isNull("formatted_address")) {
				builder.setAddress(resultObj.getString("formatted_address"));
			}
			if (!resultObj.isNull("rating")) {
				builder.setRating(resultObj.getDouble("rating"));
			}
			if (!resultObj.isNull("user_ratings_total")) {
				builder.setRaingTotal(resultObj.getInt("user_ratings_total"));
			}
			if (!resultObj.isNull("geometry")) {
				builder.setLocation(getLat(resultObj), getLon(resultObj));
			}
			String city = ""; 
			JSONArray addressComponents = resultObj.getJSONArray("address_components");
			for (int i = 0; i < addressComponents.length(); i++) {
				if (!city.isEmpty()) break;
				JSONObject component = addressComponents.getJSONObject(i);
				JSONArray types = component.getJSONArray("types");
				for (int j = 0; j < types.length(); j++) {
					if (types.getString(j).equals("locality")) {
						city = component.getString("long_name");
						break;
					}
				}
			}
			builder.setCity(city.trim().toLowerCase());
			return builder.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		GoogleMapApi api = new GoogleMapApi();
//		List<Place> result = api.search("new york", null, 100);
//		System.out.println(result.size());
		Place x = api.getPlaceDetail("ChIJx8xdGIdZwokRT15ilvIVcTM");
		System.out.println(x.getCity());
	}
}