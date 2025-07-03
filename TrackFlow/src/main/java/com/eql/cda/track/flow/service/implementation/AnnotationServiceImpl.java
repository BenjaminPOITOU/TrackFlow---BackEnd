package com.eql.cda.track.flow.service.implementation;

import com.eql.cda.track.flow.dto.annotationDto.AnnotationCreateDto;
import com.eql.cda.track.flow.dto.annotationDto.AnnotationUpdateDto;
import com.eql.cda.track.flow.dto.annotationDto.AnnotationViewDto;
import com.eql.cda.track.flow.entity.Annotation;
import com.eql.cda.track.flow.entity.Version;
import com.eql.cda.track.flow.exception.ResourceNotFoundException;
import com.eql.cda.track.flow.repository.AnnotationRepository;
import com.eql.cda.track.flow.repository.VersionRepository;
import com.eql.cda.track.flow.service.AnnotationService;
import com.eql.cda.track.flow.service.mapper.AnnotationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for managing {@link Annotation} entities.
 */
@Service
@Transactional
public class AnnotationServiceImpl implements AnnotationService {

    private final AnnotationRepository annotationRepository;
    private final VersionRepository versionRepository;
    private final AnnotationMapper annotationMapper;

    /**
     * Constructs an AnnotationServiceImpl with the required dependencies.
     *
     * @param annotationRepository the repository for annotation data access.
     * @param versionRepository    the repository for version data access.
     * @param annotationMapper     the mapper for converting between entities and DTOs.
     */
    public AnnotationServiceImpl(AnnotationRepository annotationRepository, VersionRepository versionRepository, AnnotationMapper annotationMapper) {
        this.annotationRepository = annotationRepository;
        this.versionRepository = versionRepository;
        this.annotationMapper = annotationMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationViewDto createAnnotation(Long versionId, AnnotationCreateDto createDto) {
        Version version = findVersionById(versionId);
        Annotation newAnnotation = annotationMapper.fromCreateDto(createDto, version);
        Annotation savedAnnotation = annotationRepository.save(newAnnotation);
        return annotationMapper.toViewDto(savedAnnotation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<AnnotationViewDto> findAllAnnotationsByVersionId(Long versionId) {
        if (!versionRepository.existsById(versionId)) {
            throw new ResourceNotFoundException("Version", versionId);
        }
        List<Annotation> annotations = annotationRepository.findAllByVersionIdOrderByCreationDateDesc(versionId);

        return annotations.stream()
                .map(annotationMapper::toViewDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationViewDto updateAnnotation(Long annotationId, AnnotationUpdateDto updateDto) {
        Annotation annotationToUpdate = findAnnotationById(annotationId);
        annotationMapper.updateFromDto(updateDto, annotationToUpdate);
        return annotationMapper.toViewDto(annotationToUpdate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAnnotation(Long annotationId) {
        Annotation annotationToDelete = findAnnotationById(annotationId);
        annotationToDelete.setSupressionDate(Instant.now());
        annotationRepository.save(annotationToDelete);
    }



    private Version findVersionById(Long versionId) {
        return versionRepository.findById(versionId)
                .orElseThrow(() -> new ResourceNotFoundException("Version", versionId));
    }

    private Annotation findAnnotationById(Long annotationId) {
        return annotationRepository.findById(annotationId)
                .orElseThrow(() -> new ResourceNotFoundException("Annotation", annotationId));
    }
}