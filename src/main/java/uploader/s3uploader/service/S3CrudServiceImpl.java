package uploader.s3uploader.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import uploader.s3uploader.util.ClientBuilderManager;
import uploader.s3uploader.util.PropertyManager;

/**
 * Handles basic operations of saving, reading, deleting, updating, and reading metadata of 
 * files in S3.
 *  
 * @author theja.kotuwella
 *
 */
public class S3CrudServiceImpl implements IS3CrudService {
	private static Logger log = Logger.getLogger(S3CrudServiceImpl.class.getName());
	
	private static final String AUTHOR_METADATA_ATTRIBUTE 		= "author";
	private static final String ISBN_METADATA_ATTRIBUTE 		= "isbn";
	private static final String CONTENT_TYPE					= "application/x-octet-stream";
	private static final int NOT_FOUND_STATUS_CODE				= 404;
	
	/**
	 * Saves a given file with the given name in S3
	 * 
	 * @param src Source file to be saved
	 * @param destinationFileName Name of the file that is saved
	 */
	@Override
	public void createFile(File src, String destinationFileName) {
		if(!doesObjectExist(destinationFileName)) {
			PutObjectRequest objectRequest = createPutObjectRequest(src, destinationFileName);

			ClientBuilderManager.s3Client().putObject(objectRequest);
		
			log.info("S3CrudServiceImpl: File " + destinationFileName + " saved in S3");
		}
	}

	/**
	 * Returns a File object from S3 with the given name
	 * 
	 * @param fileName Name of the file to be read
	 * @return File object of the file saved in S3
	 * @throws IOException
	 */
	@Override
	public File readFile(String fileName) {
		File downloadedFile = null;

		try {

			if(doesObjectExist(fileName)) {
				GetObjectRequest objectRequest = new GetObjectRequest(PropertyManager.S3_ATTRIBUTE, 
						fileName);

				S3Object fullObject = ClientBuilderManager.s3Client().getObject(objectRequest);


				downloadedFile = File.createTempFile(getFileName(fileName), 
						getFileExtension(fileName));


				Files.copy(fullObject.getObjectContent(), 
						downloadedFile.toPath(), 
						StandardCopyOption.REPLACE_EXISTING);

				log.info("S3CrudServiceImpl: File " + fileName + " downloaded");
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}
		return downloadedFile;
	}

	/**
	 * Replaces an existing file in S3 with the given name
	 * 
	 * @param src Source file that is replacing the existing file in S3
	 * @param destinationfileName Name of the file that needs to be replaced
	 */
	@Override
	public void updateFile(File src, String destinationFileName) {
		if(doesObjectExist(destinationFileName)) {
			deleteFile(destinationFileName);
		}
		
		log.info("S3CrudServiceImpl: Updating file " + destinationFileName);
		createFile(src, destinationFileName);
	}

	/**
	 * Deletes a file in S3 by the given name
	 * 
	 * @param fileName Name of the file that needs to be deleted in S3
	 */
	@Override
	public void deleteFile(String fileName) {
		if(doesObjectExist(fileName)) {
			ClientBuilderManager.s3Client().deleteObject(PropertyManager.S3_ATTRIBUTE, fileName);
		}
		
		log.info("S3CrudServiceImpl: File " + fileName + " deleted");
	}
	
	/**
	 * Saves a file in S3 with the given name plus metadata 'author' and 'isbn'
	 * 
	 * @param src File that needs to be saved in S3
	 * @param destinationFileName Name of the file that is being saved in S3
	 * @param author Metadata author
	 * @param isbn Metadata isbn
	 */
	@Override
	public void createFileWithMetadata(File src, 
										String destinationFileName,
										String author,
										String isbn) {
		if(!doesObjectExist(destinationFileName)) {
			PutObjectRequest objectRequest = createPutObjectRequest(src, destinationFileName);

			ObjectMetadata metadata = createMetadta(author, isbn);

			objectRequest = objectRequest.withMetadata(metadata);

			ClientBuilderManager.s3Client().putObject(objectRequest);
		}
		
		log.info("S3CrudServiceImpl: Created file " + destinationFileName + " with metadata");
	}
	
	/**
	 * Reads metadata 'author' and 'isbn' from given file in S3
	 * 
	 * @param fileName Name of the file in which metadata is read
	 * @return List of metadata from the given file in S3
	 */
	@Override
	public List<String> readFileMetadata(String fileName) {
		List<String> metadataList = new ArrayList<>();
		
		if(doesObjectExist(fileName)) {
			GetObjectRequest objectRequest 	= new GetObjectRequest(PropertyManager.S3_ATTRIBUTE, fileName);
			S3Object fullObject 			= ClientBuilderManager.s3Client().getObject(objectRequest);
			
			ObjectMetadata metadata = fullObject.getObjectMetadata();
			
			String author 	= metadata.getUserMetaDataOf(AUTHOR_METADATA_ATTRIBUTE);
			String isbn 	= metadata.getUserMetaDataOf(ISBN_METADATA_ATTRIBUTE);
			
			metadataList.add(AUTHOR_METADATA_ATTRIBUTE + ":" + author);
			metadataList.add(ISBN_METADATA_ATTRIBUTE + ":" + isbn);
			
			log.info("S3CrudServiceImpl: Reading metadata author: " + author);
			log.info("S3CrudServiceImpl: Reading metadata isbn: " + isbn);
		} 
		return metadataList;
	}
	
	private String getFileName(String fileNameWithExtension) {
		String fileName = fileNameWithExtension;
		
		if(fileNameWithExtension.contains(".")) {
			fileName = fileNameWithExtension.substring(0, fileNameWithExtension.lastIndexOf("."));
		}
		
		log.info("S3CrudServiceImpl: Extracted fileName: " + fileName);
		return fileName;
	}
	
	private String getFileExtension(String fileNameWithExtension) {
		String extension = "";
		
		if(fileNameWithExtension.contains(".")) {
			extension = fileNameWithExtension.substring(fileNameWithExtension.lastIndexOf(".") + 1);
		}
		
		log.info("S3CrudServiceImpl: Extracted file extension: " + extension);
		return extension;
	}
	
	private PutObjectRequest createPutObjectRequest(File src, String destinationFileName) {
		PutObjectRequest objectRequest = new PutObjectRequest(PropertyManager.S3_ATTRIBUTE,  
																destinationFileName, 
																src).withCannedAcl(CannedAccessControlList.PublicRead);
		
		log.debug("S3CrudServiceImpl: PutObjectRequest created");
		return objectRequest;
	}
	
	private ObjectMetadata createMetadta(String author, String isbn) {
		ObjectMetadata objectMetadata = new ObjectMetadata();
    	
    	objectMetadata.addUserMetadata(AUTHOR_METADATA_ATTRIBUTE, author);
    	objectMetadata.addUserMetadata(ISBN_METADATA_ATTRIBUTE, isbn);
    	
    	objectMetadata.setContentType(CONTENT_TYPE);
    	
    	log.debug("S3CrudServiceImpl: ObjectMetadata created");
    	return objectMetadata;
	}
	
	private boolean doesObjectExist(String fileName) {
		try {
			ObjectMetadata object = ClientBuilderManager.s3Client().getObjectMetadata(PropertyManager.S3_ATTRIBUTE, fileName);
			object.getETag();

		} catch(AmazonServiceException e) {
			int errorCode = e.getStatusCode();

			if (errorCode != NOT_FOUND_STATUS_CODE) {
				throw e;
			}

			log.debug("S3CrudServiceImpl: Object doesn't exist in S3");
			return false;
		}
		
		log.debug("S3CrudServiceImpl: Object already exists in S3");
		return true;
	}
}
