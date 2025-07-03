package com.eql.cda.track.flow.service.mapper;

import com.eql.cda.track.flow.dto.branchDto.BranchCreateDto;
import com.eql.cda.track.flow.dto.branchDto.BranchSummaryDto;
import com.eql.cda.track.flow.dto.branchDto.BranchUpdateDto;
import com.eql.cda.track.flow.entity.Branch;
import org.springframework.stereotype.Component;

/**
 * A component responsible for converting between {@link Branch} entities
 * and their corresponding Data Transfer Objects (DTOs).
 * This mapper is stateless and does not perform database lookups.
 */
@Component
public class BranchMapper {

    /**
     * Converts a {@link BranchCreateDto} to a new {@link Branch} entity.
     * Note that this method does not set the parent branch entity; this is the
     * responsibility of the service layer, which must fetch the parent entity by its ID.
     *
     * @param dto The source {@link BranchCreateDto} object.
     * @return A new {@link Branch} entity with basic information mapped, or {@code null} if the input is null.
     */
    public Branch toEntity(BranchCreateDto dto) {
        if (dto == null) {
            return null;
        }
        Branch entity = new Branch();
        entity.setName(dto.getBranchName());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    /**
     * Applies updates from a {@link BranchUpdateDto} to an existing {@link Branch} entity.
     * This method performs partial updates; it only changes fields in the entity
     * if they are not null in the source DTO. The parent branch is not handled here
     * and must be managed by the service layer.
     *
     * @param dto The source {@link BranchUpdateDto} containing update data.
     * @param entity The target {@link Branch} entity to be modified.
     */
    public void updateFromDto(BranchUpdateDto dto, Branch entity) {
        if (dto == null || entity == null) {
            return;
        }
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
    }

    /**
     * Converts a {@link Branch} entity to a {@link BranchSummaryDto}.
     * It correctly handles the mapping of the parent branch ID.
     *
     * @param entity The source {@link Branch} entity.
     * @return A {@link BranchSummaryDto}, or {@code null} if the input entity is null.
     */
    public BranchSummaryDto toSummaryDto(Branch entity) {
        if (entity == null) {
            return null;
        }

        Long parentId = (entity.getParent() != null) ? entity.getParent().getId() : null;

        BranchSummaryDto dto = new BranchSummaryDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setParentBrancheId(parentId);

        return dto;
    }
}