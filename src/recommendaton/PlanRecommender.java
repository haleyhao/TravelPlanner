package recommendaton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.List;


import entity.Place;
import entity.Plan;
import entity.PlanIdGenerator;
import external.GoogleMapApi;

public class PlanRecommender {
	
	public Plan recommend(String city, String keyword, int PlaceNum) {
		GoogleMapApi api = new GoogleMapApi();
		List<Place> candidateList = api.search(city, keyword, PlaceNum * 2);		
		Collections.sort(candidateList, (p1, p2) -> (int) (p2.getTotalScore() - p1.getTotalScore()));
		
		
//		Set<String> names = new HashSet<>();
//		int i = 0;
//		for (Place p : placeList) {
//			System.out.println(i + " " + p.getName() + p.getTotalScore());
//			names.add(p.getName());
//			i++;
//		}
//		System.out.println("Univque number is " + names.size());
		
		
		List<String> placeIdList = new ArrayList<>();
		
		for (int i = 0; i < candidateList.size() && i < PlaceNum; i++) {
			placeIdList.add(candidateList.get(i).getPlaceId());
		}

		
		String plan_id = PlanIdGenerator.getUsableId();
		Plan plan = new Plan(plan_id);
		plan.updatePlaceIdList(placeIdList);
		return plan;
	}
	
	public static void main(String[] args) {
		PlanRecommender test = new PlanRecommender();
		Plan plan = test.recommend("New York", null, 150);
		System.out.println(plan.toJSONObject().toString());
	}
}
