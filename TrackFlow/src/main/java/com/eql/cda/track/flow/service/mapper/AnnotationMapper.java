package com.eql.cda.track.flow.service.mapper;

import com.eql.cda.track.flow.dto.annotationDto.AnnotationCreateDto;
import com.eql.cda.track.flow.dto.annotationDto.AnnotationUpdateDto;
import com.eql.cda.track.flow.dto.annotationDto.AnnotationViewDto;
import com.eql.cda.track.flow.entity.Annotation;
import com.eql.cda.track.flow.entity.AnnotationCategory;
import com.eql.cda.track.flow.entity.AnnotationStatus;
import com.eql.cda.track.flow.entity.Version;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A mapper component responsible for converting between Annotation entities and their corresponding DTOs.
 */
@Component
public class AnnotationMapper {

    /**
     * Finds an AnnotationCategory by its label, ignoring case.
     * @param label The label to search for (e.g., "Mixage").
     * @return The matching AnnotationCategory enum constant.
     * @throws IllegalArgumentException if no match is found.
     */
    private AnnotationCategory findCategoryByLabel(String label) {
        return Arrays.stream(AnnotationCategory.values())
                .filter(category -> category.getLabel().equalsIgnoreCase(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid category label provided: " + label));
    }

    /**
     * Finds an AnnotationStatus by its label, ignoring case.
     * @param label The label to search for (e.g., "À traiter").
     * @return The matching AnnotationStatus enum constant.
     * @throws IllegalArgumentException if no match is found.
     */
    private AnnotationStatus findStatusByLabel(String label) {
        return Arrays.stream(AnnotationStatus.values())
                .filter(status -> status.getLabel().equalsIgnoreCase(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid status label provided: " + label));
    }

    /**
     * Converts an {@link AnnotationCreateDto} to a new {@link Annotation} entity.
     * It parses the string representations of category and status into their corresponding enum types.
     *
     * @param dto     the source DTO containing the data for the new annotation.
     * @param version the parent Version entity to associate with the new annotation.
     * @return a new {@link Annotation} instance, ready to be persisted.
     */
    public Annotation fromCreateDto(AnnotationCreateDto dto, Version version) {
        Annotation annotation = new Annotation();
        annotation.setVersion(version);
        annotation.setContent(dto.content());
        annotation.setTimePosition(dto.timePosition());

        AnnotationCategory categoryEnum = findCategoryByLabel(dto.category());
        AnnotationStatus statusEnum = findStatusByLabel(dto.status());

        annotation.setAnnotationCategory(categoryEnum);
        annotation.setAnnotationStatus(statusEnum);
        annotation.setResolved(statusEnum == AnnotationStatus.RESOLUE);

        return annotation;
    }

    /**
     * Updates an existing {@link Annotation} entity with data from an {@link AnnotationUpdateDto}.
     * It parses string values from the DTO into enum types before updating the entity.
     * Only non-null fields from the DTO will be applied to the entity.
     *
     * @param dto    the source DTO containing the update data.
     * @param entity the target {@link Annotation} entity to update.
     */
    public void updateFromDto(AnnotationUpdateDto dto, Annotation entity) {
        if (dto.content() != null) {
            entity.setContent(dto.content());
        }

        if (dto.category() != null) {
            entity.setAnnotationCategory(findCategoryByLabel(dto.category()));
        }

        if (dto.status() != null) {
            AnnotationStatus statusEnum = findStatusByLabel(dto.status());
            entity.setAnnotationStatus(statusEnum);
            entity.setResolved(statusEnum == AnnotationStatus.RESOLUE);
        }
    }
    /**
     * Converts an {@link Annotation} entity to a detailed {@link AnnotationViewDto}.
     *
     * @param entity the source {@link Annotation} entity.
     * @return an {@link AnnotationViewDto} representing the entity.
     */
    public AnnotationViewDto toViewDto(Annotation entity) {
        AnnotationViewDto dto = new AnnotationViewDto();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setTimePosition(entity.getTimePosition());
        dto.setResolved(entity.isResolved());
        dto.setCreationDate(entity.getCreationDate());
        dto.setAnnotationCategory(entity.getAnnotationCategory().getLabel());
        dto.setAnnotationStatus(entity.getAnnotationStatus().getLabel());
        return dto;
    }




}