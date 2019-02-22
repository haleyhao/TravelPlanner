package rpc;

import java.io.IOException;
import java.util.ArrayList;
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
import db.mysql.MySQLConnection;

@WebServlet("/history")
public class PlanHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public PlanHistory() {
        super();
    }

    //TO DO: For fetchPlan and fetchPlans
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	//TO DO: For savePlan
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	   	 DBConnection connection = DBConnectionFactory.getConnection();
	   	 try {
	   		 JSONObject input = RpcHelper.readJSONObject(request);
	   		 String planId = input.getString("plan_id");
	   		 JSONArray array = input.getJSONArray("place_ids");
	   		 List<String> placeIds = new ArrayList<>();
	   		 for(int i = 0; i < array.length(); ++i) {
	   			placeIds.add(array.getString(i));
	   		 }
	   		 
	   		 connection.savePlan(planId, placeIds);
	   		 RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));
	   		 
	   	 } catch (Exception e) {
	   		 e.printStackTrace();
	   	 } finally {
	   		 if (connection != null) {
	   			connection.close(); 
	   		 }	   		 
	   	 }
	}

	//For deletePlan
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		MySQLConnection conn = new MySQLConnection();
		
		try {
			
			JSONObject input = RpcHelper.readJSONObject(request);
			String userId = input.getString("user_id");
			String plan_Id = input.getString("plan_id");
			conn.deletePlan(userId, plan_Id);
			RpcHelper.writeJsonObject(response, new JSONObject().put("result","SUCCESS"));
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}

}
