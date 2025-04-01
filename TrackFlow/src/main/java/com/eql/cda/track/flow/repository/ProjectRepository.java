package com.eql.cda.track.flow.repository;

import com.eql.cda.track.flow.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findById(Long id);
    List<Project> findAll();



}
