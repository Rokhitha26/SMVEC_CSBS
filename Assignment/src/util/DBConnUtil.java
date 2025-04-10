package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnUtil {
	public static Connection getConnection(String propertyFileName) throws SQLException, FileNotFoundException, IOException {
		Connection conn=null;
		try {
			Properties props=DBPropertyUtil.loadProperties(propertyFileName);
			
			String driver=props.getProperty("db.driver");
			String url= props.getProperty("db.url");
			String username=props.getProperty("db.username");
			String password=props.getProperty("db.password");
			
			Class.forName(driver);
			conn=DriverManager.getConnection(url, username, password);
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		return conn;
	}
}
