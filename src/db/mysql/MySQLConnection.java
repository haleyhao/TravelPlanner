package db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;

import com.mysql.cj.jdbc.Driver;

import db.DBConnection;


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
		// TODO Auto-generated method stub
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
	public String getIfCommingSoon(String cityId) {
		if (connection == null) {
			return "";
		}
		
		String ifCommingSoon = "";
		try {
			String sql = "SELECT commingSoon FROM citys WHERE city_id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, cityId);
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
			ps.setString(1, planId);
			ps.setString(2, placeIdArray.toString());
			ps.setString(3, userId);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}	

	}
}
