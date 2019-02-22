package db;

import java.util.List;
import java.util.Set;

import org.json.JSONArray;

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

}
