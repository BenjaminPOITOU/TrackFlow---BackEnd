package com.eql.cda.track.flow.service.implementation;


import com.eql.cda.track.flow.dto.projectDto.ProjectCreateDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectSummaryDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectUpdateDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectViewDto;
import com.eql.cda.track.flow.service.mapper.ProjectMapper;
import com.eql.cda.track.flow.entity.Project;
import com.eql.cda.track.flow.entity.User;
import com.eql.cda.track.flow.repository.ProjectRepository;
import com.eql.cda.track.flow.repository.UserRepository;
import com.eql.cda.track.flow.service.ProjectService;
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

import java.time.Instant;
import java.util.Objects;

/**
 * Implementation of the ProjectService interface.
 */
@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private static final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;




    /**
     * Constructs the service with its required dependencies.
     */
    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectMapper = projectMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectViewDto createProject(Long userId, ProjectCreateDto projectCreateDto) {
        log.debug("Attempting to create project for user ID: {}", userId);
        User user = findUserByIdOrThrow(userId);

        Project project = projectMapper.toEntity(projectCreateDto);
        project.setUser(user);
        project.setCreatedDate(Instant.now());
        project.setLastUpdateDate(Instant.now());
        project.setArchived(false);

        Project savedProject = projectRepository.save(project);
        log.info("Successfully created project with ID: {} for user ID: {}", savedProject.getId(), userId);
        return projectMapper.toProjectViewDto(savedProject);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public ProjectViewDto getProjectByIdAndUser(Long projectId, Long userId) {
        log.debug("Fetching project ID: {} for user ID: {}", projectId, userId);
        Project project = findProjectByIdOrThrow(projectId);
        validateProjectOwnership(project, userId);
        return projectMapper.toProjectViewDto(project);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectViewDto> getAllProjectsPaginated(Long userId, Pageable pageable) {
        log.debug("Fetching active paginated projects for user ID: {}", userId);
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }
        Page<Project> projectPage = projectRepository.findByUserIdAndArchivedFalse(userId, pageable);
        return projectPage.map(projectMapper::toProjectViewDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectSummaryDto> findRecentProjectsForCurrentUser(Pageable pageable) {
        log.debug("Finding recent projects for current authenticated user");
        String username = getCurrentUsername();
        User currentUser = userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        Page<Project> recentProjects = projectRepository.findByUserIdOrderByCreatedDateDesc(currentUser.getId(), pageable);
        return recentProjects.map(projectMapper::toProjectSummaryDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateProjectForUser(Long projectId, Long userId, ProjectUpdateDto projectUpdateDto) {
        log.debug("Updating project ID: {} for user ID: {}", projectId, userId);
        Project projectToUpdate = findProjectByIdAndValidateOwnership(projectId, userId);

        projectMapper.updateProjectFromDto(projectUpdateDto, projectToUpdate);
        projectToUpdate.setLastUpdateDate(Instant.now());

        projectRepository.save(projectToUpdate);
        log.info("Project ID {} updated successfully by user ID {}", projectId, userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void archiveProjectForUser(Long projectId, Long userId) {
        log.debug("Archiving project ID: {} for user ID: {}", projectId, userId);
        Project projectToArchive = findProjectByIdAndValidateOwnership(projectId, userId);

        if (projectToArchive.isArchived()) {
            throw new IllegalStateException("Project with id " + projectId + " is already archived.");
        }

        projectToArchive.setArchived(true);
        projectToArchive.setLastUpdateDate(Instant.now());
        projectRepository.save(projectToArchive);
        log.info("Project ID {} archived successfully by user ID {}", projectId, userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteProjectForUser(Long projectId, Long userId) {
        log.debug("Deleting project ID: {} for user ID: {}", projectId, userId);
        Project projectToDelete = findProjectByIdAndValidateOwnership(projectId, userId);
        projectRepository.delete(projectToDelete);
        log.info("Project ID {} deleted successfully by user ID {}", projectId, userId);
    }

    private Project findProjectByIdAndValidateOwnership(Long projectId, Long userId) {
        Project project = findProjectByIdOrThrow(projectId);
        validateProjectOwnership(project, userId);
        return project;
    }

    private Project findProjectByIdOrThrow(Long projectId) {
        return projectRepository.findByIdWithDetails(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));
    }

    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }

    private void validateProjectOwnership(Project project, Long userId) {
        if (!Objects.equals(project.getUser().getId(), userId)) {
            log.warn("Access denied for user {} on project {}", userId, project.getId());
            throw new AccessDeniedException("User with id " + userId + " is not authorized to access project " + project.getId());
        }
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new org.springframework.security.core.AuthenticationException("No authenticated user found") {};
        }
        return authentication.getName();
    }
}