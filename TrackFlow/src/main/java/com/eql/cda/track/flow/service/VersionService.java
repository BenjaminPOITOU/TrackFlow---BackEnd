package com.eql.cda.track.flow.service;

import com.eql.cda.track.flow.dto.versionDto.AudioUploadResponseDto;
import com.eql.cda.track.flow.dto.versionDto.NewVersionModalDto;
import com.eql.cda.track.flow.dto.versionDto.VersionCreateDto;
import com.eql.cda.track.flow.dto.versionDto.VersionDetailDto;
import com.eql.cda.track.flow.dto.versionDto.VersionResponseDto;
import com.eql.cda.track.flow.dto.versionDto.VersionSummaryDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface VersionService {

    VersionResponseDto createVersion(Long compositionId, VersionCreateDto dto);
    void deleteVersion(Long versionId);
    AudioUploadResponseDto uploadAudioAndExtractMetadata(MultipartFile file, String desiredPathPrefix) throws Exception;
    NewVersionModalDto prepareNewVersionModalData(Long compositionId, Optional<Long> basedOnVersionId);
    VersionDetailDto getVersionDetailsById(Long versionId);
    List<VersionSummaryDto> getVersionsForComposition(Long compositionId, Optional<Long> branchId);
    Optional<VersionResponseDto> findLatestVersionForUser(String username);

}
