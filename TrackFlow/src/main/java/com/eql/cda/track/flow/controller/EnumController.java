package com.eql.cda.track.flow.controller;



import com.eql.cda.track.flow.dto.EnumDto; // Importe le DTO créé
// Importe TOUTES tes énumérations
import com.eql.cda.track.flow.entity.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/enums") // Chemin de base pour toutes les énumérations
public class EnumController {

    // Méthode générique (interne) pour convertir un Enum en EnumDto
    private <E extends Enum<E>> EnumDto mapToEnumDto(E enumConstant, String label) {
        return new EnumDto(enumConstant.name(), label);
    }

    // --- Endpoints pour les Enums de Project ---

    @GetMapping("/project-commercial-status")
    public ResponseEntity<List<EnumDto>> getProjectCommercialStatus() {
        List<EnumDto> values = Arrays.stream(ProjectCommercialStatus.values())
                .map(e -> mapToEnumDto(e, e.getLabel()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(values);
    }

    @GetMapping("/project-musical-gender")
    public ResponseEntity<List<EnumDto>> getProjectMusicalGenders() {
        List<EnumDto> values = Arrays.stream(ProjectMusicalGenderPreDefined.values())
                .map(e -> mapToEnumDto(e, e.getLabel()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(values);
    }

    @GetMapping("/project-purpose")
    public ResponseEntity<List<EnumDto>> getProjectPurposes() {
        List<EnumDto> values = Arrays.stream(ProjectPurpose.values())
                .map(e -> mapToEnumDto(e, e.getLabel()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(values);
    }

    @GetMapping("/project-status")
    public ResponseEntity<List<EnumDto>> getProjectStatus() {
        List<EnumDto> values = Arrays.stream(ProjectStatus.values())
                .map(e -> mapToEnumDto(e, e.getLabel()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(values);
    }

    @GetMapping("/project-type")
    public ResponseEntity<List<EnumDto>> getProjectTypes() {
        List<EnumDto> values = Arrays.stream(ProjectType.values())
                .map(e -> mapToEnumDto(e, e.getLabel()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(values);
    }

    // --- Endpoint pour l'Enum de Composition ---

    @GetMapping("/composition-status")
    public ResponseEntity<List<EnumDto>> getCompositionStatus() {
        List<EnumDto> values = Arrays.stream(CompositionStatus.values())
                .map(e -> mapToEnumDto(e, e.getLabel()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(values);
    }

    // --- Endpoint pour l'Enum de User ---

    @GetMapping("/user-roles")
    public ResponseEntity<List<EnumDto>> getUserRoles() {
        List<EnumDto> values = Arrays.stream(UserRole.values())
                .map(e -> mapToEnumDto(e, e.getLabel()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(values);
    }

    // --- Endpoint pour l'Enum de Version (Instruments) ---

    @GetMapping("/version-instruments")
    public ResponseEntity<List<EnumDto>> getVersionInstruments() {
        List<EnumDto> values = Arrays.stream(VersionInstrumentPreDefined.values())
                .map(e -> mapToEnumDto(e, e.getLabel()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(values);
    }
}