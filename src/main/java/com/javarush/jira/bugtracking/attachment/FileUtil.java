package com.javarush.jira.bugtracking.attachment;

import com.javarush.jira.common.error.IllegalRequestDataException;
import com.javarush.jira.common.error.NotFoundException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@UtilityClass
public class FileUtil {
    private static final String ATTACHMENT_PATH = "./attachments/%s/";

    public static void upload(MultipartFile multipartFile, String directoryPath, String fileName) {
        if (multipartFile.isEmpty()) {
            throw new IllegalRequestDataException("Select a file to upload.");
        }

        try {
            Path dir = Paths.get(directoryPath);
            Files.createDirectories(dir);

            Path targetPath = dir.resolve(fileName).normalize();
            if (!targetPath.startsWith(dir)) {
                throw new IllegalRequestDataException("File name contains invalid path sequence");
            }

            try (var inputStream = multipartFile.getInputStream()) {
                Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ex) {
            log.error("Failed to upload file {}", multipartFile.getOriginalFilename(), ex);
            throw new IllegalRequestDataException("Failed to upload file " + multipartFile.getOriginalFilename());
        }
    }

    public static Resource download(String fileLink) {
        try {
            Path path = Paths.get(fileLink).normalize();
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new IllegalRequestDataException("Failed to download file: resource not readable or doesn't exist");
            }
        } catch (MalformedURLException ex) {
            log.error("File {} not found", fileLink, ex);
            throw new NotFoundException("File " + fileLink + " not found");
        }
    }

    public static void delete(String fileLink) {
        Path path = Paths.get(fileLink).normalize();
        try {
            Files.deleteIfExists(path);
        } catch (IOException ex) {
            log.error("File {} deletion failed", fileLink, ex);
            throw new IllegalRequestDataException("File " + fileLink + " deletion failed.");
        }
    }

    public static String getPath(String titleType) {
        return String.format(ATTACHMENT_PATH, titleType.toLowerCase());
    }
}