package db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
<<<<<<< HEAD

import db.DBConnection;


public class MySQLConnection implements DBConnection {

private Connection conn;
    
    public MySQLConnection() {
   	 try {
   		 Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
   		 conn = DriverManager.getConnection(MySQLDBUtil.URL);
   		 
   	 } catch (Exception e) {
   		 e.printStackTrace();
   	 }
    }
    
    @Override
    public void close() {
   	 if (conn != null) {
   		 try {
   			 conn.close();
   		 } catch (Exception e) {
   			 e.printStackTrace();
   		 }
   	 }
    }


	@Override
	public boolean verifyLogin(String userId, String password) {
		// TODO Auto-generated method stub
		if (conn == null) {
			return false;
		}
		
		try {
			String sql = "SELECT user_id FROM users WHERE user_id = ? AND password = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			statement.setString(2, password);

			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				return true;
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	
		return false;
	}

	@Override
	public void register(String userId, String password, String firstname, String lastname) {
		// TODO Auto-generated method stub
		if(conn == null) {
			System.err.println("DB connection failed");
			return;
		}
		
		try {
			String sql = "INSERT IGNORE INTO users VALUES (?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, password);
			ps.setString(3, firstname);
			ps.setString(4, lastname);
			ps.execute();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}

	@Override
	public String getFullname(String userId) {
		// TODO Auto-generated method stub
		if (conn == null) {
			return "";
		}
		
		String name = "";
		try {
			String sql = "SELECT first_name, last_name FROM users WHERE user_id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				name = rs.getString("first_name") + " " + rs.getString("last_name");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return name;

	}

=======
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.cj.jdbc.Driver;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Place;
import entity.Place.PlaceBuilder;
import external.GoogleMapApi;
import rpc.RpcHelper;
import entity.Plan;
import entity.PlanIdGenerator;


public class MySQLConnection implements DBConnection{
	
	private Connection connection;
    
    public MySQLConnection() {
   	 try {
   		 Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
   		 connection = DriverManager.getConnection(MySQLDBUtil.URL);
   	 } catch (Exception e) {
   		 e.printStackTrace();
   	 }
    }
    
    @Override
    public void close() {
   	 if (connection != null) {
   		 try {
   			connection.close();
   			} catch (Exception e) {
   				e.printStackTrace();
   				}
   		 }
    }

	@Override
	public Set<String> getCitys(String itemId) {
		if (connection == null) {
			return null;
		}
		Set<String> citys = new HashSet<>();
		try {
			String sql = "SELECT city_name from citys";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				String city = rs.getString("city_name");
				citys.add(city);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return citys;
	}

	@Override
	public String getIfCommingSoon(String city) {
		if (connection == null) {
			return "";
		}
		
		String ifCommingSoon = "";
		try {
			String sql = "SELECT commingSoon FROM citys WHERE city_name = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, city);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				ifCommingSoon = rs.getString("commingSoon");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return ifCommingSoon;
	}
	
	@Override
	public void deletePlan(String user_id, String plan_id) {
		if (connection == null) {
			System.err.println("DS connection failed");
			return ;
		}
		
		try {
			String sql = "DELETE FROM plan WHERE plan_id = ? and user_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			System.out.println(plan_id + user_id);
			ps.setString(1, plan_id);
			ps.setString(2, user_id);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void savePlan(String userId, String planId, JSONArray placeIdArray) {
		if (connection == null) {
			System.err.println("DB connection failed");
			return;
			}

		try {
			String sql = "INSERT IGNORE INTO plan(plan_id, place_ids, user_id) VALUES (?, ?, ?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, PlanIdGenerator.getUsableId());
			ps.setString(2, placeIdArray.toString());
			ps.setString(3, userId);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}	

	}
	
	@Override
	public List<Plan> fetchPlans(String email) {
		if (connection == null) {
			System.err.println("DB connection failed");
			return null;
		}
		List<Plan> plans = new ArrayList<>();
		try {
			String sql = "SELECT * FROM plan WHERE user_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				List<Place> placeList = new ArrayList<Place>();
				Plan plan = new Plan(rs.getString("plan_id"));
				System.out.println(rs.getString("place_ids"));
				JSONArray array = new JSONArray(rs.getString("place_ids"));
				for (int i = 0; i < array.length(); i++) {
					Place place = getPlace(array.getString(i));
					if (place != null) {
						placeList.add(place);
					}
				}
				plan.updatePlaceList(placeList);
				plans.add(plan);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return plans;
	}
	
	@Override
	public Plan fetchPlan(String email, String plan_id) {
		if (connection == null) {
			System.err.println("DB connection failed");
			return null;
		}
		
		Plan plan = new Plan(plan_id);
		
		try {
			String sql = "SELECT place_ids FROM plan WHERE user_id = ? and plan_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, email);
			ps.setString(2, plan_id);
			ResultSet rs = ps.executeQuery();
			List<Place> placeList = new ArrayList<Place>();
			if(rs.next()) {
				System.out.println(rs.getString("place_ids"));
				JSONArray array = new JSONArray(rs.getString("place_ids"));
				for (int i = 0; i < array.length(); i++) {
					Place place = getPlace(array.getString(i));
					if (place != null) {
						placeList.add(place);
					}
				}
			}
			plan.updatePlaceList(placeList);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return plan;
	}
	
	@Override
	public String getFullname(String email) {
		if (connection == null) {
			System.err.println("DB connection failed");
			return "";
		}
		String fullName = "";
		try {
			String sql = "SELECT name FROM users WHERE email = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				fullName = rs.getString("name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return fullName;
	}
	
	@Override
	public boolean verifyLogin(String email, String password) {
		if (connection == null) {
			System.err.println("DB connection failed");
			return false;
		}
		try {
			String sql = "SELECT email FROM users WHERE email = ? AND password = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, email);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;	
	}
	
	@Override
	public boolean addNewUser(String email, String password, String name) {
		
		if (connection == null) {
			System.err.println("DB connection failed");
			return false;
		}
		try {
			String sql = "INSERT INTO users VALUES(?, ?, ?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, email);
			ps.setString(2, password);
			ps.setString(3, name);
			ps.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;			
	}
	
	@Override
	public Place getPlace(String place_id) {
		if (connection == null) {
			System.err.println("DB connection failed");
			return null;
		}
		
		try {
			String sql = "SELECT * FROM places WHERE place_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, place_id);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				PlaceBuilder builder = new PlaceBuilder();
				builder.setPlace_id(place_id)
					   .setName(rs.getString("name"))
					   .setRating(rs.getFloat("rating"))
					   .setRaingTotal(rs.getInt("total_rated"))
					   .setCity(rs.getString("city"))
					   .setAddress(rs.getString("formatted_address"))
					   .setLocation(rs.getFloat("lat"), rs.getFloat("lon"));
				return builder.build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void savePlaces(List<Place> placeList) {
		if (connection == null) {
			System.err.println("DB connection failed");
			return;
		}

		try {
			String sql = "INSERT IGNORE INTO places(place_id, name, formatted_address, city, rating, total_rated, lat, lon) VALUES (?,?,?,?,?,?,?,?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			for (Place place : placeList) {
				ps.setString(1, place.getPlaceId());
				ps.setString(2, place.getName());
				ps.setString(3, place.getAddress());
				ps.setString(4, place.getCity());
				ps.setFloat(5, (float)place.getRating());
				ps.setInt(6, place.getTotalRated());
				ps.setFloat(7, (float)place.getLat());
				ps.setFloat(8, (float)place.getLon());
				ps.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	@Override
	public List<Place> getAllPlaces(String city) {
		List<Place> allPlaces = new ArrayList<>();
		if (connection == null) {
			System.err.println("DB connection failed");
			return null;
		}
		
		try {
			String sql = "SELECT * FROM places WHERE city = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, city);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				PlaceBuilder builder = new PlaceBuilder();
				builder.setPlace_id(rs.getString("place_id"))
					   .setName(rs.getString("name"))
					   .setRating(rs.getFloat("rating"))
					   .setRaingTotal(rs.getInt("total_rated"))
					   .setCity(rs.getString("city"))
					   .setAddress(rs.getString("formatted_address"))
					   .setLocation(rs.getFloat("lat"), rs.getFloat("lon"));
				allPlaces.add(builder.build());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allPlaces;
	}
	
	public static void main(String[] args) { 
		DBConnection connection = DBConnectionFactory.getConnection();
		try {
			//test saveplaces
//			GoogleMapApi api = new GoogleMapApi();
//			List<Place> result = api.search("new york", null, 100);
//   		 	connection.savePlaces(result);
			
			//test getPlace
			Place place = connection.getPlace("ChIJnxlg1U5YwokR8T90UrZiIwI");
			System.out.println(place.getName());
			
			//test fetchPlan
//			Plan plan =connection.fetchPlan("1", "1");
//			System.out.println(plan.toJSONObject().toString());
			
			//test getAllPlaces
//			List<Place> places = connection.getAllPlaces("new york");
//			System.out.println(places.size());
			
			
   	 	} catch (Exception e) {
   	 		e.printStackTrace();
   	 	} finally {
   	 		if (connection != null) {
   	 			connection.close(); 
   	 		}
   	 	}

	}
>>>>>>> branch 'backend' of https://github.com/haleyhao/TravelPlanner.git
}
