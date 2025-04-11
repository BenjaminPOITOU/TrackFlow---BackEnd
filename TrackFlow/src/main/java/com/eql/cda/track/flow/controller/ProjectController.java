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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    private ProjectService projectService;

    @PostMapping("/users/{userId}/projects")
    public ResponseEntity<ProjectCreateDto> createProject(
            @PathVariable Long userId, // <--- Récupération de l'ID depuis l'URL
            @Valid @RequestBody ProjectCreateDto projectCreateDto) {

        // --- Considération de Sécurité Importante ---
        // !!! ATTENTION !!!
        // Maintenant que l'ID utilisateur est fourni par le client, vous DEVEZ ABSOLUMENT vérifier
        // que l'utilisateur actuellement authentifié (via Spring Security Context)
        // a le droit de créer un projet pour cet `userId`.
        // Soit l'utilisateur authentifié EST `userId`, soit il a un rôle d'administrateur.
        // Cette vérification devrait idéalement se faire DANS LE SERVICE ou via
        // la sécurité au niveau méthode (@PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")).
        // Sans cette vérification, n'importe quel utilisateur authentifié pourrait créer
        // un projet pour n'importe quel autre utilisateur en changeant l'URL !
        // Exemple de vérification à ajouter (simpliste) :
        // checkUserPermission(userId); // Méthode à implémenter qui vérifie le contexte de sécurité actuel
        // -------------------------------------------

        try {
            // On passe directement le userId reçu en paramètre
            ProjectCreateDto createdProject = projectService.createProject(userId, projectCreateDto);
            // Retourner le projet créé
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
        } catch (IllegalArgumentException e) {
            // Ex: Utilisateur non trouvé par le service, champ obligatoire manquant (si non géré par @Valid)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (EntityNotFoundException e) {
            // Si le service lève cette exception spécifiquement pour l'utilisateur non trouvé
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Creator user not found with ID: " + userId, e);
        }
        // Catch d'autres exceptions métier spécifiques si nécessaire
    }

    @GetMapping("/users/{userId}/projects")
    public ResponseEntity<Page<ProjectViewDto>> getAllUserProjects(
            @PathVariable Long userId,
            // Applique des valeurs par défaut si les paramètres ne sont pas fournis dans l'URL
            // Ex: Par défaut, page 0, 10 éléments par page, trié par updateDate décroissant
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC)
            Pageable pageable // Spring injecte le Pageable final (défauts ou params URL)
    ) {
        // Log initial avec l'ID utilisateur et le pageable appliqué
        logger.info("GET /api/users/{}/projects - Request received with pageable: {}", userId, pageable);

        try {
            // Appelle la méthode de service paginée
            // Assure-toi que cette méthode existe bien dans ton service/implémentation
            Page<ProjectViewDto> projectsPage = projectService.getAllProjectsPaginated(userId, pageable);

            // Log succès avant de retourner
            logger.info("Successfully retrieved {} projects for user {} (page {} of {})",
                    projectsPage.getNumberOfElements(), userId, projectsPage.getNumber(), projectsPage.getTotalPages());

            // Retourne la page de projets avec un statut OK (200)
            return ResponseEntity.ok(projectsPage);

        } catch (EntityNotFoundException e) {
            // Spécifiquement si l'utilisateur {userId} n'existe pas (levé par le service)
            logger.warn("User not found with ID: {}", userId);
            // Utilise ResponseStatusException pour inclure le message et causer un log d'erreur approprié
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId, e);
            // Alternativement, retourne directement comme dans l'exemple /recent :
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Moins d'info dans la réponse

        } catch (IllegalArgumentException e) {
            // Si l'ID ou un paramètre est invalide (ex: userId null passé au service)
            logger.warn("Invalid argument provided for user projects request (userId: {}): {}", userId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
            // Alternative:
            // return ResponseEntity.badRequest().build();

        } catch (Exception e) {
            // Gérer les autres erreurs inattendues
            logger.error("Unexpected error retrieving projects for user {}: {}", userId, e.getMessage(), e);
            // Utilise ResponseStatusException pour un meilleur logging et une réponse d'erreur serveur
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving user projects", e);
            // Alternative:
            // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/users/{userId}/projects/project/{id}")
    public ResponseEntity<ProjectViewDto> getProjectById(@PathVariable Long id) {
        try {
            ProjectViewDto projectViewDto = projectService.getCurrentProjectInfo(id);
            return ResponseEntity.ok(projectViewDto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            // Ex: ID null ou invalide passé au service
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/projects/recent")
    public ResponseEntity<Page<ProjectSummaryDto>> getRecentProjects(
            @RequestParam String login, // Pour l'instant, viendra de la sécurité plus tard
            @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        logger.info("GET /api/projects/recent - Request for user '{}', pageable: {}", login, pageable);

        try {
            Page<ProjectSummaryDto> recentProjects = projectService.findRecentProjectsForUser(login, pageable);
            return ResponseEntity.ok(recentProjects);
        } catch (UsernameNotFoundException e) {
            logger.warn("User not found for login: '{}'", login);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 si user inconnu
        } catch (Exception e) {
            logger.error("Error retrieving recent projects for user '{}': {}", login, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PatchMapping("/users/{userId}/projects/{id}")
    public ResponseEntity<Void> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectUpdateDto projectUpdateDto) {
        try {
            projectService.updateProject(id, projectUpdateDto);
            return ResponseEntity.noContent().build(); // 204 No Content est approprié pour PATCH/PUT réussi sans retour de corps
            // Alternativement: return ResponseEntity.ok().build(); // 200 OK
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            // Ex: ID null, DTO null, validation de longueur échouée dans le service
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        // Catch d'autres exceptions métier spécifiques (ex: validation de statut)
    }
    @PostMapping("/{id}/archive")
    public ResponseEntity<Void> archiveProject(@PathVariable long id) {
        try {
            projectService.archiveProject(id);
            return ResponseEntity.ok().build(); // 200 OK pour une action réussie
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (IllegalStateException e) {
            // Ex: Projet déjà archivé
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e); // 409 Conflict est approprié ici
        } catch (IllegalArgumentException e) {
            // Ex: ID invalide
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable long id) {
        try {
            projectService.deleteProject(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            // Ex: ID invalide
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

    }


    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }
}
