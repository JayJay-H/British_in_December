package DBController;

import java.sql.Statement;

public class deleteFromDB {

	public static boolean deleteMember(String ID) {
		try {
			
			Statement 	stmt = connectToDB.DBStmt();
	        String sql = "delete from Member where name=\""+ID+"\"";
	        stmt.executeUpdate(sql);
	        
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean deleteManager(String ID) {
		try {
			
			Statement 	stmt = connectToDB.DBStmt();
			String sql = "delete from Manager where name=\""+ID+"\"";
	        stmt.executeUpdate(sql);
	        
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean deleteScooter(String ID) {
		try {
			
			Statement 	stmt = connectToDB.DBStmt();
			String sql = "delete from Scooter where name=\""+ID+"\"";
	        stmt.executeUpdate(sql);
	        
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
