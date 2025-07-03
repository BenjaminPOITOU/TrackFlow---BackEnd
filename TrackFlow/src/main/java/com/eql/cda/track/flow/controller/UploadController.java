package com.eql.cda.track.flow.controller;

import com.eql.cda.track.flow.dto.audioUploadDto.AudioUploadResponseDto;
import com.eql.cda.track.flow.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Handles file upload requests.
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/uploads")
public class UploadController {

    private final UploadService uploadService;

    @Autowired
    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    /**
     * Uploads an audio file for a composition. The file is stored and its metadata extracted.
     * The response contains the relative path (unique filename) and metadata for a subsequent "create version" call.
     *
     * @param file The audio file sent as multipart/form-data.
     * @param compositionId The context of the composition for storage path organization.
     * @return A {@link ResponseEntity} containing the upload result.
     * @throws Exception if the process fails.
     */
    @PostMapping("/audio")
    public ResponseEntity<AudioUploadResponseDto> uploadAudio(
            @RequestParam("file") MultipartFile file,
            @RequestParam("compositionId") Long compositionId) throws Exception {
        AudioUploadResponseDto response = uploadService.uploadAudioAndExtractMetadata(file, compositionId);
        return ResponseEntity.ok(response);
    }
}