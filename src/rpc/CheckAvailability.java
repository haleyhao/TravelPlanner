package rpc;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;

@WebServlet("/checkAvailability")
public class CheckAvailability extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public CheckAvailability() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String city = request.getParameter("city");
		JSONObject obj = new JSONObject();
		
		DBConnection conn = DBConnectionFactory.getConnection();
		try {
			String status = conn.getIfCommingSoon(city);
			try {
				obj.put("status is: ", status);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			RpcHelper.writeJsonObject(response, obj);
		} finally {
			conn.close();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
