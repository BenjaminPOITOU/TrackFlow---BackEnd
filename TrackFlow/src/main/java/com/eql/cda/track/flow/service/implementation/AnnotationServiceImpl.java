package com.eql.cda.track.flow.service.implementation;

import com.eql.cda.track.flow.dto.annotationDto.AnnotationCreateDto;
import com.eql.cda.track.flow.dto.annotationDto.AnnotationResponseDto;
import com.eql.cda.track.flow.dto.annotationDto.AnnotationUpdateDto;
import com.eql.cda.track.flow.dto.annotationDto.UserRecentAnnotationDto;
import com.eql.cda.track.flow.entity.Annotation;
import com.eql.cda.track.flow.entity.Composition;
import com.eql.cda.track.flow.entity.User;
import com.eql.cda.track.flow.entity.Version;
import com.eql.cda.track.flow.repository.AnnotationRepository;
import com.eql.cda.track.flow.repository.UserRepository;
import com.eql.cda.track.flow.repository.VersionRepository;
import com.eql.cda.track.flow.service.AnnotationService;
import com.eql.cda.track.flow.service.mapper.AnnotationMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;


@Service
public class AnnotationServiceImpl implements AnnotationService {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationServiceImpl.class);

    private final AnnotationRepository annotationRepository;
    private final AnnotationMapper annotationMapper;
    private final VersionRepository versionRepository;
    private final UserRepository userRepository;// Pour trouver la Version associée

    @Autowired
    public AnnotationServiceImpl(AnnotationRepository annotationRepository, AnnotationMapper annotationMapper, VersionRepository versionRepository, UserRepository userRepository) {
        this.annotationRepository = annotationRepository;
        this.annotationMapper = annotationMapper;
        this.versionRepository = versionRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public AnnotationResponseDto createAnnotation(AnnotationCreateDto createDto, Long versionId) {

        Version version = versionRepository.findById(versionId)
                .orElseThrow(() -> new EntityNotFoundException("Version not found with id: " + versionId));


        Annotation newAnnotation = annotationMapper.fromAnnotationCreateDto(createDto, version);
        Annotation savedAnnotation = annotationRepository.save(newAnnotation);

        return annotationMapper.toAnnotationResponseDto(savedAnnotation);
    }


    @Override
    @Transactional // Optimisation pour la lecture
    public AnnotationResponseDto getAnnotationById(Long annotationId) {
        // 1. Interaction BDD
        Annotation annotation = annotationRepository.findById(annotationId)
                .orElseThrow(() -> new EntityNotFoundException("Annotation not found with id: " + annotationId));


        // 2. Déléguer la transformation Entité -> DTO au Mapper
        return annotationMapper.toAnnotationResponseDto(annotation);
    }

    @Override
    @Transactional
    public AnnotationResponseDto updateAnnotation(Long annotationId, AnnotationUpdateDto updateDto) {
        // 1. Interaction BDD : Récupérer l'entité existante
        Annotation existingAnnotation = annotationRepository.findById(annotationId)
                .orElseThrow(() -> new EntityNotFoundException("Annotation not found with id: " + annotationId));

        // 2. Déléguer la mise à jour des champs au Mapper
        //    Le mapper modifie l'objet 'existingAnnotation' directement
        annotationMapper.updateAnnotationFromDto(updateDto, existingAnnotation);

        // 3. Interaction BDD : La sauvegarde est souvent implicite avec @Transactional
        //    car l'entité est managée, mais un save() explicite est parfois plus clair/sûr.
        // Annotation updatedAnnotation = annotationRepository.save(existingAnnotation); // Optionnel si @Transactional gère bien

        // 4. Déléguer la transformation Entité -> DTO de réponse au Mapper
        return annotationMapper.toAnnotationResponseDto(existingAnnotation); // Utiliser l'entité modifiée
    }

    @Override
    public void deleteAnnotation(Long annotationId) {

        logger.info("Attempting to soft delete annotation with ID: {}", annotationId);

        // 1. Vérifier si l'annotation existe
        //    (Important pour éviter des erreurs silencieuses et pour la sécurité future)
        //    @Where clause sur l'entité filtrera déjà les supprimés, donc existsById est correct ici.
        if (!annotationRepository.existsById(annotationId)) {
            logger.warn("Annotation not found with ID: {}. Cannot delete.", annotationId);
            throw new EntityNotFoundException("Annotation not found with id: " + annotationId);
        }

        // 2. Appeler la méthode delete du repository.
        //    Grâce à @SQLDelete sur l'entité Annotation, Hibernate exécutera
        //    l'UPDATE défini au lieu d'un vrai DELETE.
        try {
            annotationRepository.deleteById(annotationId);
            logger.info("Annotation with ID: {} marked as deleted (soft delete).", annotationId);
        } catch (Exception e) {
            // Attraper des exceptions potentielles (ex: problème de base de données)
            logger.error("Error occurred while attempting to soft delete annotation ID {}: {}", annotationId, e.getMessage(), e);
            // Relancer une exception spécifique à l'application si nécessaire
            throw new RuntimeException("Could not soft delete annotation with id " + annotationId, e);
        }

    }

    @Override
    @Transactional
    public List<AnnotationResponseDto> getAllAnnotationsByVersionIdSorted(Long versionId) {
        logger.debug("Fetching ALL sorted annotations for version ID: {}", versionId);

        // 1. Vérifier si la version existe (bonne pratique)
        if (!versionRepository.existsById(versionId)) {
            logger.warn("Cannot fetch annotations: Version {} not found.", versionId);
            throw new EntityNotFoundException("Version not found with id: " + versionId);
        }

        // 2. Appeler la nouvelle méthode du repository qui retourne une List triée
        List<Annotation> annotations = annotationRepository.findByVersionIdOrderByCreationDateDesc(versionId);
        logger.debug("Found {} annotations for version {}", annotations.size(), versionId);

        // 3. Mapper la liste d'entités en liste de DTOs
        return annotations.stream()
                .map(annotationMapper::toAnnotationResponseDto) // Utilise ton mapper
                .toList(); // Ou .collect(Collectors.toList()) pour Java < 16
    }


    @Override
    @Transactional
    public Page<UserRecentAnnotationDto> findRecentAnnotationsForUser(String login, Pageable pageable) {
        logger.info("Finding recent annotations for user: {} with pageable: {}", login, pageable);

        // 1. Trouver l'entité User par son login (ou email, etc.)
        //    Lance une exception si l'utilisateur n'est pas trouvé.
        User user = userRepository.findByLogin(login) // NOUVELLE LIGNE (utilise le nom de méthode corrigé)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with login: " + login));
        logger.debug("User found with ID: {}", user.getId()); // Ne pas logger d'infos sensibles

        Page<Annotation> annotationPage = annotationRepository.findRecentAnnotationsByUserWithDetails(user, pageable);
        logger.debug("Found {} annotations on page {} for user {}", annotationPage.getNumberOfElements(), annotationPage.getNumber(), login);

        // 3. Mapper la Page<Annotation> en Page<UserRecentAnnotationDto>
        //    La méthode 'map' de l'objet Page permet d'appliquer une fonction de mapping à chaque élément.
        return annotationPage.map(this::mapToUserRecentAnnotationDtoHelper);
    }
    /**
     * Méthode privée helper pour convertir une entité Annotation en UserRecentAnnotationDto.
     * Cette logique pourrait aussi être placée dans AnnotationMapper.
     *
     * @param annotation L'entité Annotation à convertir.
     * @return Le DTO UserRecentAnnotationDto correspondant.
     */
    private UserRecentAnnotationDto mapToUserRecentAnnotationDtoHelper(Annotation annotation) {
        // Grâce aux JOIN FETCH dans la requête du repository, les entités liées
        // devraient être chargées et ne pas causer de requêtes supplémentaires (N+1).
        Version version = annotation.getVersion();
        // Accès prudent aux relations pour éviter les NullPointerException si les données sont incohérentes
        Composition composition = (version != null && version.getBranch() != null)
                ? version.getBranch().getComposition()
                : null;

        // Préparer les IDs et noms avec des valeurs par défaut si les entités liées manquent
        Long versionId = (version != null) ? version.getId() : null;
        String versionName = (version != null) ? version.getName() : "Version Inconnue"; // Ou null selon ton choix
        Long compositionId = (composition != null) ? composition.getId() : null;
        String compositionName = (composition != null && composition.getTitle() != null)
                ? composition.getTitle() // Assure-toi que Composition a un getter getName()
                : "Composition Inconnue"; // Ou null

        // Construire le DTO
        return new UserRecentAnnotationDto(
                annotation.getId(),
                annotation.getContent(),
                annotation.getTimePosition(), // Peut être null
                annotation.getAnnotationStatus(),
                annotation.getAnnotationCategory(),
                annotation.getCreationDate(), // Assure-toi que Annotation a getCreationDate()
                versionId,
                versionName,
                compositionId,
                compositionName
        );
    }

}

