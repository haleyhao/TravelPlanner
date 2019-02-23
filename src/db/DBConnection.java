package db;

public interface DBConnection {
	
	public void close();
	
	public boolean verifyLogin(String userId, String password);
	
	public void register(String userId, String password, String firstname, String lastname);
	
	public String getFullname(String userId);
}
