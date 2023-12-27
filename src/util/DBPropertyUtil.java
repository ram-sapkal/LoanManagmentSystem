package util;

import java.io.FileInputStream;
import java.util.Properties;

public class DBPropertyUtil {

	public static String getConnectionString(String propertyFileName) throws Exception{
		
		try {
			
		FileInputStream fileInputStream = new FileInputStream(propertyFileName);
		Properties properties =new Properties();
		properties.load(fileInputStream);
		String url=properties.getProperty("url")	;
		String user=properties.getProperty("user");
		String pass=properties.getProperty("pass");
		return url + "?user=" + user + "&password=" + pass ;

		}catch(Exception e) {
			System.out.println("Exception occured : "+e.getMessage());
		}
		return null;
		
	}

}
