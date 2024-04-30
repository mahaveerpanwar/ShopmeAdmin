package com.shopme.admin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
	
	private static final Logger logger= LoggerFactory.getLogger(FileUploadUtil.class);

	public static void saveFile(String uploaddir, String fileName, MultipartFile multipartFile) throws IOException {

		Path uploadPath = Paths.get(uploaddir);

		if (!Files.exists(uploadPath)) {

			Files.createDirectories(uploadPath);
		}

		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filepath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filepath, StandardCopyOption.REPLACE_EXISTING);

		} catch (Exception e) {

			throw new IOException("Could not save File" + fileName, e);

		}

	}

	public static void cleanDirect(String dir) {
		Path dirpath = Paths.get(dir);

		try {
			Files.list(dirpath).forEach(file -> {

				if (!Files.isDirectory(file)) {
					try {
						Files.delete(file);
					} catch (Exception e) {
//						System.out.println("Could not delete the file" + file);
						logger.error("Could not delete the file" + file);
					}

				}
			});
		} catch (Exception e) {
//			System.out.println("Could not delete the file" + dirpath);
			logger.error("Could not list the directory" + dirpath);
		}

	}

}
