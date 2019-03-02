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
import org.json.JSONException;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Place;
import entity.Plan;
import recommendaton.PlanRecommender;

@WebServlet("/recommend")
public class RecommendPlans extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public RecommendPlans() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String city = request.getParameter("city").trim();
		String keyword = request.getParameter("keyword").trim();
		
		try {
			JSONArray array = new JSONArray();
			List<Integer> lengths = Arrays.asList(3,6,9);
			PlanRecommender planner = new PlanRecommender();
			for (int len : lengths) {
				Plan plan = planner.recommend(city, keyword, len);
				JSONObject obj = plan.toJSONObject();
				array.put(obj);
			}
			System.out.println("finish");
			RpcHelper.writeJsonArray(response, array);
		} catch(Exception e) {
			e.printStackTrace();
   	 	}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
