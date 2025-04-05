package com.eql.cda.track.flow.service.mapper;

import com.eql.cda.track.flow.dto.EnumDto;
import com.eql.cda.track.flow.dto.annotationDto.AnnotationCreateDto;
import com.eql.cda.track.flow.dto.annotationDto.AnnotationResponseDto;
import com.eql.cda.track.flow.dto.annotationDto.AnnotationUpdateDto;
import com.eql.cda.track.flow.dto.annotationDto.NewVersionAnnotationDto;
import com.eql.cda.track.flow.entity.Annotation;
import com.eql.cda.track.flow.entity.AnnotationStatus;
import com.eql.cda.track.flow.entity.Version;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AnnotationMapper {



    /**
     * Convertit un DTO de création d'annotation (pour une seule annotation) en une entité Annotation.
     * L'entité créée est liée à la Version fournie mais n'est pas encore persistée.
     * La date de création sera gérée par JPA (@PrePersist ou @CreatedDate).
     *
     * @param dto Le DTO contenant les données pour la nouvelle annotation.
     * @param version L'entité Version existante à laquelle cette annotation doit être liée.
     * @return Une nouvelle instance d'Annotation, prête à être persistée.
     * @throws IllegalArgumentException si dto ou version est null.
     */
    public Annotation fromAnnotationCreateDto(AnnotationCreateDto dto, Version version) {
        if (dto == null) {
            throw new IllegalArgumentException("AnnotationCreateDto cannot be null for mapping.");
        }
        if (version == null) {
            throw new IllegalArgumentException("Version cannot be null for mapping an annotation.");
        }

        Annotation annotation = new Annotation();

        // Lier l'annotation à sa version parente
        annotation.setVersion(version);

        // Mapper les champs depuis le DTO vers l'Entité
        annotation.setContent(dto.content());
        annotation.setTimePosition(dto.timePosition()); // Record: accès direct sans get()
        annotation.setAnnotationCategory(dto.category());
        annotation.setAnnotationStatus(dto.status());

        // Les champs comme 'id', 'creationDate', 'supressionDate' sont gérés
        // par JPA ou par défaut (null pour supressionDate).

        return annotation;
    }



    public AnnotationResponseDto toAnnotationResponseDto(Annotation annotation) {
        if (annotation == null) {
            return null;
        }
        // Attention: Assurez-vous que les données sont chargées !
        // Le service appelant doit garantir que l'entité 'annotation'
        // a ses champs et relations nécessaires (version, category, status) initialisés
        // pour éviter LazyInitializationException ici.

        EnumDto categoryDto = new EnumDto(
                annotation.getAnnotationCategory().name(),
                annotation.getAnnotationCategory().getLabel() // Assumes getLabel() existe
        );
        EnumDto statusDto = new EnumDto(
                annotation.getAnnotationStatus().name(),
                annotation.getAnnotationStatus().getLabel() // Assumes getLabel() existe
        );
        boolean isResolved = annotation.getAnnotationStatus() == AnnotationStatus.RESOLUE;
        // Gestion prudente de la relation pour l'ID de version
        Long versionId = (annotation.getVersion() != null) ? annotation.getVersion().getId() : null;

        return new AnnotationResponseDto(
                annotation.getId(),
                annotation.getContent(),
                annotation.getTimePosition(),
                annotation.getCreationDate(), // S'assurer qu'il est chargé si non null
                isResolved,
                categoryDto,
                statusDto,
                versionId
        );
    }

    public List<AnnotationResponseDto> toAnnotationResponseDtoList(List<Annotation> annotations) {
        if (annotations == null || annotations.isEmpty()) {
            return Collections.emptyList();
        }
        return annotations.stream()
                .map(this::toAnnotationResponseDto) // Appel de la méthode de la même classe
                .collect(Collectors.toList());
    }


    /**
     * Crée une NOUVELLE entité Annotation (non persistée) à partir du DTO
     * spécifiquement utilisé lors de la création de version.
     *
     * @param dto      Le DTO contenant les données de l'annotation à créer.
     * @param newVersion L'entité Version (la NOUVELLE version) à laquelle cette annotation sera associée.
     * @return Une nouvelle instance d'Annotation, prête à être persistée (avec la Version liée).
     */
    public Annotation fromNewVersionAnnotationDto(NewVersionAnnotationDto dto, Version newVersion) {
        if (dto == null || newVersion == null) {
            // Lever une exception ou retourner null selon ta gestion d'erreur
            throw new IllegalArgumentException("DTO or newVersion cannot be null for mapping");
        }
        Annotation ann = new Annotation();
        ann.setVersion(newVersion); // Association cruciale à la NOUVELLE version
        ann.setContent(dto.content());
        ann.setTimePosition(dto.timePosition());
        ann.setAnnotationCategory(dto.category());
        ann.setAnnotationStatus(dto.status());

        // Note: creationDate sera géré par @PrePersist ou @CreatedDate
        // Note: supressionDate est null par défaut
        // Note: sourceAnnotationId n'est pas mappé vers l'entité ici

        return ann;
    }


    /**
     * Mappe une liste de NewVersionAnnotationDto en une liste d'entités Annotation.
     */
    public List<Annotation> fromNewVersionAnnotationDtoList(List<NewVersionAnnotationDto> dtos, Version newVersion) {
        if (dtos == null || dtos.isEmpty()) {
            return Collections.emptyList();
        }
        if (newVersion == null) {
            throw new IllegalArgumentException("New Version entity cannot be null for list mapping");
        }
        return dtos.stream()
                .map(dto -> fromNewVersionAnnotationDto(dto, newVersion)) // Appelle la méthode pour chaque DTO
                .collect(Collectors.toList());
    }



    public void updateAnnotationFromDto(AnnotationUpdateDto dto, Annotation entityToUpdate) {
        if (dto == null || entityToUpdate == null) {
            return; // Ou lever exception
        }

        // Met à jour seulement si la valeur est fournie dans le DTO (non-null)
        if (dto.content() != null) {
            entityToUpdate.setContent(dto.content());
        }
        if (dto.timePosition() != null) { // Attention: comment gérer la mise à null explicite ? Parfois on envoie une valeur spéciale ou une structure différente pour PATCH. Pour l'instant, null = "ne pas changer". Si timePosition est dans le DTO mais null, cela signifierait "supprimer la timePosition"? A définir. On va supposer que null dans le DTO = "ne pas changer" pour l'instant.
            entityToUpdate.setTimePosition(dto.timePosition());
        }
        if (dto.category() != null) {
            entityToUpdate.setAnnotationCategory(dto.category());
        }
        if (dto.status() != null) {
            entityToUpdate.setAnnotationStatus(dto.status());
            // Mettre à jour le champ 'isResolved' si tu l'as ajouté dans l'entité (pas recommandé, dériver est mieux)
            // entityToUpdate.setResolved(dto.status() == AnnotationStatus.RESOLUE);
        }
        // Les autres champs (id, version, creationDate) ne sont généralement pas modifiés par un PATCH standard.
    }


}
