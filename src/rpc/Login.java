<<<<<<< HEAD
package rpc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;


    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
   	 super();
   	 // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *  	response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
      		 throws ServletException, IOException {
   		DBConnection connection = DBConnectionFactory.getConnection();
   		try {
   			HttpSession session = request.getSession(false);
   			JSONObject obj = new JSONObject();
   			
   			if (session != null) {
   				String userId = session.getAttribute("user_id").toString();
   				obj.put("status", "OK").put("user_id", userId).put("name", connection.getFullname(userId));
   			} else {
   				response.setStatus(403);
   				obj.put("status", "Invalid Session");
   			}
   			RpcHelper.writeJsonObject(response, obj);
   		} catch (Exception e) {
   			e.printStackTrace();
   		} finally {
   			connection.close();
   		}
       }

       /**
        * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
        *  	response)
        */
       protected void doPost(HttpServletRequest request, HttpServletResponse response)
      		 throws ServletException, IOException {		
   		DBConnection connection = DBConnectionFactory.getConnection();
   		try {
   			JSONObject input = RpcHelper.readJSONObject(request);
   			String userId = input.getString("user_id");
   			String password = input.getString("password");
   			
   			JSONObject obj = new JSONObject();
   			if (connection.verifyLogin(userId, password)) {
   				HttpSession session = request.getSession();
   				session.setAttribute("user_id", userId);
   				session.setMaxInactiveInterval(600);
   				obj.put("status", "OK").put("user_id", userId).put("name", connection.getFullname(userId));
   			} else {
   				response.setStatus(401);
   				obj.put("status", "User Doesn't Exist");
   			}
   			RpcHelper.writeJsonObject(response, obj);
   		} catch (Exception e) {
   			e.printStackTrace();
   		} finally {
   			connection.close();
   		}
       }


}
=======
package rpc;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;

@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Login() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBConnection connection = DBConnectionFactory.getConnection();
		try {
			HttpSession session = request.getSession(false);
			JSONObject obj = new JSONObject();
			
			if (session != null) {
				String user_id = session.getAttribute("user_id").toString();
				obj.put("status", "OK").put("user_id", user_id).put("name", connection.getFullname(user_id));
			} else {
				response.setStatus(403);
				obj.put("status", "Invalid Session");
			}
			RpcHelper.writeJsonObject(response, obj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBConnection conn = DBConnectionFactory.getConnection();
		
		try {
			
			JSONObject obj = RpcHelper.readJSONObject(request);
			String user_id = obj.getString("user_id");
			String password = obj.getString("password");
			
			JSONObject returnObj = new JSONObject();
			if (conn.verifyLogin(user_id, password)) {
				
				HttpSession session = request.getSession(); //a new one is created since no one in the request
				session.setAttribute("user_id", user_id);
				session.setMaxInactiveInterval(600); //time out
				returnObj.put("status", "OK").put("user_id", user_id).put("name", conn.getFullname(user_id));
				
			} else {
				response.setStatus(401); // No such user
				returnObj.put("status", "User doesn't exist!");
			}
			
			RpcHelper.writeJsonObject(response, returnObj);
			
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}

}
>>>>>>> branch 'backend' of https://github.com/haleyhao/TravelPlanner.git
