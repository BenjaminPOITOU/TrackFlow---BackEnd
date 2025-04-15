package com.eql.cda.track.flow.service;

import com.eql.cda.track.flow.dto.projectDto.ProjectCreateDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectSummaryDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectUpdateDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectViewDto;
import jakarta.persistence.EntityNotFoundException; // Import standard pour cette exception
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException; // Import si vous lancez cette exception pour les droits

/**
 * Service gérant la logique métier liée aux projets.
 * Les méthodes qui modifient ou accèdent à des données spécifiques à un utilisateur
 * devraient idéalement inclure l'ID de l'utilisateur pour les vérifications de permission.
 */
public interface ProjectService {

    /**
     * Crée un nouveau projet pour l'utilisateur spécifié.
     * Vérifie que l'utilisateur créateur existe.
     *
     * @param userId           L'ID de l'utilisateur qui crée le projet.
     * @param projectCreateDto Les données du projet à créer.
     * @return Le DTO du projet nouvellement créé.
     * @throws EntityNotFoundException Si l'utilisateur avec l'ID fourni n'existe pas.
     * @throws IllegalArgumentException Si les données du DTO sont invalides (ex: titre manquant).
     */
    ProjectCreateDto createProject(Long userId, ProjectCreateDto projectCreateDto);

    /**
     * Récupère les informations d'un projet spécifique par son ID, en vérifiant les droits de l'utilisateur.
     *
     * @param projectId L'ID du projet à récupérer.
     * @param userId    L'ID de l'utilisateur effectuant la requête (pour la vérification des permissions).
     * @return Le DTO de vue du projet.
     * @throws EntityNotFoundException Si le projet ou l'utilisateur n'existe pas.
     * @throws AccessDeniedException   Si l'utilisateur spécifié n'a pas accès à ce projet.
     */
    ProjectViewDto getProjectByIdAndUser(Long projectId, Long userId);

    /**
     * Récupère une page de tous les projets visibles pour un utilisateur spécifié.
     * (Typiquement, les projets créés par cet utilisateur).
     *
     * @param userId    L'ID de l'utilisateur dont on veut les projets.
     * @param pageable  Les informations de pagination et de tri.
     * @return Une page contenant les DTOs de vue des projets.
     * @throws EntityNotFoundException Si l'utilisateur spécifié n'existe pas.
     */
    Page<ProjectViewDto> getAllProjectsPaginated(Long userId, Pageable pageable);

    /**
     * Récupère une page des projets récents pour un utilisateur (identifié par son login/username).
     * Note: Utiliser l'ID utilisateur serait plus cohérent si disponible partout.
     *
     * @param username Le nom d'utilisateur (login).
     * @param pageable Les informations de pagination et de tri.
     * @return Une page contenant les DTOs résumés des projets récents.
     * @throws org.springframework.security.core.userdetails.UsernameNotFoundException Si aucun utilisateur ne correspond au login fourni.
     */
    Page<ProjectSummaryDto> findRecentProjectsForUser(String username, Pageable pageable);

    /**
     * Met à jour un projet existant après vérification des permissions de l'utilisateur.
     *
     * @param projectId        L'ID du projet à mettre à jour.
     * @param userId           L'ID de l'utilisateur effectuant la requête (pour la vérification des permissions).
     * @param projectUpdateDto Les données de mise à jour.
     * @throws EntityNotFoundException Si le projet ou l'utilisateur n'existe pas.
     * @throws AccessDeniedException   Si l'utilisateur n'a pas le droit de modifier ce projet.
     * @throws IllegalArgumentException Si les données de mise à jour sont invalides.
     */
    void updateProjectForUser(Long projectId, Long userId, ProjectUpdateDto projectUpdateDto);

    /**
     * Archive un projet spécifié après vérification des permissions de l'utilisateur.
     * L'archivage est généralement une opération logique (mise à jour d'un statut).
     *
     * @param projectId L'ID du projet à archiver.
     * @param userId    L'ID de l'utilisateur effectuant la requête (pour la vérification des permissions).
     * @throws EntityNotFoundException Si le projet ou l'utilisateur n'existe pas.
     * @throws AccessDeniedException   Si l'utilisateur n'a pas le droit d'archiver ce projet.
     * @throws IllegalStateException   Si le projet est déjà dans un état qui empêche l'archivage (ex: déjà archivé).
     */
    void archiveProjectForUser(Long projectId, Long userId);

    /**
     * Supprime un projet spécifié de manière permanente après vérification des permissions de l'utilisateur.
     *
     * @param projectId L'ID du projet à supprimer.
     * @param userId    L'ID de l'utilisateur effectuant la requête (pour la vérification des permissions).
     * @throws EntityNotFoundException Si le projet ou l'utilisateur n'existe pas.
     * @throws AccessDeniedException   Si l'utilisateur n'a pas le droit de supprimer ce projet.
     */
    void deleteProjectForUser(Long projectId, Long userId);

    // --- Méthode Originale Renommée/Obsolète ---
    // Cette méthode n'est plus utilisée directement par le contrôleur tel que modifié,
    // car elle ne prend pas userId pour la vérification des droits.
    // Gardez-la si elle est utilisée ailleurs, sinon envisagez de la supprimer/adapter.
    // Si vous la gardez, assurez-vous que l'implémentation vérifie les droits
    // via SecurityContextHolder si userId n'est pas passé.
    // ProjectViewDto getCurrentProjectInfo(Long id);

}