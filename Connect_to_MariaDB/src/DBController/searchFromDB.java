package DBController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// DB에 있는 정보를 가져오기 위한 클래스이다.
public class searchFromDB {
	
	// 테이블 이름에 맞는 정보를 ResultSet으로 반환한다.
	public static ResultSet searchObjects(String tableName) throws SQLException {
        Statement stmt = connectToDB.DBStmt();
        ResultSet rs = null;
        String sql; 
        if(tableName.equals("Member")) {
        	sql= "SELECT * from " + "`" +tableName+"`";
        }
        else {
        	sql= "SELECT * from " + tableName;
        }

        rs = stmt.executeQuery(sql);
        
		return rs;
	}
	
}
