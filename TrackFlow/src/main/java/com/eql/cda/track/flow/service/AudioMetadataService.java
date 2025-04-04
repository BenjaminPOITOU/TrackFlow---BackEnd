package com.eql.cda.track.flow.service;

import com.eql.cda.track.flow.dto.AudioMetadataDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface AudioMetadataService {


    /**
     * Extracts metadata (like BPM and duration) from an audio file.
     * @param file The audio file.
     * @return An AudioMetadata object containing the extracted info.
     * @throws Exception If metadata extraction fails (parsing error, unsupported format, etc.).
     */
    AudioMetadataDto extractMetadata(MultipartFile file) throws Exception;

}
