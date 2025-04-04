package com.eql.cda.track.flow.controller;


import com.eql.cda.track.flow.dto.projectDto.ProjectCreateDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectSummaryDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectUpdateDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectViewDto;
import com.eql.cda.track.flow.service.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/projects")
public class ProjectController {

    private ProjectService projectService;

    @PostMapping
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

    @GetMapping
    public ResponseEntity<List<ProjectViewDto>> getAllUserProjects(@PathVariable Long userId) {
        try {
            // -----=====>>>>>> SÉCURITÉ CRITIQUE <<<<<<=====-----
            // Il est ABSOLUMENT INDISPENSABLE de vérifier ici que l'utilisateur
            // authentifié (celui qui fait l'appel API) a le droit de voir les projets
            // de l'{userId} spécifié dans l'URL.
            // Typiquement, un utilisateur ne devrait pouvoir accéder qu'à ses propres projets,
            // sauf s'il est administrateur.
            // Exemple de vérification (simpliste, à adapter avec Spring Security):
            // String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            // User authenticatedUser = userRepository.findByLogin(authenticatedUsername); // Ou via un service
            // if (!authenticatedUser.getId().equals(userId) && !/* estAdmin(authenticatedUser) */) {
            //    throw new AccessDeniedException("User not authorized to access projects for user ID: " + userId);
            // }
            // -----=====>>>>>> FIN SÉCURITÉ CRITIQUE <<<<<<=====-----


            List<ProjectViewDto> projects = projectService.getAllProjects(userId);
            return ResponseEntity.ok(projects);
        } catch (EntityNotFoundException e) {
            // Si l'utilisateur {userId} lui-même n'est pas trouvé par le service
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            // Si l'ID utilisateur est invalide (ex: null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
            // } catch (AccessDeniedException e) { // Si vous implémentez la vérification de sécurité
            //     throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (Exception e) {
            // Gérer les autres erreurs inattendues
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving user projects", e);
        }
    }

    @GetMapping("/project/{id}")
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

    @GetMapping("/recent")
    public ResponseEntity<List<ProjectSummaryDto>> getRecentProjects() {
        try {
            List<ProjectSummaryDto> recentProjects = projectService.getRecentProjects(); // Appel inchangé, mais le retour est différent
            return ResponseEntity.ok(recentProjects);
        } catch (Exception e) {
            // Log l'erreur de préférence
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving recent projects summary", e);
        }
    }


    @PatchMapping("/{id}")
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
