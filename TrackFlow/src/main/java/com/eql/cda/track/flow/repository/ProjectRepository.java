package com.eql.cda.track.flow.repository;

import com.eql.cda.track.flow.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Project entity.
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    /**
     * Finds a single project by its ID and eagerly fetches its collections to prevent N+1 issues.
     * This is the preferred method for retrieving a single project's full details.
     *
     * @param projectId The ID of the project.
     * @return An Optional containing the project with its details, or an empty Optional if not found.
     */
    @Query("SELECT p FROM Project p " +
            "LEFT JOIN FETCH p.projectPurposes " +
            "LEFT JOIN FETCH p.projectMusicalGendersPreDefined " +
            "WHERE p.id = :projectId")
    Optional<Project> findByIdWithDetails(@Param("projectId") Long projectId);



    /**
     * Finds a page of non-archived projects for a specific user, eagerly fetching associated collections.
     * This query is optimized to prevent N+1 problems when mapping to DTOs.
     * The DISTINCT keyword is crucial to avoid duplicate project rows due to the multiple JOIN FETCH clauses.
     *
     * @param userId The ID of the user.
     * @param pageable Pagination and sorting information.
     * @return A Page of non-archived projects with their details loaded.
     */
    @Query(value = "SELECT DISTINCT p FROM Project p " +
            "LEFT JOIN FETCH p.projectPurposes " +
            "LEFT JOIN FETCH p.projectMusicalGendersPreDefined " +
            "WHERE p.user.id = :userId AND p.archived = false",
            countQuery = "SELECT count(p) FROM Project p WHERE p.user.id = :userId AND p.archived = false")
    Page<Project> findByUserIdAndArchivedFalse(@Param("userId") Long userId, Pageable pageable);

    /**
     * Finds a page of projects for a specific user, ordered by creation date descending,
     * and eagerly fetches the musical genres.
     * This query is optimized for the "recent projects" dashboard view.
     *
     * @param userId The ID of the user.
     * @param pageable Pagination and sorting information.
     * @return A Page of projects with their musical genres loaded.
     */
    @Query(value = "SELECT DISTINCT p FROM Project p " +
            "LEFT JOIN FETCH p.projectMusicalGendersPreDefined " +
            "WHERE p.user.id = :userId",
            countQuery = "SELECT count(p) FROM Project p WHERE p.user.id = :userId")
    Page<Project> findByUserIdOrderByCreatedDateDesc(@Param("userId") Long userId, Pageable pageable);
}