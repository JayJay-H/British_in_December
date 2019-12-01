package DBController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class searchFromDB {
	
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
