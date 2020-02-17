package uploader.s3uploader.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads the properties in the properties file.
 * 
 * @author theja.kotuwella
 *
 */
public class PropertyManager {
	private final String PROPERTIES_FILE 			= "/Users/theja.kotuwella/Wellcom/Theja/s3uploader/s3uploader/src/main/resources/properties";	
	private static final String REGION_PROP 		= "aws.region";
	private static final String ACCESS_KEY_PROP 	= "aws.accessKey";
	private static final String SECRET_PROP 		= "aws.secret";
	private static final String S3_PROP 			= "aws.s3";
	
	public static String REGION_ATTRIBUTE 			= null;
	public static String ACCESS_KEY_ATTRIBUTE 		= null;
	public static String SECRET_ATTRIBUTE 			= null;
	public static String S3_ATTRIBUTE 				= null;
	
	private Properties readPropertiesFile() {
		Properties prop = new Properties();
		
		try (InputStream input = new FileInputStream(PROPERTIES_FILE)) {
            prop.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
		
		return prop;
	}
	
	public static void loadProperties() {
		PropertyManager propertyLoader = new PropertyManager();
		Properties prop = propertyLoader.readPropertiesFile();
		
		REGION_ATTRIBUTE 		= prop.getProperty(REGION_PROP);
		ACCESS_KEY_ATTRIBUTE 	= prop.getProperty(ACCESS_KEY_PROP);
		SECRET_ATTRIBUTE 		= prop.getProperty(SECRET_PROP);
		S3_ATTRIBUTE 			= prop.getProperty(S3_PROP);
	}
}
