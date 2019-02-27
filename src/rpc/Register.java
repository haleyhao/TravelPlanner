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

@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Register() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		DBConnection conn = DBConnectionFactory.getConnection();
		try {
			
			JSONObject obj = RpcHelper.readJSONObject(request);
			String email = obj.getString("email");
			String password = obj.getString("password");
			String name = obj.getString("name");
			
			JSONObject returnObj = new JSONObject();
			if (conn.getFullname(email).isEmpty()) {
				conn.addNewUser(email, password, name);
				HttpSession session = request.getSession(); //a new one is created since no one in the request
				session.setAttribute("email", email);
				session.setMaxInactiveInterval(600); //time out
				returnObj.put("status", "OK");
			} else {
				response.setStatus(403); // user already exists
				returnObj.put("status", "User already exist!");
			}
			
			RpcHelper.writeJsonObject(response, returnObj);
			
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}

}
