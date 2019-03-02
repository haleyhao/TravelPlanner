package rpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Place;
import entity.Plan;
import external.GoogleMapApi;
import recommendaton.PlanRecommender;

@WebServlet("/history")
public class PlanHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public PlanHistory() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("user_id");
		String planId = request.getParameter("plan_id");
		
		DBConnection connection = DBConnectionFactory.getConnection();
		try {
			if (planId == null) { //fetch all plans of a user
				JSONArray array = new JSONArray();
				for (Plan plan : connection.fetchPlans(email)) {
					JSONObject obj = plan.toJSONObject();
					array.put(obj);
				}
				RpcHelper.writeJsonArray(response, array);
			} else { //fetch one plan of a user
				JSONObject obj = connection.fetchPlan(email, planId).toJSONObject();
				RpcHelper.writeJsonObject(response, obj);
			}
			System.out.println("finish");		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.close(); 
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	   	 DBConnection connection = DBConnectionFactory.getConnection();
	   	 try {
	   		 System.out.println("get post request");
	   		 JSONObject input = RpcHelper.readJSONObject(request);
	   		 String userId = input.getString("user_id");
	   		 System.out.println(userId);
	   		 String planId = input.getString("plan_id");
	   		 System.out.println(planId);
	   		 JSONArray placeIdArray = input.getJSONArray("place_ids");
	   		 System.out.println(placeIdArray.toString());
	   		 
	   		 /*
	   		  * check if the place is in the DB.
	   		  * If not, fetch place detail from Google map API and save to DB
	   		  */
	   		 List<Place> newPlaces = new ArrayList<>();
	   		 GoogleMapApi googleApi = new GoogleMapApi();
	   		 for(int i = 0; i < placeIdArray.length(); ++i) {
	   			String place_id = placeIdArray.getString(i);
	   			if (connection.getPlace(place_id) == null) {
	   				Place newPlace = googleApi.getPlaceDetail(place_id);
	   				if (newPlace != null) {
	   					newPlaces.add(newPlace);
	   				}
	   			}
	   		 }
	   		 connection.savePlaces(newPlaces);
	   		 connection.savePlan(userId, planId, placeIdArray);
	   		 RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));

	   	 } catch (Exception e) {
	   		 e.printStackTrace();
	   	 } finally {
	   		 if (connection != null) {
	   			connection.close(); 
	   		 }
	   	 }
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		DBConnection conn = DBConnectionFactory.getConnection();
		
		try {
			
			JSONObject input = RpcHelper.readJSONObject(request);
			String userId = input.getString("user_id");
			String planId = input.getString("plan_id");
			conn.deletePlan(userId, planId);
			RpcHelper.writeJsonObject(response, new JSONObject().put("result","SUCCESS"));
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}

}
