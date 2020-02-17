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
	public void createFile(File src, String destinationFileName);
	public void createFileWithMetadata(File src, String destinationFileName,
										String author, String isbn);
	
	public File readFile(String filename);
	public List<String> readFileMetadata(String filename);
	
	public void updateFile(File src, String destinationFileName);
	public void deleteFile(String filename);
}
