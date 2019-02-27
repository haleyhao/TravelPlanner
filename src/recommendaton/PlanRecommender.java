package recommendaton;

import java.util.ArrayList;
import java.util.Collections;

import db.DBConnection;
import db.DBConnectionFactory;

import java.util.List;


import entity.Place;
import entity.Plan;
import entity.PlanIdGenerator;
import external.GoogleMapApi;

public class PlanRecommender {
	
	public Plan recommend(String city, String keyword, int PlaceNum) {
		//get candidate place list from Google map API: too slow
//		GoogleMapApi api = new GoogleMapApi();
//		List<Place> candidateList = api.search(city, keyword, PlaceNum * 2);
		
		List<Place> candidateList = new ArrayList<>();
		DBConnection connection = DBConnectionFactory.getConnection();
		try {
			//get candidate place list from DB
			candidateList = connection.getAllPlaces(city);
		} catch (Exception e) {
   	 		e.printStackTrace();
   	 	} finally {
   	 		if (connection != null) {
   	 			connection.close(); 
   	 		}
   	 	}
		
		
		//Sort by on popularity
		Collections.sort(candidateList, (p1, p2) -> (int) (p2.getTotalScore() - p1.getTotalScore()));	
		
		List<Place> placeList = new ArrayList<>();
		
		for (int i = 0; i < candidateList.size() && i < PlaceNum; i++) {
			placeList.add(candidateList.get(i));
		}

		
		String plan_id = PlanIdGenerator.getUsableId();
		Plan plan = new Plan(plan_id);
		plan.updatePlaceList(placeList);
		return plan;
		
	}
	
	public static void main(String[] args) {
		PlanRecommender test = new PlanRecommender();
		Plan plan = test.recommend("New York", null, 150);
		System.out.println(plan.toJSONObject().toString());
	}

}
