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
		DBConnection conn = DBConnectionFactory.getConnection();
		
		try {
			HttpSession session = request.getSession(false);
			JSONObject obj = new JSONObject();

			if (session != null) {
				String email = session.getAttribute("email").toString();
				obj.put("status", "OK").put("email", email).put("name", conn.getFullname(email));
			} else {
				response.setStatus(403); // No authorization
				obj.put("status", "Invalid session!");
			}
			RpcHelper.writeJsonObject(response, obj);
			
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBConnection conn = DBConnectionFactory.getConnection();
		
		try {
			
			JSONObject obj = RpcHelper.readJSONObject(request);
			String email = obj.getString("email");
			String password = obj.getString("password");
			
			JSONObject returnObj = new JSONObject();
			if (conn.verifyLogin(email, password)) {
				
				HttpSession session = request.getSession(); //a new one is created since no one in the request
				session.setAttribute("email", email);
				session.setMaxInactiveInterval(600); //time out
				returnObj.put("status", "OK").put("email", email).put("name", conn.getFullname(email));
				
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
