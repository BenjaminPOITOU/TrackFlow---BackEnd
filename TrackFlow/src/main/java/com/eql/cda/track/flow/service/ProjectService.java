package com.eql.cda.track.flow.service;

import com.eql.cda.track.flow.dto.projectDto.ProjectCreateDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectSummaryDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectUpdateDto;
import com.eql.cda.track.flow.entity.Project;
import com.eql.cda.track.flow.dto.projectDto.ProjectViewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {
    ProjectCreateDto createProject(Long userId, ProjectCreateDto projectCreateDto);
    ProjectViewDto getCurrentProjectInfo(Long id);
    Page<ProjectViewDto> getAllProjectsPaginated(Long userId, Pageable pageable);
    Page<ProjectSummaryDto> findRecentProjectsForUser(String username, Pageable pageable);
    void updateProject(Long id, ProjectUpdateDto projectUpdateDto);
    void archiveProject(long id);
    void deleteProject(long id);
}
