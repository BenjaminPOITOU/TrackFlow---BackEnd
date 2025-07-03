package com.eql.cda.track.flow.service.mapper;

import com.eql.cda.track.flow.dto.versionDto.VersionCreateDto;
import com.eql.cda.track.flow.dto.versionDto.VersionSummaryDto;
import com.eql.cda.track.flow.dto.versionDto.VersionUpdateDto;
import com.eql.cda.track.flow.dto.versionDto.VersionViewDto;
import com.eql.cda.track.flow.entity.Version;
import com.eql.cda.track.flow.entity.VersionInstrument;
import com.eql.cda.track.flow.entity.VersionInstrumentPreDefined;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class VersionMapper {

    private final AnnotationMapper annotationMapper;

    /**
     * A component responsible for converting between {@link Version} entities
     * and their corresponding Data Transfer Objects (DTOs).
     */
    public VersionMapper(AnnotationMapper annotationMapper) {
        this.annotationMapper = annotationMapper;
    }

    private VersionInstrumentPreDefined findInstrumentByIdentifier(String identifier) {
        return Arrays.stream(VersionInstrumentPreDefined.values())
                .filter(instrument -> instrument.getLabel().equalsIgnoreCase(identifier) || instrument.name().equalsIgnoreCase(identifier))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid instrument identifier provided: " + identifier));
    }

    public Version toEntity(VersionCreateDto dto) {
        if (dto == null) {
            return null;
        }
        Version entity = new Version();
        entity.setName(dto.getVersionName());
        entity.setParentVersionId(dto.getParentVersionId());
        entity.setAudioFileUrl(dto.getAudioFileUrl());
        entity.setDurationSeconds(dto.getDurationSeconds());
        entity.setBpm(dto.getBpm());
        entity.setKey(dto.getKey());
        entity.setMetadata(dto.getMetadata());

        if (dto.getVersionInstrumentPreDefinedList() != null) {
            entity.setInstruments(
                    dto.getVersionInstrumentPreDefinedList().stream()
                            .map(this::findInstrumentByIdentifier)
                            .map(instrumentEnum -> new VersionInstrument(entity, instrumentEnum))
                            .collect(Collectors.toSet())
            );
        }

        return entity;
    }

    public VersionSummaryDto toSummaryDto(Version entity) {
        if (entity == null) {
            return null;
        }
        VersionSummaryDto dto = new VersionSummaryDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCreatedDate(entity.getCreatedDate());

        if (entity.getBranch() != null) {
            dto.setBranchName(entity.getBranch().getName());
        }

        return dto;
    }

    public VersionViewDto toViewDto(Version entity) {
        if (entity == null) {
            return null;
        }
        VersionViewDto dto = new VersionViewDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAuthor(entity.getAuthor());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setAudioFileUrl(entity.getAudioFileUrl());
        dto.setDurationSeconds(entity.getDurationSeconds());
        dto.setBpm(entity.getBpm());
        dto.setKey(entity.getKey());
        dto.setParentVersionId(entity.getParentVersionId());

        if (entity.getBranch() != null) {
            dto.setBranchId(entity.getBranch().getId());
            dto.setBranchName(entity.getBranch().getName());
            dto.setBranchDescription(entity.getBranch().getDescription());
        }

        if (entity.getInstruments() != null) {
            dto.setInstruments(
                    entity.getInstruments().stream()
                            .map(VersionInstrument::getInstrument)
                            .collect(Collectors.toSet())
            );
        }

        if (entity.getAnnotations() != null) {
            dto.setAnnotations(
                    entity.getAnnotations().stream()
                            .filter(anno -> !anno.isResolved())
                            .map(annotationMapper::toViewDto)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    public void updateFromDto(VersionUpdateDto dto, Version entity) {
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getBpm() != null) {
            entity.setBpm(dto.getBpm());
        }
        if (dto.getKey() != null) {
            entity.setKey(dto.getKey());
        }
        if (dto.getInstruments() != null) {
            synchronizeInstruments(entity, dto.getInstruments());
        }
    }

    private void synchronizeInstruments(Version version, Set<String> instrumentIdentifiers) {
        version.getInstruments().clear();

        Set<VersionInstrument> newVersionInstruments = instrumentIdentifiers.stream()
                .map(this::findInstrumentByIdentifier)
                .map(instrumentEnum -> new VersionInstrument(version, instrumentEnum))
                .collect(Collectors.toSet());

        version.getInstruments().addAll(newVersionInstruments);
    }
}