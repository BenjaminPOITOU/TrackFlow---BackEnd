package com.eql.cda.track.flow.service.implementation;

import com.eql.cda.track.flow.dto.compositionDto.CompositionCreateDto;
import com.eql.cda.track.flow.dto.compositionDto.CompositionSummaryDto;
import com.eql.cda.track.flow.dto.compositionDto.CompositionUpdateDto;
import com.eql.cda.track.flow.dto.compositionDto.CompositionViewDto;
import com.eql.cda.track.flow.entity.Composition;
import com.eql.cda.track.flow.entity.Project;
import com.eql.cda.track.flow.entity.User;
import com.eql.cda.track.flow.repository.CompositionRepository;
import com.eql.cda.track.flow.repository.ProjectRepository;
import com.eql.cda.track.flow.repository.UserRepository;
import com.eql.cda.track.flow.service.CompositionService;
import com.eql.cda.track.flow.service.mapper.CompositionMapper;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link CompositionService} interface. This class orchestrates
 * all business logic related to compositions, interacting with the persistence layer
 * and handling data mapping.
 */
@Service
@Transactional
public class CompositionServiceImpl implements CompositionService {

    private static final Logger log = LoggerFactory.getLogger(CompositionServiceImpl.class);

    private final CompositionRepository compositionRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CompositionMapper compositionMapper;

    /**
     * Constructs the service with its required dependencies.
     *
     * @param compositionRepository The repository for composition data access.
     * @param projectRepository The repository for project data access.
     * @param userRepository The repository for user data access.
     * @param compositionMapper The mapper for converting between entities and DTOs.
     */
    @Autowired
    public CompositionServiceImpl(CompositionRepository compositionRepository, ProjectRepository projectRepository, UserRepository userRepository, CompositionMapper compositionMapper) {
        this.compositionRepository = compositionRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.compositionMapper = compositionMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompositionViewDto createComposition(Long projectId, CompositionCreateDto compositionCreateDto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));

        Composition composition = compositionMapper.toEntity(compositionCreateDto);
        composition.setProject(project);

        Composition savedComposition = compositionRepository.save(composition);
        log.info("Created composition with ID {} for project ID {}", savedComposition.getId(), projectId);

        return getCompositionById(projectId, savedComposition.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CompositionSummaryDto> findRecentCompositionsForCurrentUser(Pageable pageable) {
        User currentUser = findCurrentUserOrThrow();
        Page<Composition> compositions = compositionRepository.findRecentCompositionsByUserId(currentUser.getId(), pageable);
        return compositions.map(compositionMapper::toSummaryDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<CompositionSummaryDto> getAllCompositionsByProject(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new EntityNotFoundException("Project not found with id: " + projectId);
        }
        List<Composition> compositions = compositionRepository.findByProjectIdOrderByLastUpdateDateDesc(projectId);
        return compositions.stream()
                .map(compositionMapper::toSummaryDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public CompositionViewDto getCompositionById(Long projectId, Long compositionId) {
        Composition composition = findCompositionAndValidateContext(projectId, compositionId);
        return compositionMapper.toViewDto(composition);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompositionViewDto updateComposition(Long projectId, Long compositionId, CompositionUpdateDto compositionUpdateDto) {
        Composition composition = findCompositionAndValidateContext(projectId, compositionId);
        compositionMapper.updateFromDto(compositionUpdateDto, composition);
        Composition updatedComposition = compositionRepository.save(composition);
        log.info("Updated composition with ID {}", updatedComposition.getId());
        return compositionMapper.toViewDto(updatedComposition);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteComposition(Long projectId, Long compositionId) {
        Composition composition = findCompositionAndValidateContext(projectId, compositionId);
        compositionRepository.delete(composition);
        log.info("Deleted composition with ID {}", compositionId);
    }

    private User findCurrentUserOrThrow() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new AccessDeniedException("No authenticated user found.");
        }
        String username = authentication.getName();
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    private Composition findCompositionAndValidateContext(Long projectId, Long compositionId) {
        Composition composition = compositionRepository.findByIdWithProjectAndBranches(compositionId)
                .orElseThrow(() -> new EntityNotFoundException("Composition not found with id: " + compositionId));

        composition.getSubGenders().size();
        if (composition.getProject() != null && composition.getProject().getProjectMusicalGendersPreDefined() != null) {
            composition.getProject().getProjectMusicalGendersPreDefined().size();
        }

        if (!Objects.equals(composition.getProject().getId(), projectId)) {
            log.warn("Access violation: Composition {} does not belong to project {}", compositionId, projectId);
            throw new AccessDeniedException("Composition does not belong to the specified project.");
        }
        return composition;
    }
}