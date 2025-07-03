package com.eql.cda.track.flow.service.implementation;

import com.eql.cda.track.flow.dto.versionDto.VersionCreateDto;
import com.eql.cda.track.flow.dto.versionDto.VersionSummaryDto;
import com.eql.cda.track.flow.dto.versionDto.VersionUpdateDto;
import com.eql.cda.track.flow.dto.versionDto.VersionViewDto;
import com.eql.cda.track.flow.entity.Annotation;
import com.eql.cda.track.flow.entity.Branch;
import com.eql.cda.track.flow.entity.Version;
import com.eql.cda.track.flow.repository.AnnotationRepository;
import com.eql.cda.track.flow.repository.BranchRepository;
import com.eql.cda.track.flow.repository.VersionRepository;
import com.eql.cda.track.flow.service.StorageService;
import com.eql.cda.track.flow.service.VersionService;
import com.eql.cda.track.flow.service.mapper.VersionMapper;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link VersionService} interface.
 */
@Service
@Transactional
public class VersionServiceImpl implements VersionService {

    private static final Logger log = LoggerFactory.getLogger(VersionServiceImpl.class);

    private final VersionRepository versionRepository;
    private final BranchRepository branchRepository;
    private final AnnotationRepository annotationRepository;
    private final StorageService storageService;
    private final VersionMapper versionMapper;

    /**
     * Constructs the service with its required dependencies.
     *
     * @param versionRepository Repository for version data access.
     * @param branchRepository Repository for branch data access.
     * @param annotationRepository Repository for annotation data access.
     * @param storageService Service for interacting with file storage.
     * @param versionMapper Mapper for converting between entities and DTOs.
     */
    @Autowired
    public VersionServiceImpl(VersionRepository versionRepository, BranchRepository branchRepository, AnnotationRepository annotationRepository, StorageService storageService, VersionMapper versionMapper) {
        this.versionRepository = versionRepository;
        this.branchRepository = branchRepository;
        this.annotationRepository = annotationRepository;
        this.storageService = storageService;
        this.versionMapper = versionMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VersionViewDto createVersion(Long projectId, Long compositionId, Long branchId, VersionCreateDto versionCreateDto) {
        Branch branch = findBranchAndValidateContext(projectId, compositionId, branchId);
        Version version = versionMapper.toEntity(versionCreateDto);
        version.setBranch(branch);

        resolveAnnotationsFromParent(versionCreateDto.getAnnotationIdsToResolve());
        cloneUnresolvedAnnotationsFromParent(versionCreateDto.getParentVersionId(), version);

        Version savedVersion = versionRepository.save(version);
        log.info("Created version with ID {} in branch {}", savedVersion.getId(), branchId);
        return versionMapper.toViewDto(savedVersion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<VersionSummaryDto> getVersionsByBranch(Long projectId, Long compositionId, Long branchId) {
        findBranchAndValidateContext(projectId, compositionId, branchId);
        List<Version> versions = versionRepository.findByBranchIdOrderByCreatedDateDesc(branchId);
        return versions.stream().map(versionMapper::toSummaryDto).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public VersionViewDto getVersionById(Long projectId, Long compositionId, Long branchId, Long versionId) {
        Version version = findVersionAndValidateContext(projectId, compositionId, branchId, versionId, true);
        return versionMapper.toViewDto(version);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<VersionViewDto> getLatestVersionByBranch(Long projectId, Long compositionId, Long branchId) {
        findBranchAndValidateContext(projectId, compositionId, branchId);
        return versionRepository.findFirstByBranchIdOrderByCreatedDateDesc(branchId)
                .map(versionMapper::toViewDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VersionViewDto updateVersion(Long projectId, Long compositionId, Long branchId, Long versionId, VersionUpdateDto versionUpdateDto) {
        Version version = findVersionAndValidateContext(projectId, compositionId, branchId, versionId, false);
        versionMapper.updateFromDto(versionUpdateDto, version);
        Version updatedVersion = versionRepository.save(version);
        log.info("Updated version with ID {}", updatedVersion.getId());
        return versionMapper.toViewDto(updatedVersion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteVersion(Long projectId, Long compositionId, Long branchId, Long versionId) {
        Version version = findVersionAndValidateContext(projectId, compositionId, branchId, versionId, false);
        deleteAssociatedStorageFile(version);
        versionRepository.delete(version);
        log.info("Deleted version with ID {}", versionId);
    }

    private void deleteAssociatedStorageFile(Version version) {
        String audioFileUrl = version.getAudioFileUrl();
        if (audioFileUrl == null || audioFileUrl.isBlank()) {
            log.warn("Version ID {} has no associated file to delete.", version.getId());
            return;
        }
        try {
            String objectPath = storageService.extractObjectPathFromUrl(audioFileUrl);
            if(objectPath != null) {
                storageService.deleteFile(objectPath);
                log.info("Successfully deleted stored file {} for version ID {}.", objectPath, version.getId());
            } else {
                log.warn("Could not extract object path from URL: {}. File may be orphaned.", audioFileUrl);
            }
        } catch (Exception e) {
            log.error("Failed to delete stored file for URL {}. The database entry will be removed, but the file may be orphaned.", audioFileUrl, e);
        }
    }

    private void resolveAnnotationsFromParent(List<Long> annotationIdsToResolve) {
        if (annotationIdsToResolve == null || annotationIdsToResolve.isEmpty()) {
            return;
        }
        List<Annotation> annotations = annotationRepository.findAllById(annotationIdsToResolve);
        annotations.forEach(anno -> anno.setResolved(true));
        annotationRepository.saveAll(annotations);
    }

    private void cloneUnresolvedAnnotationsFromParent(Long parentVersionId, Version newVersion) {
        if (parentVersionId == null) {
            return;
        }
        List<Annotation> parentAnnotations = annotationRepository.findByVersionIdAndIsResolvedFalse(parentVersionId);
        List<Annotation> clonedAnnotations = new ArrayList<>();
        for (Annotation parentAnno : parentAnnotations) {
            Annotation clone = new Annotation();
            clone.setVersion(newVersion);
            clone.setContent(parentAnno.getContent());
            clone.setAnnotationCategory(parentAnno.getAnnotationCategory());
            clone.setTimePosition(parentAnno.getTimePosition());
            clone.setAnnotationStatus(parentAnno.getAnnotationStatus());
            clone.setResolved(false);
            clonedAnnotations.add(clone);
        }
        newVersion.getAnnotations().addAll(clonedAnnotations);
    }

    private Branch findBranchAndValidateContext(Long projectId, Long compositionId, Long branchId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with id: " + branchId));
        if (branch.getComposition() == null || !Objects.equals(branch.getComposition().getId(), compositionId)) {
            throw new AccessDeniedException("Branch " + branchId + " does not belong to composition " + compositionId);
        }
        if (branch.getComposition().getProject() == null || !Objects.equals(branch.getComposition().getProject().getId(), projectId)) {
            throw new AccessDeniedException("Composition " + compositionId + " does not belong to project " + projectId);
        }
        return branch;
    }

    private Version findVersionAndValidateContext(Long projectId, Long compositionId, Long branchId, Long versionId, boolean fetchDetails) {
        Version version = (fetchDetails ? versionRepository.findByIdWithDetails(versionId) : versionRepository.findById(versionId))
                .orElseThrow(() -> new EntityNotFoundException("Version not found with id: " + versionId));
        if (version.getBranch() == null || !Objects.equals(version.getBranch().getId(), branchId)) {
            throw new AccessDeniedException("Version " + versionId + " does not belong to branch " + branchId);
        }
        findBranchAndValidateContext(projectId, compositionId, branchId);
        return version;
    }
}