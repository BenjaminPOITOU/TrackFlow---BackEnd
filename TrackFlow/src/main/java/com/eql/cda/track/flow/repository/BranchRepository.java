package com.eql.cda.track.flow.repository;

import com.eql.cda.track.flow.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the {@link Branch} entity.
 * It provides standard CRUD (Create, Read, Update, Delete) operations and allows
 * for the definition of custom query methods to retrieve branch data from the database.
 */
@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

    /**
     * Finds all branches associated with a specific composition ID.
     * The results are ordered by the last update date in descending order to show the most
     * recently modified branches first. This is useful for displaying branches in a user interface.
     *
     * @param compositionId The ID of the parent {@link com.eql.cda.track.flow.entity.Composition}.
     * @return A {@link List} of {@link Branch} entities, ordered by their last update date.
     *         Returns an empty list if no branches are found for the given composition.
     */
    List<Branch> findByCompositionIdOrderByLastUpdateDateDesc(Long compositionId);
}