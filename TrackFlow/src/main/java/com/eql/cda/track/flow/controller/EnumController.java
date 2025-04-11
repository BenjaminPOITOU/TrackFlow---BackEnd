package com.eql.cda.track.flow.controller;

import com.eql.cda.track.flow.dto.EnumDto;
import com.eql.cda.track.flow.entity.ProjectCommercialStatus;
import com.eql.cda.track.flow.entity.ProjectMusicalGenderPreDefined;
import com.eql.cda.track.flow.entity.ProjectPurpose;
import com.eql.cda.track.flow.entity.ProjectStatus;
import com.eql.cda.track.flow.entity.ProjectType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/enums")
@CrossOrigin(origins = "http://localhost:3000")
public class EnumController {

    private <T extends Enum<T>> List<EnumDto> enumToDtoList(Class<T> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(enumConstant -> {
                    String label;
                    try {
                        // Utilise la réflexion pour appeler getLabel() de manière générique
                        label = (String) enumClass.getMethod("getLabel").invoke(enumConstant);
                    } catch (Exception e) {
                        // Fallback au nom si getLabel() n'existe pas ou échoue
                        System.err.println("Warning: Could not invoke getLabel() on " + enumClass.getSimpleName() + "." + enumConstant.name() + ". Falling back to name(). Error: " + e.getMessage());
                        label = enumConstant.name(); // Ou une autre valeur par défaut
                    }
                    return new EnumDto(enumConstant.name(), label);
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/project-statuses")
    public List<EnumDto> getProjectStatuses() {
        return enumToDtoList(ProjectStatus.class);
    }

    @GetMapping("/project-purposes")
    public List<EnumDto> getProjectPurposes() {
        return enumToDtoList(ProjectPurpose.class);
    }

    @GetMapping("/project-types")
    public List<EnumDto> getProjectTypes() {
        return enumToDtoList(ProjectType.class);
    }

    @GetMapping("/project-musical-genders")
    public List<EnumDto> getProjectMusicalGenders() {
        return enumToDtoList(ProjectMusicalGenderPreDefined.class);
    }

    @GetMapping("/project-commercial-statuses")
    public List<EnumDto> getProjectCommercialStatuses() {
        return enumToDtoList(ProjectCommercialStatus.class);
    }
}
