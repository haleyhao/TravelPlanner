package rpc;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Place;
import external.GoogleMapApi;

@WebServlet("/saveplace")
public class CreatePlacesTable extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public CreatePlacesTable() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String city = request.getParameter("city");
		JSONObject obj = new JSONObject();
		
		DBConnection conn = DBConnectionFactory.getConnection();
		try {
			GoogleMapApi googleApi = new GoogleMapApi();
			List<Place> allPlaces = googleApi.search(city, null, 150);
			conn.savePlaces(allPlaces);
			obj.put("Number of saved: ", allPlaces.size());
			RpcHelper.writeJsonObject(response, obj);
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
