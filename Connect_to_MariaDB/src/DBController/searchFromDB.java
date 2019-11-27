package DBController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class searchFromDB {
	
	public static ResultSet searchObjects(String tableName) throws SQLException {
        Statement stmt = connectToDB.DBStmt();
        ResultSet rs = null;
        
        String sql = "SELECT * from " + tableName;

        rs = stmt.executeQuery(sql);
        
		return rs;
	}
	
}
