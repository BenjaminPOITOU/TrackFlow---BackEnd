package com.eql.cda.track.flow.service.mapper;


import com.eql.cda.track.flow.dto.versionDto.VersionResponseDto;
import com.eql.cda.track.flow.entity.Branch;
import com.eql.cda.track.flow.entity.Composition;
import com.eql.cda.track.flow.entity.Project;
import com.eql.cda.track.flow.entity.User;
import com.eql.cda.track.flow.entity.Version;
import com.eql.cda.track.flow.entity.VersionInstrument;
import com.eql.cda.track.flow.entity.VersionInstrumentPreDefined;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VersionMapper {




    public VersionResponseDto toVersionResponseDto(Version version) {
        if (version == null) {
            return null;
        }

        VersionResponseDto dto = new VersionResponseDto();

        // --- Champs directs de Version ---
        dto.setId(version.getId());
        dto.setName(version.getName());
        dto.setAudioFileUrl(version.getAudioFileUrl());
        dto.setCreatedDate(version.getCreatedDate());
        dto.setDurationSeconds(version.getDurationSeconds());
        dto.setBpm(version.getBpm());
        dto.setKey(version.getKey());

        // --- Initialisation et copie de Metadata (CORRECTION ICI) ---
        Map<String, String> meta = version.getMetadata();
        if (meta != null) {
            // Force l'initialisation PENDANT la transaction
            Hibernate.initialize(meta);
            // Crée une nouvelle map dans le DTO pour éviter de passer le proxy Hibernate
            dto.setMetadata(new HashMap<>(meta)); // Copie défensive
        } else {
            dto.setMetadata(Collections.emptyMap()); // Ou null si tu préfères
        }


        // --- Initialisation et copie des Instruments ---
        // (Déjà fait via la méthode helper, mais ajoutons Hibernate.initialize pour la robustesse)
        Set<VersionInstrument> instruments = version.getInstruments();
        if (instruments != null) {
            Hibernate.initialize(instruments); // Force init de la collection principale
            // La méthode mapInstrumentsToStrings accédera aux éléments, les initialisant aussi
            Set<String> instrumentNames = mapInstrumentsToStrings(instruments);
            dto.setInstruments(instrumentNames); // mapInstrumentsToStrings retourne déjà un nouveau Set
        } else {
            dto.setInstruments(Collections.emptySet());
        }


        // --- Champs liés via Branch, Composition, Project, User ---
        Branch branch = version.getBranch();
        if (branch != null) {
            // Pas besoin d'initialiser les relations ToOne LAZY ici si on
            // accède seulement à leurs IDs ou champs simples immédiatement.
            // Si tu accédais à une collection LAZY de Branch (ex: branch.getOtherVersions()),
            // il faudrait aussi l'initialiser ici.
            dto.setBranchId(branch.getId());
            dto.setBranchName(branch.getName());
            dto.setBranchDescription(branch.getDescription());

            Composition composition = branch.getComposition();
            if (composition != null) {
                Project project = composition.getProject();
                if (project != null) {
                    User user = project.getUser();
                    if (user != null) {
                        dto.setAuthor(user.getLogin());
                    }
                }
            }
        }
        // ... gestion des valeurs null par défaut si besoin ...

        return dto;
    }

    // La méthode mapInstrumentsToStrings reste la même
    private Set<String> mapInstrumentsToStrings(Set<VersionInstrument> instruments) {
        if (instruments == null || instruments.isEmpty()) {
            return Collections.emptySet();
        }
        // Pas besoin de Hibernate.initialize ici si la collection a déjà été initialisée avant l'appel
        return instruments.stream()
                .map(VersionInstrument::getInstrument)
                .map(VersionInstrumentPreDefined::name)
                .collect(Collectors.toSet());
    }

}
