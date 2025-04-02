package com.eql.cda.track.flow.repository;

import com.eql.cda.track.flow.entity.Composition;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CompositionRepository extends JpaRepository <Composition, Long> {

    List<Composition> findByProjectId(Long projectId);


    @Query("SELECT c FROM Composition c WHERE c.project.user.id = :userId")
    Page<Composition> findAllByProjectUserId(@Param("userId") Long userId, Pageable pageable);




    Optional<Composition> findById(Long compositionId);

}
