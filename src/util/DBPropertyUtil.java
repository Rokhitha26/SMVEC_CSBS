package util;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
public class DBPropertyUtil {
public static Properties loadProperties(String filename) throws FileNotFoundException, IOException {
	
	Properties props= new Properties();
	
	try(FileInputStream fis= new FileInputStream(filename)){
		props.load(fis);
		
	}catch(IOException e) {
		e.printStackTrace();
	}
	return props;
}
}
