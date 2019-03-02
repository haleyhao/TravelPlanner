package db.mysql;

import java.sql.DriverManager;
<<<<<<< HEAD
import java.sql.Statement;
import java.sql.Connection;

public class MySQLTableCreation {
	// Run this as Java application to reset db schema.
	public static void main(String[] args) {
		try {
			// Step 1 Connect to MySQL.
			System.out.println("Connecting to " + MySQLDBUtil.URL);
			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
			Connection conn = DriverManager.getConnection(MySQLDBUtil.URL);
			
			if (conn == null) {
				return;
			}
			
			// Step 2 Drop tables in case they exist.
			Statement statement = conn.createStatement();
			String sql = "DROP TABLE IF EXISTS users";
			statement.executeUpdate(sql);
			
			sql = "CREATE TABLE users ("
					+ "user_id VARCHAR(255) NOT NULL,"
					+ "password VARCHAR(255) NOT NULL,"
					+ "first_name VARCHAR(255),"
					+ "last_name VARCHAR(255),"
					+ "PRIMARY KEY (user_id)"
					+ ")";
			statement.executeUpdate(sql);
			
			sql = "INSERT INTO users VALUES('1111', '2222', 'Jay', 'Chou')";
			statement.executeUpdate(sql);
			
			conn.close();
			System.out.println("Import done successfully");

=======
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.json.JSONArray;

import java.sql.Connection;

public class MySQLTableCreation {
	// Run this as Java application to reset db schema.
	public static void main(String[] args) {
		
		//create a new table to store plan information
		createPlanTable();
		
		//create a new table to store user information
		createUserTable();
		
		//create a new table to store place information
		createPlaceTable();
		
		createUserLogTable();
		
		try {
			// Step 1 Connect to MySQL.
			System.out.println("Connecting to " + MySQLDBUtil.URL);
			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
			Connection conn = DriverManager.getConnection(MySQLDBUtil.URL);
			
			if (conn == null) {
				return;
			}
			
			// Step 2 Drop tables in case they exist.
			Statement statement = conn.createStatement();
			String sql = "DROP TABLE IF EXISTS citys";
			statement.executeUpdate(sql);
			
			// Step 3 Create new tables
			sql = "CREATE TABLE citys ("
					+ "city_id VARCHAR(255) NOT NULL,"
					+ "city_name VARCHAR(255),"
					+ "commingSoon VARCHAR(255),"
					+ "PRIMARY KEY (city_id)"
					+ ")";
			statement.executeUpdate(sql);
			
			// Step 4: insert fake user 1111/3229c1097c00d497a0fd282d586be050
			sql = "INSERT INTO citys VALUES('3229c1097c00d497a0fd282d586be050', 'New York', 'Available'), ('3229c1097c00d497a0fd282d586be051', 'Los Angeles', 'CommingSoon'), ('3229c1097c00d497a0fd282d586be052', 'San Francisco', 'CommingSoon')";
			statement.executeUpdate(sql);			
			
			System.out.println("Import done successfully");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void createPlanTable() {
		
		try {
			// Step 1 Connect to MySQL.
			System.out.println("Connecting to " + MySQLDBUtil.URL);
			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
			Connection conn = DriverManager.getConnection(MySQLDBUtil.URL);
			
			if (conn == null) {
				return;
			}
			
			// Step 2 Drop table plan in case it exists.
			Statement statement = conn.createStatement();
			String sql = "DROP TABLE IF EXISTS plan";
			statement.executeUpdate(sql);

			// Step 3 Create table plan
			sql = "CREATE TABLE plan ("
					+ "plan_id VARCHAR(255) NOT NULL,"
					+ "place_ids VARCHAR(255) NOT NULL,"
					+ "user_id VARCHAR(255) NOT NULL,"
					+ "PRIMARY KEY (plan_id)"
//					+ "FOREIGN KEY (user_id) REFERENCES user(email)"
					+ ")";
			statement.executeUpdate(sql);
			
			// Step 4: insert fake plan: plan_id: 1, user_id: 1, place_ids ["1","2","3"]
			sql = "INSERT INTO plan VALUES('1','[\"ChIJ2aWDJ_hYwokRUKinWgKdI70\",\"ChIJN6W-X_VYwokRTqwcBnTw1Uk\",\"ChIJoxqn-kD2wokRLvhOLfAneU8\"]','1')";
			statement.executeUpdate(sql);
			
			System.out.println("Plan Import done successfully");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void createUserLogTable() {
		try {
			
			// Step 1 Connect to MySQL.
			System.out.println("Connecting to " + MySQLDBUtil.URL);
			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
			Connection conn = DriverManager.getConnection(MySQLDBUtil.URL);
			
			if (conn == null) {
				return;
			}
			
			// Step 2 Drop table users in case it exists.
			Statement statement = conn.createStatement();
			String sql = "DROP TABLE IF EXISTS userlogs";
			statement.executeUpdate(sql);
			
			// Step 3 Create table users
			sql = "CREATE TABLE userlogs ("
					+ "date TIMESTAMP NOT NULL,"
					+ "user_id VARCHAR(255) NOT NULL,"
					+ "event VARCHAR(255) NOT NULL,"
					+ "PRIMARY KEY (date)"
					+ ")";
			statement.executeUpdate(sql);
			
			System.out.println("userlogs Import done successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void createUserTable() {
		try {
			
			// Step 1 Connect to MySQL.
			System.out.println("Connecting to " + MySQLDBUtil.URL);
			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
			Connection conn = DriverManager.getConnection(MySQLDBUtil.URL);
			
			if (conn == null) {
				return;
			}
			
			// Step 2 Drop table users in case it exists.
			Statement statement = conn.createStatement();
			String sql = "DROP TABLE IF EXISTS users";
			statement.executeUpdate(sql);
			
			// Step 3 Create table users
			sql = "CREATE TABLE users ("
					+ "email VARCHAR(255) NOT NULL,"
					+ "password VARCHAR(255) NOT NULL,"
					+ "name VARCHAR(255),"
					+ "PRIMARY KEY (email)"
					+ ")";
			statement.executeUpdate(sql);
			
			// Step 4: insert fake user 1111/3229c1097c00d497a0fd282d586be050
			sql = "INSERT INTO users VALUES('1111', '3229c1097c00d497a0fd282d586be050', 'John Smith')";
			statement.executeUpdate(sql);
			System.out.println("users Import done successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void createPlaceTable() {
		try {
			
			// Step 1 Connect to MySQL.
			System.out.println("Connecting to " + MySQLDBUtil.URL);
			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
			Connection conn = DriverManager.getConnection(MySQLDBUtil.URL);
			
			if (conn == null) {
				return;
			}
			
			// Step 2 Drop table users in case it exists.
			Statement statement = conn.createStatement();
			String sql = "DROP TABLE IF EXISTS places";
			statement.executeUpdate(sql);
			
			// Step 3 Create table users
			sql = "CREATE TABLE places ("
					+ "place_id VARCHAR(255) NOT NULL,"
					+ "name VARCHAR(255),"
					+ "formatted_address VARCHAR(255),"
					+ "city VARCHAR(255),"
					+ "rating FLOAT,"
					+ "total_rated INT,"
					+ "lat FLOAT,"
					+ "lon FLOAT,"
					+ "PRIMARY KEY (place_id)"
					+ ")";
			statement.executeUpdate(sql);
			
			// Step 4: insert fake place (optional)
//			sql = "INSERT INTO places VALUES('1234321', 'fake place', '1 1sr Str.', 'New York', 4.5, 80, 30.5, 60.8)";
//			statement.executeUpdate(sql);
			System.out.println("palces Import done successfully");
>>>>>>> branch 'backend' of https://github.com/haleyhao/TravelPlanner.git
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
