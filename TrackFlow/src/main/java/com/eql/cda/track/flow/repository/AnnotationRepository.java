package com.eql.cda.track.flow.repository;

import com.eql.cda.track.flow.entity.Annotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link Annotation} entities.
 * Provides standard database operations and custom queries for annotations.
 */
@Repository
public interface AnnotationRepository extends JpaRepository<Annotation, Long> {

    /**
     * Finds all annotations associated with a specific version ID, ordered by their creation date in descending order.
     * This ensures that the most recent annotations are returned first.
     *
     * @param versionId the ID of the {@link com.eql.cda.track.flow.entity.Version} to find annotations for.
     * @return a list of {@link Annotation} entities, sorted by creation date descending.
     */
    List<Annotation> findAllByVersionIdOrderByCreationDateDesc(Long versionId);

    /**
     * Finds all annotations for a given version ID that are not marked as resolved.
     *
     * @param versionId the ID of the parent {@link com.eql.cda.track.flow.entity.Version}.
     * @return a list of unresolved {@link Annotation} entities.
     */
    List<Annotation> findByVersionIdAndIsResolvedFalse(Long versionId);
}