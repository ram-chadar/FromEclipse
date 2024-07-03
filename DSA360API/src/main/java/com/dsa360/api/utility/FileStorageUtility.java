package com.dsa360.api.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dsa360.api.exceptions.SomethingWentWrongException;

@Service
public class FileStorageUtility {

	private final Path rootLocation;

	public FileStorageUtility() {
		this.rootLocation = Paths.get("kyc-docs/agent/").toAbsolutePath().normalize();
	}

	public List<Path> storeFiles(String dsaRegistrationId, MultipartFile... files) {
		Path targetDir = this.rootLocation.resolve(dsaRegistrationId);
		List<Path> storedFilePaths = new ArrayList<>();

		try {
			// Delete the directory if it exists
	        if (Files.exists(targetDir)) {
	            Files.walk(targetDir)
	                .sorted(Comparator.reverseOrder())
	                .map(Path::toFile)
	                .forEach(File::delete);
	        }

	        // Create a new directory
	        Files.createDirectories(targetDir);

			// Save each file
			for (MultipartFile file : files) {
				String fileName = file.getOriginalFilename();
				if (fileName != null && fileName.contains("..")) {
					throw new SomethingWentWrongException("Invalid path sequence in file name: " + fileName);
				}

				Path targetPath = targetDir.resolve(fileName).normalize();
				Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
				storedFilePaths.add(targetPath);
			}
		} catch (IOException e) {
			// Rollback if any error occurs
			for (Path path : storedFilePaths) {
				try {
					Files.deleteIfExists(path);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			throw new SomethingWentWrongException("Failed to store files. Transaction rolled back.", e);
		}
		return storedFilePaths;
	}
}
