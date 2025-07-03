package com.eql.cda.track.flow.repository;

import com.eql.cda.track.flow.entity.Composition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link Composition} entity.
 * It provides methods to perform database operations on compositions,
 * including optimized queries for fetching related data.
 */
@Repository
public interface CompositionRepository extends JpaRepository<Composition, Long> {

    /**
     * Finds a single composition by its ID and eagerly fetches its associated project, branches, and versions.
     * This is the primary query for loading a composition's main graph. The DISTINCT keyword is crucial
     * to prevent duplication of the root Composition object due to the nested collection fetch (versions).
     * Other collections like 'subGenders' should be initialized explicitly in the service layer to avoid
     * a Cartesian product.
     *
     * @param compositionId The ID of the composition to find.
     * @return An {@link Optional} containing the {@link Composition} with its project and branch graph,
     * or an empty Optional if not found.
     */
    @Query("SELECT DISTINCT c FROM Composition c " +
            "LEFT JOIN FETCH c.project p " +
            "LEFT JOIN FETCH c.branches b " +
            "LEFT JOIN FETCH b.versions " +
            "WHERE c.id = :compositionId")
    Optional<Composition> findByIdWithProjectAndBranches(@Param("compositionId") Long compositionId);

    /**
     * Finds all compositions for a given project ID, ordered by the last update date in descending order.
     * This query is optimized with JOIN FETCH to load necessary data for summary views in a single operation.
     *
     * @param projectId The ID of the parent project.
     * @return A {@link List} of {@link Composition} entities.
     */
    @Query("SELECT DISTINCT c FROM Composition c " +
            "LEFT JOIN FETCH c.branches b " +
            "LEFT JOIN FETCH b.versions " +
            "WHERE c.project.id = :projectId " +
            "ORDER BY c.lastUpdateDate DESC")
    List<Composition> findByProjectIdOrderByLastUpdateDateDesc(@Param("projectId") Long projectId);

    /**
     * Finds a paginated list of the most recent compositions belonging to any project of a specific user.
     * This query is designed for dashboard-like features.
     *
     * @param userId The ID of the user whose compositions are to be retrieved.
     * @param pageable The pagination information.
     * @return A {@link Page} of {@link Composition} entities.
     */
    @Query(value = "SELECT DISTINCT c FROM Composition c " +
            "JOIN FETCH c.project p " +
            "LEFT JOIN FETCH c.branches b " +
            "LEFT JOIN FETCH b.versions " +
            "WHERE p.user.id = :userId",
            countQuery = "SELECT count(c) FROM Composition c JOIN c.project p WHERE p.user.id = :userId")
    Page<Composition> findRecentCompositionsByUserId(@Param("userId") Long userId, Pageable pageable);
}