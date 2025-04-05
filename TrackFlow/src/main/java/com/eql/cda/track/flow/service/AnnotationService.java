package com.eql.cda.track.flow.service;

import com.eql.cda.track.flow.dto.annotationDto.AnnotationCreateDto;
import com.eql.cda.track.flow.dto.annotationDto.AnnotationResponseDto;
import com.eql.cda.track.flow.dto.annotationDto.AnnotationUpdateDto;
import com.eql.cda.track.flow.dto.annotationDto.UserRecentAnnotationDto;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AnnotationService {


    AnnotationResponseDto createAnnotation(AnnotationCreateDto createDto, Long versionId);
    List<AnnotationResponseDto> getAllAnnotationsByVersionIdSorted(Long versionId);
    AnnotationResponseDto getAnnotationById(Long annotationId);
    AnnotationResponseDto updateAnnotation(Long annotationId, AnnotationUpdateDto updateDto);
    void deleteAnnotation(Long annotationId);
    Page<UserRecentAnnotationDto> findRecentAnnotationsForUser(String username, Pageable pageable);

}
