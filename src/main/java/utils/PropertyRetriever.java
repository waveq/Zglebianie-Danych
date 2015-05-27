package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Szymon on 2015-05-23.
 */
public class PropertyRetriever {

	public static String getProperty(String propertyKey) {
		try {
			InputStream is = new FileInputStream("config.properties");
			java.util.Properties prop = new java.util.Properties();
			prop.load(is);
			return prop.getProperty(propertyKey);
		} catch (Exception e) {
			System.out.println("Probably there is no such property " + propertyKey);
			return null;
		}
	}
}
