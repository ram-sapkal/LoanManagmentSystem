package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnUtil {
	
	public static Connection getDBConn(String connectionString) throws Exception {
	
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			return DriverManager.getConnection(connectionString);
		}catch(Exception e) {
			System.out.println("Exception Occured : "+e.getMessage());
		}
		return null;
		}
				
}
