package com.eql.cda.track.flow.controller;

import com.eql.cda.track.flow.dto.projectDto.ProjectCreateDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectSummaryDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectUpdateDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectViewDto;
import com.eql.cda.track.flow.service.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// Assurez-vous d'importer UsernameNotFoundException si elle est utilisée
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*; // Import global pour les annotations
import org.springframework.web.server.ResponseStatusException;

// Importez ceci si vous voulez utiliser la sécurité au niveau méthode
// import org.springframework.security.access.prepost.PreAuthorize;


@RestController
// ---> Chemin de base pour toutes les méthodes liées aux projets d'un utilisateur <---
@RequestMapping("/api/users/{userId}/projects")
// ---> Configuration CORS pour ce contrôleur <---
@CrossOrigin(origins = "http://localhost:3000", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS
})
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectService projectService; // Injection par constructeur recommandée

    // ---> Injection par Constructeur (Préférable à @Autowired sur setter/field) <---
    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Crée un nouveau projet pour un utilisateur spécifié.
     */
    // @PreAuthorize("#userId == principal.id or hasRole('ADMIN')") // Exemple Sécurité
    @PostMapping // Mappe sur POST /api/users/{userId}/projects
    public ResponseEntity<ProjectCreateDto> createProject(
            @PathVariable Long userId,
            @Valid @RequestBody ProjectCreateDto projectCreateDto) {
        logger.info("POST /api/users/{}/projects - Request received", userId);
        // !!! Ajoutez ici la vérification de sécurité pour s'assurer que l'utilisateur
        // authentifié a le droit de créer pour cet userId !!!
        try {
            ProjectCreateDto createdProject = projectService.createProject(userId, projectCreateDto);
            logger.info("Project created successfully with ID {} for user {}", createdProject.getId(), userId); // Supposant que DTO a un ID
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            // Gérer utilisateur non trouvé ou argument invalide
            logger.warn("Bad request creating project for user {}: {}", userId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error creating project for user {}: {}", userId, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not create project", e);
        }
    }

    /**
     * Récupère la liste paginée de tous les projets pour un utilisateur spécifié.
     */
    // @PreAuthorize("#userId == principal.id or hasRole('ADMIN')") // Exemple Sécurité
    @GetMapping // Mappe sur GET /api/users/{userId}/projects
    public ResponseEntity<Page<ProjectViewDto>> getAllUserProjects(
            @PathVariable Long userId,
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        logger.info("GET /api/users/{}/projects - Request received with pageable: {}", userId, pageable);
        // !!! Ajoutez ici la vérification de sécurité si nécessaire (ex: l'utilisateur voit-il ses propres projets?) !!!
        try {
            Page<ProjectViewDto> projectsPage = projectService.getAllProjectsPaginated(userId, pageable);
            logger.info("Retrieved {} projects for user {} (page {} of {})",
                    projectsPage.getNumberOfElements(), userId, projectsPage.getNumber(), projectsPage.getTotalPages());
            return ResponseEntity.ok(projectsPage);
        } catch (EntityNotFoundException e) { // Si le service lève pour l'utilisateur
            logger.warn("User not found for ID: {}", userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId, e);
        } catch (Exception e) {
            logger.error("Error retrieving projects for user {}: {}", userId, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving user projects", e);
        }
    }

    /**
     * Récupère les détails d'un projet spécifique pour un utilisateur.
     */
    // @PreAuthorize("hasPermission(#projectId, 'com.yourpackage.Project', 'read') or hasRole('ADMIN')") // Exemple sécurité basée sur ressource
    // Ou vérifier dans le service que l'utilisateur authentifié a accès au projet de cet userId
    @GetMapping("/{projectId}") // Mappe sur GET /api/users/{userId}/projects/{projectId}
    public ResponseEntity<ProjectViewDto> getProjectById(
            @PathVariable Long userId, // Utilisé pour vérifier les droits
            @PathVariable Long projectId) {
        logger.info("GET /api/users/{}/projects/{} - Request received", userId, projectId);
        // !!! Ajoutez ici la vérification de sécurité : l'utilisateur {userId} a-t-il accès à {projectId}? !!!
        try {
            // Idéalement, la méthode de service prend aussi userId pour vérifier la propriété/permission
            ProjectViewDto projectViewDto = projectService.getProjectByIdAndUser(projectId, userId); // Nom de méthode exemple
            logger.info("Project details retrieved for project ID {}", projectId);
            return ResponseEntity.ok(projectViewDto);
        } catch (EntityNotFoundException e) {
            logger.warn("Project or User not found for project ID {} / user ID {}: {}", projectId, userId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (SecurityException e) { // Si le service lève une exception de sécurité
            logger.warn("Access denied for user {} on project {}: {}", userId, projectId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error retrieving project {} for user {}: {}", projectId, userId, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving project details", e);
        }
    }

    // --- Méthode getRecentProjects ---
    // Cette méthode ne s'intègre pas bien au @RequestMapping de la classe.
    // Elle devrait soit être dans un contrôleur différent (ex: GeneralProjectController)
    // soit le RequestMapping de la classe devrait être plus général ("/api") et les chemins
    // de chaque méthode plus longs.
    // Solution de contournement: Mettre dans un sous-chemin improbable ou dans un autre contrôleur.
    // --> Placé dans un autre contrôleur serait plus propre. <--


    /**
     * Met à jour un projet existant.
     */
    // @PreAuthorize("hasPermission(#projectId, 'com.yourpackage.Project', 'write') or hasRole('ADMIN')") // Exemple sécurité
    @PatchMapping("/{projectId}") // Mappe sur PATCH /api/users/{userId}/projects/{projectId}
    public ResponseEntity<Void> updateProject(
            @PathVariable Long userId, // Utilisé pour vérifier les droits
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectUpdateDto projectUpdateDto) {
        logger.info("PATCH /api/users/{}/projects/{} - Request received", userId, projectId);
        // !!! Ajoutez ici la vérification de sécurité : l'utilisateur {userId} peut-il modifier {projectId}? !!!
        try {
            // Idéalement, la méthode de service prend aussi userId
            projectService.updateProjectForUser(projectId, userId, projectUpdateDto); // Nom de méthode exemple
            logger.info("Project {} updated successfully for user {}", projectId, userId);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (EntityNotFoundException e) {
            logger.warn("Project or User not found for update, project ID {} / user ID {}: {}", projectId, userId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (SecurityException e) {
            logger.warn("Access denied for update user {} on project {}: {}", userId, projectId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (IllegalArgumentException e) { // Pour les erreurs de validation métier non interceptées par @Valid
            logger.warn("Invalid data for updating project {}: {}", projectId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error updating project {} for user {}: {}", projectId, userId, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not update project", e);
        }
    }

    /**
     * Archive un projet spécifié.
     */
    // @PreAuthorize("hasPermission(#projectId, 'com.yourpackage.Project', 'archive') or hasRole('ADMIN')") // Exemple sécurité
    @PostMapping("/{projectId}/archive") // Mappe sur POST /api/users/{userId}/projects/{projectId}/archive
    public ResponseEntity<Void> archiveProject(
            @PathVariable Long userId, // Utilisé pour vérifier les droits
            @PathVariable Long projectId) {
        logger.info("POST /api/users/{}/projects/{}/archive - Request received", userId, projectId);
        // !!! Ajoutez ici la vérification de sécurité !!!
        try {
            // Idéalement, la méthode de service prend aussi userId
            projectService.archiveProjectForUser(projectId, userId); // Nom de méthode exemple
            logger.info("Project {} archived successfully for user {}", projectId, userId);
            return ResponseEntity.ok().build(); // 200 OK (ou 204 No Content)
        } catch (EntityNotFoundException e) {
            logger.warn("Project or User not found for archive, project ID {} / user ID {}: {}", projectId, userId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (SecurityException e) {
            logger.warn("Access denied for archive user {} on project {}: {}", userId, projectId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (IllegalStateException e) { // Ex: Déjà archivé
            logger.warn("Cannot archive project {} (user {}): {}", projectId, userId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e); // 409 Conflict
        } catch (Exception e) {
            logger.error("Error archiving project {} for user {}: {}", projectId, userId, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not archive project", e);
        }
    }

    /**
     * Supprime un projet spécifié de manière permanente.
     */
    // @PreAuthorize("hasPermission(#projectId, 'com.yourpackage.Project', 'delete') or hasRole('ADMIN')") // Exemple sécurité
    @DeleteMapping("/{projectId}") // Mappe sur DELETE /api/users/{userId}/projects/{projectId}
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long userId, // Utilisé pour vérifier les droits
            @PathVariable Long projectId) {
        logger.info("DELETE /api/users/{}/projects/{} - Request received", userId, projectId);
        // !!! Ajoutez ici la vérification de sécurité !!!
        try {
            // Idéalement, la méthode de service prend aussi userId
            projectService.deleteProjectForUser(projectId, userId); // Nom de méthode exemple
            logger.info("Project {} deleted successfully for user {}", projectId, userId);
            return ResponseEntity.noContent().build(); // 204 No Content est standard
        } catch (EntityNotFoundException e) {
            logger.warn("Project or User not found for delete, project ID {} / user ID {}: {}", projectId, userId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (SecurityException e) {
            logger.warn("Access denied for delete user {} on project {}: {}", userId, projectId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error deleting project {} for user {}: {}", projectId, userId, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not delete project", e);
        }
    }

    /*
     * Note sur /projects/recent:
     * Cette méthode ne correspond pas au @RequestMapping de la classe.
     * Elle devrait être déplacée dans un contrôleur plus approprié,
     * ou le @RequestMapping de cette classe devrait être changé pour juste "/api"
     * et les chemins de chaque méthode devraient être allongés en conséquence
     * (ex: @PostMapping("/users/{userId}/projects"), @GetMapping("/users/{userId}/projects"), etc.).
     * Laisser ici peut fonctionner mais rend l'API moins logique.
     */
    // Exemple si on la laisse avec un chemin distinct mais sous /api
    @GetMapping("/projects/recent") // Sera mappé à /api/projects/recent (car /api est le base path de ce controller même si le mapping est /api/users/{userId}/projects) -> C'est confus!
    // Pour la clareté, il serait mieux de changer le RequestMapping de la classe à /api
    // et de mettre le chemin complet sur chaque méthode.
    // Ou de créer un GeneralProjectController pour les actions non liées à un user spécifique.
    public ResponseEntity<Page<ProjectSummaryDto>> getRecentProjects(
            @RequestParam String login, // Récupérer via SecurityContextHolder serait mieux
            @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        logger.info("GET /api/projects/recent - Request for user '{}', pageable: {}", login, pageable);
        try {
            Page<ProjectSummaryDto> recentProjects = projectService.findRecentProjectsForUser(login, pageable);
            return ResponseEntity.ok(recentProjects);
        } catch (Exception e) { // Catch plus large pour inclure UsernameNotFoundException si applicable
            logger.error("Error retrieving recent projects for user '{}': {}", login, e.getMessage(), e);
            // Renvoyer NOT_FOUND si c'est UsernameNotFoundException, sinon INTERNAL_SERVER_ERROR
            // if (e instanceof UsernameNotFoundException) {
            //    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            // }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Le setter @Autowired est moins recommandé que l'injection par constructeur.
    // @Autowired
    // public void setProjectService(ProjectService projectService) {
    //    this.projectService = projectService;
    // }
}