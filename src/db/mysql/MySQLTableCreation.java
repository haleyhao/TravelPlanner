package db.mysql;

import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Connection;

public class MySQLTableCreation {
	// Run this as Java application to reset db schema.
	public static void main(String[] args) {
		
		//create a new table to store plan information
		createPlanTable();
		
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
					+ "plance_ids BLOB(1024),"
					+ "user_id VARCHAR(255) NOT NULL,"
					+ "PRIMARY KEY (plan_id),"
					+ ")";
			statement.executeUpdate(sql);
			
			System.out.println("Import done successfully");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
