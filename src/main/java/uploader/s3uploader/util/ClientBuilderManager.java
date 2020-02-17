package uploader.s3uploader.util;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * Builds the AWS clients.
 * 
 * @author theja.kotuwella
 *
 */
public class ClientBuilderManager {
	private static AmazonS3 s3Client 	= null;
	private static int SOCKET_TIMEOUT 	= 60000;
	
	public static AmazonS3 s3Client() {
		if(s3Client == null) {
			try {
				ClientConfiguration clientConfig = new ClientConfiguration();
				clientConfig.setSocketTimeout(SOCKET_TIMEOUT);
				
				s3Client = AmazonS3ClientBuilder.standard()
								.withRegion(Regions.AP_SOUTHEAST_2)
								.withCredentials(getcredentials())
								.build();

			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}

		return s3Client;
	}
	
	private static AWSStaticCredentialsProvider getcredentials() {
		AWSCredentials credentials 	= new BasicAWSCredentials(PropertyManager.ACCESS_KEY_ATTRIBUTE,
																PropertyManager.SECRET_ATTRIBUTE);
		
		AWSStaticCredentialsProvider credProv = new AWSStaticCredentialsProvider(credentials);
		
		return credProv;
	}
}
