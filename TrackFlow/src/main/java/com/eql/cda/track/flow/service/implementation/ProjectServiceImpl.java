package com.eql.cda.track.flow.service.implementation;

import com.eql.cda.track.flow.dto.projectDto.ProjectCreateDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectSummaryDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectUpdateDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectViewDto;
import com.eql.cda.track.flow.entity.Project;
import com.eql.cda.track.flow.entity.ProjectMusicalGenderPreDefined;
import com.eql.cda.track.flow.entity.ProjectPurpose;
import com.eql.cda.track.flow.entity.User;
import com.eql.cda.track.flow.repository.ProjectRepository;
import com.eql.cda.track.flow.repository.UserRepository;
import com.eql.cda.track.flow.service.ProjectService;
import com.eql.cda.track.flow.validation.Constants;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional; // Attention: utiliser org.springframework.transaction.annotation.Transactional
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
// import org.springframework.security.access.AccessDeniedException; // Importer si vous l'utilisez
// import org.springframework.security.core.context.SecurityContextHolder; // Pour la sécurité
// import org.springframework.security.core.userdetails.UserDetails; // Pour la sécurité
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Instant; // Utiliser Instant pour les dates
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    // --- Injection par Constructeur ---
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    // --- Méthodes Service (Adaptées) ---

    @Override
    @Transactional // Utiliser l'annotation Spring
    public ProjectCreateDto createProject(Long userId, ProjectCreateDto projectCreateDto) {
        logger.debug("Creating project for user ID: {}", userId);
        // Validation DTO
        validateProjectCreateDto(projectCreateDto);

        // Récupération User et validation existence
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("User not found with ID {} during project creation.", userId);
                    return new EntityNotFoundException("Creator user not found with ID: " + userId);
                });

        // Vérification Sécurité (Exemple Simpliste - Adaptez avec votre logique de sécurité)
        // checkUserPermission(userId, "CREATE_PROJECT_FOR_USER"); // Implémenter cette méthode

        // Mapping vers Entité
        Project newProject = mapCreateDtoToEntity(projectCreateDto, creator);
        newProject.setCreatedDate(Instant.now()); // Initialiser la date de création

        // Sauvegarde
        Project savedProject = projectRepository.save(newProject);
        logger.info("Project created with ID: {} for user ID: {}", savedProject.getId(), userId);

        // Retourner le DTO (on pourrait aussi retourner ProjectViewDto si plus utile)
        return mapEntityToCreateDto(savedProject);
    }

    @Override
    @Transactional
    public ProjectViewDto getProjectByIdAndUser(Long projectId, Long userId) {
        logger.debug("Fetching project ID: {} for user ID: {}", projectId, userId);
        if (projectId == null || userId == null) {
            throw new IllegalArgumentException("Project ID and User ID cannot be null.");
        }

        // Récupérer le projet
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> {
                    logger.warn("Project not found with ID: {}", projectId);
                    return new EntityNotFoundException("Project not found with ID: " + projectId);
                });

        // ---> VÉRIFICATION DE PERMISSION <---
        // Vérifie si le projet appartient à l'utilisateur demandé
        if (!project.getUser().getId().equals(userId)) {
            // Ici, on pourrait aussi vérifier si l'utilisateur actuel (via SecurityContextHolder)
            // a le droit de voir les projets d'un autre utilisateur (ex: rôle ADMIN).
            logger.warn("Access denied: User ID {} attempted to access project ID {} owned by user ID {}",
                    userId, projectId, project.getUser().getId());
            throw new SecurityException("User does not have permission to access this project.");
            // Ou lancer : throw new AccessDeniedException("User does not have permission to access this project.");
        }

        logger.debug("Project ID {} found and user {} has access.", projectId, userId);
        return mapEntityToProjectViewDto(project);
    }

    @Override
    @Transactional
    public Page<ProjectViewDto> getAllProjectsPaginated(Long userId, Pageable pageable) {
        logger.debug("Fetching *active* paginated projects for user ID: {} with pageable: {}", userId, pageable); // Message mis à jour
        if (userId == null) { throw new IllegalArgumentException("User ID cannot be null"); }

        if (!userRepository.existsById(userId)) {
            logger.warn("User not found with ID: {}", userId);
            throw new EntityNotFoundException("User not found with id: " + userId);
        }


        Page<Project> userProjectsPage = projectRepository.findByUserIdAndArchivedFalse(userId, pageable);
        logger.debug("Found {} *active* projects for user ID {} on page {}", userProjectsPage.getNumberOfElements(), userId, pageable.getPageNumber());

        return userProjectsPage.map(this::mapEntityToProjectViewDto);
    }

    @Override
    @Transactional
    public Page<ProjectSummaryDto> findRecentProjectsForUser(String username, Pageable pageable) {
        logger.debug("Finding recent projects for username: {} with pageable: {}", username, pageable);
        if (!StringUtils.hasText(username)) { throw new IllegalArgumentException("Username cannot be blank"); }

        User user = userRepository.findByLogin(username)
                .orElseThrow(() -> {
                    logger.warn("User not found with username: {}", username);
                    return new UsernameNotFoundException("User not found with username: " + username);
                });

        // Sécurité: Vérifier si l'utilisateur connecté a le droit de voir les projets de 'username'
        // checkUserPermission(user.getId(), "VIEW_RECENT_PROJECTS");

        Page<Project> recentProjectsPage = projectRepository.findByUserOrderByCreatedDateDesc(user, pageable);
        logger.debug("Found {} recent projects for username {}", recentProjectsPage.getNumberOfElements(), username);

        return recentProjectsPage.map(this::mapEntityToProjectSummaryDto);
    }

    @Override
    @Transactional
    public void updateProjectForUser(Long projectId, Long userId, ProjectUpdateDto projectUpdateDto) {
        logger.debug("Updating project ID: {} for user ID: {}", projectId, userId);
        // Validation
        if (projectId == null || userId == null) { throw new IllegalArgumentException("Project ID and User ID cannot be null."); }
        if (projectUpdateDto == null) { throw new IllegalArgumentException("Project update data cannot be null."); }
        validateProjectUpdateDto(projectUpdateDto); // Validation plus poussée si nécessaire

        // Récupérer Projet
        Project projectToUpdate = projectRepository.findById(projectId)
                .orElseThrow(() -> {
                    logger.warn("Project not found with ID: {} for update.", projectId);
                    return new EntityNotFoundException("Project not found with ID: " + projectId);
                });

        // ---> VÉRIFICATION DE PERMISSION <---
        if (!projectToUpdate.getUser().getId().equals(userId)) {
            logger.warn("Access denied: User ID {} attempted to update project ID {} owned by user ID {}",
                    userId, projectId, projectToUpdate.getUser().getId());
            throw new SecurityException("User does not have permission to update this project.");
            // Ou lancer : throw new AccessDeniedException("User does not have permission...");
        }

        // Appliquer les modifications
        boolean changed = applyUpdatesFromDto(projectToUpdate, projectUpdateDto);

        // Sauvegarder si modifié
        if (changed) {
            projectToUpdate.setLastUpdateDate(Instant.now()); // Mettre à jour la date de modif
            projectRepository.save(projectToUpdate);
            logger.info("Project ID {} updated successfully by user ID {}", projectId, userId);
        } else {
            logger.info("No changes detected for project ID {}", projectId);
        }
    }

    @Override
    @Transactional
    public void archiveProjectForUser(Long projectId, Long userId) {
        logger.debug("Archiving project ID: {} for user ID: {}", projectId, userId);
        if (projectId == null || userId == null) { throw new IllegalArgumentException("Project ID and User ID cannot be null."); }

        // Récupérer Projet
        Project projectToArchive = projectRepository.findById(projectId)
                .orElseThrow(() -> {
                    logger.warn("Project not found with ID: {} for archive.", projectId);
                    return new EntityNotFoundException("Project not found with ID: " + projectId);
                });

        // ---> VÉRIFICATION DE PERMISSION <---
        if (!projectToArchive.getUser().getId().equals(userId)) {
            logger.warn("Access denied: User ID {} attempted to archive project ID {} owned by user ID {}",
                    userId, projectId, projectToArchive.getUser().getId());
            throw new SecurityException("User does not have permission to archive this project.");
            // Ou lancer : throw new AccessDeniedException("User does not have permission...");
        }

        // Vérifier état
        if (projectToArchive.getArchived()) { // Supposant une méthode getArchived()
            logger.warn("Project ID {} is already archived.", projectId);
            throw new IllegalStateException("Project with ID: " + projectId + " is already archived.");
        }

        // Archiver et sauvegarder
        projectToArchive.setArchived(true); // Supposant une méthode setArchived()
        projectToArchive.setLastUpdateDate(Instant.now());
        projectRepository.save(projectToArchive);
        logger.info("Project ID {} archived successfully by user ID {}", projectId, userId);
    }

    @Override
    @Transactional
    public void deleteProjectForUser(Long projectId, Long userId) {
        logger.debug("Deleting project ID: {} for user ID: {}", projectId, userId);
        if (projectId == null || userId == null) { throw new IllegalArgumentException("Project ID and User ID cannot be null."); }

        // Récupérer Projet (pour vérifier la propriété avant de supprimer)
        Project projectToDelete = projectRepository.findById(projectId)
                .orElseThrow(() -> {
                    logger.warn("Project not found with ID: {} for deletion.", projectId);
                    return new EntityNotFoundException("Project not found with ID: " + projectId);
                });

        // ---> VÉRIFICATION DE PERMISSION <---
        if (!projectToDelete.getUser().getId().equals(userId)) {
            logger.warn("Access denied: User ID {} attempted to delete project ID {} owned by user ID {}",
                    userId, projectId, projectToDelete.getUser().getId());
            throw new SecurityException("User does not have permission to delete this project.");
            // Ou lancer : throw new AccessDeniedException("User does not have permission...");
        }

        // Suppression
        projectRepository.deleteById(projectId);
        logger.info("Project ID {} deleted successfully by user ID {}", projectId, userId);
    }


    // --- Méthodes privées de Mapping et Validation ---

    private Project mapCreateDtoToEntity(ProjectCreateDto dto, User creator) {
        Project entity = new Project();
        entity.setUser(creator);
        entity.setTitle(dto.getTitle().trim());
        entity.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : null);
        entity.setIllustration(dto.getIllustration());
        entity.setProjectStatus(dto.getProjectStatus());
        entity.setProjectType(dto.getProjectType());
        entity.setProjectCommercialStatus(dto.getProjectCommercialStatus());
        entity.setProjectPurposes(dto.getProjectPurposes() != null ? new HashSet<>(dto.getProjectPurposes()) : new HashSet<>());
        entity.setProjectMusicalGendersPreDefined(dto.getProjectMusicalGendersPreDefined() != null ? new HashSet<>(dto.getProjectMusicalGendersPreDefined()) : new HashSet<>());
        // Initialiser les dates et l'archivage
        entity.setCreatedDate(Instant.now());
        entity.setLastUpdateDate(Instant.now());
        entity.setArchived(false); // Par défaut, non archivé
        return entity;
    }

    private ProjectCreateDto mapEntityToCreateDto(Project entity) {
        // Inverse de mapCreateDtoToEntity (pour le retour après création)
        ProjectCreateDto dto = new ProjectCreateDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setIllustration(entity.getIllustration());
        dto.setProjectStatus(entity.getProjectStatus());
        dto.setProjectType(entity.getProjectType());
        dto.setProjectCommercialStatus(entity.getProjectCommercialStatus());
        dto.setProjectPurposes(entity.getProjectPurposes());
        dto.setProjectMusicalGendersPreDefined(entity.getProjectMusicalGendersPreDefined());
        dto.setCreationDate(entity.getCreatedDate()); // Mappe la date de création
        return dto;
    }

    private ProjectViewDto mapEntityToProjectViewDto(Project entity) {
        // Logique de mapping existante (assurez-vous qu'elle est correcte)
        logger.trace("Mapping Project entity ID: {} to ProjectViewDto", entity.getId());
        ProjectViewDto dto = new ProjectViewDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setIllustration(entity.getIllustration());
        dto.setProjectStatus(entity.getProjectStatus());
        dto.setProjectType(entity.getProjectType());
        dto.setProjectCommercialStatus(entity.getProjectCommercialStatus());
        // Vérifier la logique de calcul de compositionsTotal
        dto.setCompositionsTotal(entity.getCompositions() != null ? entity.getCompositions().size() : 0);
        // Copier les Sets pour éviter les modifications par référence
        dto.setProjectPurposes(entity.getProjectPurposes() != null ? new HashSet<>(entity.getProjectPurposes()) : new HashSet<>());
        dto.setProjectMusicalGendersPreDefined(entity.getProjectMusicalGendersPreDefined() != null ? new HashSet<>(entity.getProjectMusicalGendersPreDefined()) : new HashSet<>());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdateDate(entity.getLastUpdateDate());
        logger.trace("Finished mapping Project entity ID: {}", entity.getId());
        return dto;
    }

    private ProjectSummaryDto mapEntityToProjectSummaryDto(Project entity) {
        // Logique de mapping existante
        logger.trace("Mapping Project entity ID: {} to ProjectSummaryDto", entity.getId());
        ProjectSummaryDto dto = new ProjectSummaryDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setProjectStatus(entity.getProjectStatus());
        dto.setCreatedDate(entity.getCreatedDate());
        List<String> genreNames = new ArrayList<>();
        if (entity.getProjectMusicalGendersPreDefined() != null) {
            entity.getProjectMusicalGendersPreDefined().forEach(genreEnum -> genreNames.add(genreEnum.name()));
        }
        dto.setProjectMusicalGendersPreDefined(genreNames);
        logger.trace("Finished mapping Project entity ID: {}", entity.getId());
        return dto;
    }

    // Applique les mises à jour du DTO à l'entité, retourne true si quelque chose a changé
    private boolean applyUpdatesFromDto(Project entity, ProjectUpdateDto dto) {
        boolean changed = false;
        // Titre
        if (dto.getTitle() != null && StringUtils.hasText(dto.getTitle())) {
            String newTitle = dto.getTitle().trim(); /* + validation longueur */
            if (!newTitle.equals(entity.getTitle())) { entity.setTitle(newTitle); changed = true; }
        }
        // Description
        if (dto.getDescription() != null) {
            String newDescription = dto.getDescription().trim(); /* + validation longueur */
            if (!Objects.equals(newDescription, entity.getDescription())) { entity.setDescription(newDescription); changed = true; }
        }
        // Illustration
        if (dto.getIllustration() != null) {
            /* + validation URL */
            if (!Objects.equals(dto.getIllustration(), entity.getIllustration())) { entity.setIllustration(dto.getIllustration()); changed = true; }
        }
        // Project Status
        if (dto.getProjectStatus() != null && dto.getProjectStatus() != entity.getProjectStatus()) {
            entity.setProjectStatus(dto.getProjectStatus()); changed = true;
        }
        // Project Type
        if (dto.getProjectType() != null && dto.getProjectType() != entity.getProjectType()) {
            entity.setProjectType(dto.getProjectType()); changed = true;
        }
        // Commercial Status
        if (dto.getProjectCommercialStatus() != null && dto.getProjectCommercialStatus() != entity.getProjectCommercialStatus()) {
            entity.setProjectCommercialStatus(dto.getProjectCommercialStatus()); changed = true;
        }
        // Purposes (remplacement complet)
        if (dto.getProjectPurposes() != null) {
            Set<ProjectPurpose> newPurposes = new HashSet<>(dto.getProjectPurposes());
            if (!Objects.equals(newPurposes, entity.getProjectPurposes())) { entity.setProjectPurposes(newPurposes); changed = true; }
        }
        // Musical Genders (remplacement complet)
        if (dto.getProjectMusicalGendersPreDefined() != null) {
            Set<ProjectMusicalGenderPreDefined> newGenders = new HashSet<>(dto.getProjectMusicalGendersPreDefined());
            if (!Objects.equals(newGenders, entity.getProjectMusicalGendersPreDefined())) { entity.setProjectMusicalGendersPreDefined(newGenders); changed = true; }
        }
        return changed;
    }

    // Méthodes de validation séparées (exemples)
    private void validateProjectCreateDto(ProjectCreateDto dto) {
        if (dto == null) throw new IllegalArgumentException("Project creation data cannot be null.");
        if (!StringUtils.hasText(dto.getTitle())) throw new IllegalArgumentException("Project title is mandatory.");
        // ... autres validations de base du DTO create ...
        try { if (StringUtils.hasText(dto.getIllustration())) { new URL(dto.getIllustration()).toURI(); } }
        catch (MalformedURLException | URISyntaxException e) { throw new IllegalArgumentException("Illustration URL format is invalid.", e); }
    }

    private void validateProjectUpdateDto(ProjectUpdateDto dto) {
        if (dto == null) throw new IllegalArgumentException("Project update data cannot be null.");
        // Validation spécifique au DTO update si nécessaire (ex: certains champs ne peuvent pas être mis à null)
        try { if (dto.getIllustration() != null && StringUtils.hasText(dto.getIllustration())) { new URL(dto.getIllustration()).toURI(); } }
        catch (MalformedURLException | URISyntaxException e) { throw new IllegalArgumentException("Illustration URL format is invalid.", e); }
    }

    // ---> Méthode pour la Vérification de Sécurité (À Implémenter) <---
    // private void checkUserPermission(Long targetUserId, String action) {
    //     // Récupérer l'utilisateur authentifié
    //     Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    //     Long authenticatedUserId = null;
    //     Set<String> roles = new HashSet<>();
    //
    //     if (principal instanceof UserDetails) {
    //         // Adapter ceci à votre UserDetails implementation pour obtenir l'ID
    //         // authenticatedUserId = ((YourUserDetails) principal).getId();
    //         // roles = ((YourUserDetails) principal).getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    //     } else if (principal instanceof String) {
    //         // Gérer si principal est juste un username
    //     } else {
    //         throw new AccessDeniedException("Could not determine authenticated user.");
    //     }
    //
    //     // Vérifier les droits
    //     boolean hasPermission = false;
    //     if (authenticatedUserId != null) {
    //          // L'utilisateur peut agir sur ses propres ressources OU est admin
    //         if (authenticatedUserId.equals(targetUserId) || roles.contains("ROLE_ADMIN")) {
    //             hasPermission = true;
    //         }
    //         // Ajouter d'autres logiques de permission si nécessaire
    //     }
    //
    //     if (!hasPermission) {
    //         logger.warn("Permission denied for user {} to perform action '{}' on resource related to user {}", authenticatedUserId, action, targetUserId);
    //         throw new AccessDeniedException("Insufficient permissions to perform this action.");
    //     }
    //     logger.debug("Permission granted for user {} to perform action '{}' on resource related to user {}", authenticatedUserId, action, targetUserId);
    // }

    // Méthode Originale getCurrentProjectInfo (gardée pour l'instant, marquée comme potentiellement obsolète)
    // @Override
    // @SpringTransactional(readOnly = true)
    // @Deprecated // Marquer comme dépréciée car getProjectByIdAndUser est préférée
    // public ProjectViewDto getCurrentProjectInfo(Long id) {
    //     logger.warn("Calling deprecated method getCurrentProjectInfo without userId check for project ID: {}", id);
    //     if (id == null) { throw new IllegalArgumentException("Project ID cannot be null."); }
    //     Project project = projectRepository.findById(id)
    //             .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + id));
    //     // ATTENTION: Aucune vérification de permission ici !
    //     return mapEntityToProjectViewDto(project);
    // }


    // --- Setters pour Injection (Obsolète avec l'injection par constructeur) ---
    // @Autowired
    // public void setProjectRepository (ProjectRepository projectRepository){ this.projectRepository = projectRepository; }
    // @Autowired
    // public void setUserRepository (UserRepository userRepository){ this.userRepository = userRepository; }
}