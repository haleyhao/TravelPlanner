package db;

<<<<<<< HEAD
public interface DBConnection {
	
	public void close();
	
	public boolean verifyLogin(String userId, String password);
	
	public void register(String userId, String password, String firstname, String lastname);
	
	public String getFullname(String userId);
=======
import java.util.List;
import java.util.Set;

import org.json.JSONArray;

import entity.Place;
import entity.Plan;

public interface DBConnection {
	/**
	 * Close the connection.
	 */
	public void close();

	/**
	 * Get the city name for a city.
	 * 
	 * @param userId
	 * @return cityList
	 */
	public Set<String> getCitys(String userId);

	/**
	 * Gets categories based on item id
	 * 
	 * @param cityId
	 * @return string of status
	 */
	public String getIfCommingSoon(String cityId);

	public void savePlan(String userId, String planId, JSONArray placeIds);
	
	public void deletePlan(String user_id, String plan_id);
	
	public Plan fetchPlan(String user_id, String plan_id);
	
	public List<Plan> fetchPlans(String email);
	
	public String getFullname(String userId);
	
	public boolean verifyLogin(String userId, String password);
	
	public boolean addNewUser(String email, String password, String name);
	
	public Place getPlace(String place_id);
	
	public void savePlaces(List<Place> placeList);
	
	public List<Place> getAllPlaces(String city);
>>>>>>> branch 'backend' of https://github.com/haleyhao/TravelPlanner.git
}
