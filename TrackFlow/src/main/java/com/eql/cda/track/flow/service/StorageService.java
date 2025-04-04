package com.eql.cda.track.flow.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {

    /**
     * Uploads a file to the configured storage.
     * @param file The file received from the request.
     * @param destinationFileName The desired name for the file in the storage bucket (could include paths like 'audio/track1.mp3'). Ensure uniqueness if necessary.
     * @return The public URL or identifier of the uploaded file.
     * @throws IOException If an error occurs during upload.
     */
    String uploadFile(MultipartFile file, String destinationFileName) throws IOException;
    String extractObjectPathFromUrl(String fileUrl) throws java.net.URISyntaxException;
    String generateUniqueFileName(String originalFileName);
    boolean deleteFile(String objectPath);

}

