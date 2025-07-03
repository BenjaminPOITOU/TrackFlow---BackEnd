package com.eql.cda.track.flow.service.mapper;


import com.eql.cda.track.flow.dto.projectDto.ProjectCreateDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectSummaryDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectUpdateDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectViewDto;
import com.eql.cda.track.flow.entity.Project;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A manual mapper component to convert between Project entities and their DTOs.
 */
@Component
public class ProjectMapper {

    /**
     * Converts a ProjectCreateDto to a new Project entity.
     * Note: The 'user' and date fields are not set here; they must be set in the service layer.
     * @param dto The source DTO.
     * @return The resulting Project entity.
     */
    public Project toEntity(ProjectCreateDto dto) {
        if (dto == null) {
            return null;
        }
        Project entity = new Project();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setIllustration(dto.getIllustration());
        entity.setProjectStatus(dto.getProjectStatus());
        entity.setProjectType(dto.getProjectType());
        entity.setProjectCommercialStatus(dto.getProjectCommercialStatus());
        entity.setProjectPurposes(dto.getProjectPurposes());
        entity.setProjectMusicalGendersPreDefined(dto.getProjectMusicalGendersPreDefined());
        return entity;
    }

    /**
     * Converts a Project entity to a ProjectViewDto for detailed display.
     * @param entity The source entity.
     * @return The resulting ProjectViewDto.
     */
    public ProjectViewDto toProjectViewDto(Project entity) {
        if (entity == null) {
            return null;
        }
        ProjectViewDto dto = new ProjectViewDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setIllustration(entity.getIllustration());
        dto.setProjectStatus(entity.getProjectStatus());
        dto.setProjectType(entity.getProjectType());
        dto.setProjectCommercialStatus(entity.getProjectCommercialStatus());
        dto.setProjectPurposes(entity.getProjectPurposes());
        dto.setProjectMusicalGendersPreDefined(entity.getProjectMusicalGendersPreDefined());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdateDate(entity.getLastUpdateDate());

        int compositionsTotal = (entity.getCompositions() != null) ? entity.getCompositions().size() : 0;
        dto.setCompositionsTotal(compositionsTotal);

        return dto;
    }

    /**
     * Converts a Project entity to a ProjectSummaryDto for list display.
     * @param entity The source entity.
     * @return The resulting ProjectSummaryDto.
     */
    public ProjectSummaryDto toProjectSummaryDto(Project entity) {
        if (entity == null) {
            return null;
        }
        ProjectSummaryDto dto = new ProjectSummaryDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setProjectStatus(entity.getProjectStatus());
        dto.setCreatedDate(entity.getCreatedDate());

        if (entity.getProjectMusicalGendersPreDefined() != null) {
            List<String> genderNames = entity.getProjectMusicalGendersPreDefined().stream()
                    .map(Enum::name)
                    .collect(Collectors.toList());
            dto.setProjectMusicalGendersPreDefined(genderNames);
        } else {
            dto.setProjectMusicalGendersPreDefined(Collections.emptyList());
        }

        return dto;
    }

    /**
     * Updates an existing Project entity from a ProjectUpdateDto.
     * It only updates non-null fields from the DTO.
     * @param dto The source DTO with update data.
     * @param project The target entity to be updated.
     */
    public void updateProjectFromDto(ProjectUpdateDto dto, Project project) {
        if (dto == null || project == null) {
            return;
        }

        if (dto.getTitle() != null) {
            project.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            project.setDescription(dto.getDescription());
        }
        if (dto.getIllustration() != null) {
            project.setIllustration(dto.getIllustration());
        }
        if (dto.getProjectStatus() != null) {
            project.setProjectStatus(dto.getProjectStatus());
        }
        if (dto.getProjectType() != null) {
            project.setProjectType(dto.getProjectType());
        }
        if (dto.getProjectCommercialStatus() != null) {
            project.setProjectCommercialStatus(dto.getProjectCommercialStatus());
        }
        if (dto.getProjectPurposes() != null) {
            project.setProjectPurposes(dto.getProjectPurposes());
        }
        if (dto.getProjectMusicalGendersPreDefined() != null) {
            project.setProjectMusicalGendersPreDefined(dto.getProjectMusicalGendersPreDefined());
        }
    }
}