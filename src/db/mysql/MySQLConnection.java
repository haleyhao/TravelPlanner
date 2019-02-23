package db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

}
