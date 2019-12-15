package DBController;

import java.sql.Statement;

// DB에 정보를 넣는 클래스이다.
public class insertIntoDB {
	
	// Member테이블에 사용자 정보를 넣는다.
	public static boolean addMember(String ID, String Pass) {
		try {
			Statement 	stmt = connectToDB.DBStmt();
			String 		sql = "Insert into `Member` (ID,Password,nowUse,scooterUse) values (\"" + ID + "\",\"" + Pass + "\",\"" + "0" + "\",\"" + "0" + "\")";
			stmt.executeUpdate(sql);
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	// Manager테이블에 매니저 정보를 넣는다.
	public static boolean addManager(String ID, String Pass) {
		try {
			Statement 	stmt = connectToDB.DBStmt();
			String 		sql = "Insert into Manager (ID,Password,nowUse) values (\"" + ID + "\",\"" + Pass + "\",\"" + "0" +"\")";
			stmt.executeUpdate(sql);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// Scooter테이블에 스쿠터 정보를 넣는다.
	public static boolean addScooter(String ID, String Location) {
		try {
			Statement 	stmt = connectToDB.DBStmt();
			String 		sql = "Insert into Scooter (ID,Location,nowUse) values (\"" + ID + "\",\"" + Location + "\",\"" + "0" +"\")";
			stmt.executeUpdate(sql);
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
}
