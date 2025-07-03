package com.eql.cda.track.flow.service;

import com.eql.cda.track.flow.dto.audioUploadDto.AudioUploadResponseDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for orchestrating file uploads and metadata extraction.
 */
public interface UploadService {

    /**
     * Handles the upload of an audio file to the storage provider, extracts its metadata,
     * and returns a consolidated response. This method acts as a facade over the
     * StorageService and metadata extraction logic.
     *
     * @param file The audio file to upload, received from a multipart request.
     * @param compositionId The ID of the composition to which this audio belongs, used for creating a structured storage path.
     * @return An {@link AudioUploadResponseDto} containing the unique file name (for database reference) and extracted metadata.
     * @throws Exception if the upload, file handling, or metadata extraction fails.
     */
    AudioUploadResponseDto uploadAudioAndExtractMetadata(MultipartFile file, Long compositionId) throws Exception;
}