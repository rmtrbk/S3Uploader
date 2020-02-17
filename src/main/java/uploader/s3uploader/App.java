package uploader.s3uploader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import uploader.s3uploader.service.S3CrudServiceImpl;
import uploader.s3uploader.util.PropertyManager;

/**
 * Entry point to the application.
 * Main method allows to try out CRUD operations.
 * 
 * @author theja.kotuwella
 *
 */
public class App {
	private static final String srcPath 			= "/Users/theja.kotuwella/Downloads/docs";
	private static final String srcFileName			= "1.pdf";
	private static final String destinationFileName = "test.pdf";
	private static final String author 				= "Theja Kotuwella";
	private static final String isbn 				= "978-1-56619-909-4";
	
	S3CrudServiceImpl s3CrudService = null;
	
	App(){
		initLog();
		
		PropertyManager.loadProperties();
		
		s3CrudService = new S3CrudServiceImpl();
	}
	
	public static void main(String[] arg) {
		App app = new App();
		
		app.create(srcFileName, destinationFileName);
		
		app.createWithMetadata(srcFileName, destinationFileName, author, isbn);
		app.readFileMetadata(destinationFileName);
		
		app.read(destinationFileName);
		app.update(srcFileName, destinationFileName);
		app.delete(destinationFileName);
	}
	
	private void create(String srcFileName, String destFileName) {
		File file = new File(srcPath + File.separator + srcFileName);
		s3CrudService.createFile(file, destFileName);
	}
	
	private void createWithMetadata(String srcFileName, 
										String destFileName,
										String author,
										String isbn) {
		File file = new File(srcPath + File.separator + srcFileName);
		s3CrudService.createFileWithMetadata(file, destFileName, author, isbn);
	}
	
	private List<String> readFileMetadata(String fileName){
		List<String> metadata = s3CrudService.readFileMetadata(fileName);
		
		return metadata;
	}
	
	private void read(String fileName) {
		File download = s3CrudService.readFile(fileName);
		try {
			Files.copy(download.toPath(), Paths.get(srcPath + File.separator + "_" + fileName), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void update(String srcFileName, String destFileName) {
		File file = new File(srcPath + File.separator + srcFileName);
		s3CrudService.updateFile(file, destFileName);
	}
	
	private void delete(String fileName) {
		s3CrudService.deleteFile(fileName);
	}
	
	private static void initLog() {
		Logger rootLogger = Logger.getRootLogger();
		rootLogger.setLevel(Level.INFO);

		PatternLayout layout = new PatternLayout("%d{ISO8601} [%t] %-5p %c %x - %m%n");
		rootLogger.addAppender(new ConsoleAppender(layout));
		
		try {
			RollingFileAppender fileAppender = new RollingFileAppender(layout, "S3Crud.log");

			rootLogger.addAppender(fileAppender);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
