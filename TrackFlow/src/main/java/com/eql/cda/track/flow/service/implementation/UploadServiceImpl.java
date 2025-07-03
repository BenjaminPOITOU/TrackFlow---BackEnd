package com.eql.cda.track.flow.service.implementation;

import com.eql.cda.track.flow.dto.audioUploadDto.AudioUploadResponseDto;
import com.eql.cda.track.flow.service.StorageService;
import com.eql.cda.track.flow.service.UploadService;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class UploadServiceImpl implements UploadService {

    private static final Logger log = LoggerFactory.getLogger(UploadServiceImpl.class);

    private final StorageService storageService;

    @Autowired
    public UploadServiceImpl(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * Implementation of the {@link UploadService} interface.
     * This class orchestrates the process of uploading a file to a storage provider
     * and extracting its audio metadata in a single operation.
     */
    @Override
    public AudioUploadResponseDto uploadAudioAndExtractMetadata(MultipartFile file, Long compositionId) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Cannot process a null or empty file.");
        }

        log.info("Starting audio upload and metadata extraction for file: {} (Composition ID: {})", file.getOriginalFilename(), compositionId);

        String uniqueFileName = generateUniqueFileName(file.getOriginalFilename());
        String destinationPath = String.format("compositions/%d/versions/%s", compositionId, uniqueFileName);

        String fullPublicUrl = storageService.uploadFile(file, destinationPath);
        log.info("File successfully uploaded. Public URL: {}", fullPublicUrl);

        File tempFile = null;
        try {
            tempFile = convertMultiPartToFile(file);

            AudioFile audioFile = AudioFileIO.read(tempFile);
            Tag tag = audioFile.getTag();
            AudioHeader header = audioFile.getAudioHeader();

            String bpm = null;
            if (tag != null) {
                try {
                    String bpmStr = tag.getFirst(FieldKey.BPM);
                    if (bpmStr != null && !bpmStr.isBlank()) {
                        bpm = bpmStr;
                    }
                } catch (UnsupportedOperationException e) {
                    log.warn("Could not read BPM tag: {}", e.getMessage());
                }
            }

            Integer durationSeconds = null;
            if (header != null) {
                durationSeconds = header.getTrackLength();
            }

            AudioUploadResponseDto responseDto = new AudioUploadResponseDto();
            responseDto.setFullUrl(fullPublicUrl);
            responseDto.setBpm(bpm);
            responseDto.setDurationSeconds(durationSeconds);

            log.info("Metadata extracted: BPM={}, Duration={}s. Returning response with full URL.", bpm, durationSeconds);
            return responseDto;

        } finally {
            if (tempFile != null && tempFile.exists()) {
                if (!tempFile.delete()) {
                    log.error("CRITICAL: Could not delete temporary file: {}", tempFile.getAbsolutePath());
                }
            }
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = File.createTempFile("audio_", "_" + file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }

    private String generateUniqueFileName(String originalFileName) {
        String extension = "";
        int i = originalFileName.lastIndexOf('.');
        if (i > 0) {
            extension = originalFileName.substring(i);
        }
        return UUID.randomUUID().toString() + extension;
    }
}