package db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	public void deletePlan(String user_id, String plan_id) {
		if (connection == null) {
			System.err.println("DS connection failed");
			return ;
		}
		
		try {
			String sql = "DELETE FROM plan WHERE plan_id = ? AND user_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, plan_id);
			ps.setString(2, user_id);
			ps.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
}
