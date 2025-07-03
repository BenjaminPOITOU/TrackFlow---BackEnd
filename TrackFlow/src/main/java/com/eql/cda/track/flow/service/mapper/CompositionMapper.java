package com.eql.cda.track.flow.service.mapper;


import com.eql.cda.track.flow.dto.EnumDto;
import com.eql.cda.track.flow.dto.branchDto.BranchSummaryDto;
import com.eql.cda.track.flow.dto.compositionDto.CompositionCreateDto;
import com.eql.cda.track.flow.dto.compositionDto.CompositionSummaryDto;
import com.eql.cda.track.flow.dto.compositionDto.CompositionUpdateDto;
import com.eql.cda.track.flow.dto.compositionDto.CompositionViewDto;
import com.eql.cda.track.flow.entity.Branch;
import com.eql.cda.track.flow.entity.Composition;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * A manual mapper component responsible for converting between {@link Composition} entities
 * and their corresponding Data Transfer Objects (DTOs).
 */
@Component
public class CompositionMapper {

    private final BranchMapper branchMapper;

    public CompositionMapper(BranchMapper branchMapper) {
        this.branchMapper = branchMapper;
    }

    /**
     * Converts a {@link CompositionCreateDto} to a new {@link Composition} entity.
     * This method maps the creation data to a new entity instance. The parent project
     * and audit dates must be set in the service layer.
     *
     * @param dto The source {@link CompositionCreateDto} object.
     * @return A new {@link Composition} entity, or {@code null} if the input DTO is null.
     */
    public Composition toEntity(CompositionCreateDto dto) {
        if (dto == null) {
            return null;
        }
        Composition entity = new Composition();
        entity.setTitle(dto.getTitle());
        entity.setCompositionStatus(dto.getCompositionStatus());
        entity.setSubGenders(dto.getSubGender());
        entity.setDescription(dto.getDescription());
        entity.setIllustration(dto.getIllustration());
        return entity;
    }

    /**
     * Updates an existing {@link Composition} entity from a {@link CompositionUpdateDto}.
     * This method applies partial updates; it only changes fields in the entity
     * if they are not null in the source DTO.
     *
     * @param dto The source {@link CompositionUpdateDto} containing update data.
     * @param entity The target {@link Composition} entity to be modified.
     */
    public void updateFromDto(CompositionUpdateDto dto, Composition entity) {
        if (dto == null || entity == null) {
            return;
        }
        if (dto.getTitle() != null) {
            entity.setTitle(dto.getTitle());
        }
        if (dto.getCompositionStatus() != null) {
            entity.setCompositionStatus(dto.getCompositionStatus());
        }
        if (dto.getSubGender() != null) {
            entity.setSubGenders(dto.getSubGender());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getIllustration() != null) {
            entity.setIllustration(dto.getIllustration());
        }
    }

    /**
     * Converts a {@link Composition} entity to a detailed {@link CompositionViewDto}.
     * @param entity The source {@link Composition} entity.
     * @return A {@link CompositionViewDto}, or {@code null} if the input entity is null.
     */
    public CompositionViewDto toViewDto(Composition entity) {
        if (entity == null) {
            return null;
        }
        CompositionViewDto dto = new CompositionViewDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setCompositionStatus(entity.getCompositionStatus());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastUpdateDate(entity.getLastUpdateDate());
        dto.setSubGenders(entity.getSubGenders());
        dto.setDescription(entity.getDescription());
        dto.setIllustration(entity.getIllustration());
        dto.setTotalBranches(entity.getBranches() != null ? entity.getBranches().size() : 0);
        dto.setTotalVersions(calculateTotalVersions(entity));

        if (entity.getBranches() != null) {
            dto.setBranches(entity.getBranches().stream()
                    .sorted(Comparator.comparing(Branch::getCreatedDate))
                    .map(branchMapper::toSummaryDto)
                    .collect(Collectors.toList()));
        }

        if (entity.getProject() != null) {
            dto.setProjectId(entity.getProject().getId());
            dto.setProjectTitle(entity.getProject().getTitle());
            dto.setProjectMusicalGenderPreDefinedList(entity.getProject().getProjectMusicalGendersPreDefined());
        }
        return dto;
    }



    /**
     * Converts a {@link Composition} entity to a compact {@link CompositionSummaryDto}.
     * @param entity The source {@link Composition} entity.
     * @return A {@link CompositionSummaryDto}, or {@code null} if the input entity is null.
     */
    public CompositionSummaryDto toSummaryDto(Composition entity) {
        if (entity == null) {
            return null;
        }
        CompositionSummaryDto dto = new CompositionSummaryDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setLastUpdateDate(entity.getLastUpdateDate());
        dto.setTotalBranches(entity.getBranches() != null ? entity.getBranches().size() : 0);
        dto.setTotalVersions(calculateTotalVersions(entity));

        if (entity.getCompositionStatus() != null) {
            dto.setStatus(new EnumDto(entity.getCompositionStatus().name(), entity.getCompositionStatus().getLabel()));
        }
        return dto;
    }

    private int calculateTotalVersions(Composition composition) {
        if (composition == null || composition.getBranches() == null) {
            return 0;
        }
        return composition.getBranches().stream()
                .mapToInt(branch -> branch.getVersions() != null ? branch.getVersions().size() : 0)
                .sum();
    }
}