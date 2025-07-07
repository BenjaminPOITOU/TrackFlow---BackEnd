package com.eql.cda.track.flow.controller;

import com.eql.cda.track.flow.dto.EnumDto;
import com.eql.cda.track.flow.entity.AnnotationCategory;
import com.eql.cda.track.flow.entity.AnnotationStatus;
import com.eql.cda.track.flow.entity.CompositionStatus;
import com.eql.cda.track.flow.entity.ProjectCommercialStatus;
import com.eql.cda.track.flow.entity.ProjectMusicalGenderPreDefined;
import com.eql.cda.track.flow.entity.ProjectPurpose;
import com.eql.cda.track.flow.entity.ProjectStatus;
import com.eql.cda.track.flow.entity.ProjectType;
import com.eql.cda.track.flow.entity.VersionInstrumentPreDefined;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/enums")
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

    /**
     * Point de terminaison unique pour récupérer toutes les énumérations
     * liées aux projets.
     *
     * @return Une Map contenant toutes les listes d'enums nécessaires pour le formulaire de création de projet.
     */
    /**
     * Point de terminaison unique pour récupérer toutes les énumérations
     * liées aux projets.
     *
     * @return Une Map contenant toutes les listes d'enums nécessaires pour le formulaire de création de projet.
     */
    @GetMapping("/projects")
    public Map<String, List<EnumDto>> getAllProjectEnums() {
        Map<String, List<EnumDto>> allEnums = new HashMap<>();

        allEnums.put("types", enumToDtoList(ProjectType.class));
        allEnums.put("commercialStatuses", enumToDtoList(ProjectCommercialStatus.class));
        allEnums.put("musicalGenders", enumToDtoList(ProjectMusicalGenderPreDefined.class));
        allEnums.put("purposes", enumToDtoList(ProjectPurpose.class));
        allEnums.put("statuses", enumToDtoList(ProjectStatus.class)); // Corresponds à project-statuses dans votre JS

        return allEnums;
    }

    @GetMapping("/composition-statuses")
    public List<EnumDto> getCompositionStatuses() {
        return enumToDtoList(CompositionStatus.class);
    }


    @GetMapping("/annotation-categories")
    public List<EnumDto> getAnnotationCategories() {
        return enumToDtoList(AnnotationCategory.class);
    }
    @GetMapping("/annotation-statuses")
    public List<EnumDto> getAnnotationStatuses() {
        return enumToDtoList(AnnotationStatus.class);
    }

    /**
     * Retrieves all predefined instruments available for a version.
     * @return A list of all instrument enums.
     */
    @GetMapping("/instruments")
    public List<EnumDto> getVersionInstruments() {
        return enumToDtoList(VersionInstrumentPreDefined.class);
    }

}
