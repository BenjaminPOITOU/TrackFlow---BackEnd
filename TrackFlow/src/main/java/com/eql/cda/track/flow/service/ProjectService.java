package com.eql.cda.track.flow.service;

import com.eql.cda.track.flow.dto.projectDto.ProjectCreateDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectSummaryDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectUpdateDto;
import com.eql.cda.track.flow.entity.Project;
import com.eql.cda.track.flow.dto.projectDto.ProjectViewDto;

import java.util.List;

public interface ProjectService {
    ProjectCreateDto createProject(Long userId, ProjectCreateDto projectCreateDto);
    List<ProjectViewDto> getAllProjects(Long userId);
    ProjectViewDto getCurrentProjectInfo(Long id);
    List<ProjectSummaryDto> getRecentProjects();
    void updateProject(Long id, ProjectUpdateDto projectUpdateDto);
    void archiveProject(long id);
    void deleteProject(long id);
}
