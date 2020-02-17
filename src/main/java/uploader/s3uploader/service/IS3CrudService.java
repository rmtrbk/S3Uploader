package uploader.s3uploader.service;

import java.io.File;
import java.util.List;

/**
 * CRUD services interface.
 * 
 * @author theja.kotuwella
 *
 */
public interface IS3CrudService {
	void createFile(File src, String destinationFileName);
	void createFileWithMetadata(File src, String destinationFileName,
										String author, String isbn);
	
	File readFile(String filename);
	List<String> readFileMetadata(String filename);
	
	void updateFile(File src, String destinationFileName);
	void deleteFile(String filename);
}
