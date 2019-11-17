package DBController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class connectToDB {
	public static Statement DBStmt() {
		Connection 	conn = null;
		Statement 	stmt = null;
		try{
            Class.forName("com.mysql.jdbc.Driver");
            
            String url = "jdbc:mysql://59.27.140.107:3306/term_project_seven";
            conn = DriverManager.getConnection(url, "termSeven", "sevenmillion");

            stmt = conn.createStatement();
        }
        catch( ClassNotFoundException e){
            System.out.println("Driver Loading Failed");
        }
        catch( SQLException e){
            System.out.println("Error " + e);
        }
		
		return stmt;
	}
}
