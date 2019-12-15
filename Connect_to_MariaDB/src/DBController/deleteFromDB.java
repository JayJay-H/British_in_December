package DBController;

import java.sql.Statement;

// DB에서 정보를 삭제하기 위한 클래스이다.
public class deleteFromDB {
	
	// Member테이블에서 해당 ID를 삭제한다.
	public static boolean deleteMember(String ID) {
		try {
			
			Statement 	stmt = connectToDB.DBStmt();
	        String sql = "delete from `Member` where ID=\""+ID+"\"";
	        stmt.executeUpdate(sql);
	        
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// Manager테이블에서 해당 ID를 삭제한다.
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
	
	// Scooter테이블에서 해당 ID를 삭제한다.
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
