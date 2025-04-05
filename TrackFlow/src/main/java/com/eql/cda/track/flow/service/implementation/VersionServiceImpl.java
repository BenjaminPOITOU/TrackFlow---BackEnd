package com.eql.cda.track.flow.service.implementation;


import com.eql.cda.track.flow.dto.AudioMetadataDto;

import com.eql.cda.track.flow.dto.annotationDto.AnnotationResponseDto;
import com.eql.cda.track.flow.dto.annotationDto.NewVersionAnnotationDto;
import com.eql.cda.track.flow.dto.branchDto.BranchSummaryDto;
import com.eql.cda.track.flow.dto.versionDto.AudioUploadResponseDto;
import com.eql.cda.track.flow.dto.versionDto.NewVersionModalDto;
import com.eql.cda.track.flow.dto.versionDto.VersionCreateDto;
import com.eql.cda.track.flow.dto.versionDto.VersionDetailDto;
import com.eql.cda.track.flow.dto.versionDto.VersionInstrumentDto;
import com.eql.cda.track.flow.dto.versionDto.VersionResponseDto;
import com.eql.cda.track.flow.dto.versionDto.VersionSummaryDto;
import com.eql.cda.track.flow.entity.Annotation;
import com.eql.cda.track.flow.entity.AnnotationStatus;
import com.eql.cda.track.flow.entity.Branch;
import com.eql.cda.track.flow.entity.Composition;
import com.eql.cda.track.flow.entity.Version;
import com.eql.cda.track.flow.entity.VersionInstrument;
import com.eql.cda.track.flow.entity.VersionInstrumentPreDefined;
import com.eql.cda.track.flow.repository.AnnotationRepository;
import com.eql.cda.track.flow.repository.BranchRepository;
import com.eql.cda.track.flow.repository.CompositionRepository;
import com.eql.cda.track.flow.repository.VersionInstrumentRepository;
import com.eql.cda.track.flow.repository.VersionRepository;
import com.eql.cda.track.flow.service.AudioMetadataService;
import com.eql.cda.track.flow.service.BranchService; // AJOUT
import com.eql.cda.track.flow.service.StorageService;
import com.eql.cda.track.flow.service.VersionNamingService; // AJOUT
import com.eql.cda.track.flow.service.VersionService;
import com.eql.cda.track.flow.service.mapper.AnnotationMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static com.eql.cda.track.flow.service.implementation.VersionNamingServiceImpl.MAIN_BRANCH_NAME;
import static com.eql.cda.track.flow.validation.Constants.VERSION_M_M_PATTERN;
import static com.eql.cda.track.flow.validation.Constants.VERSION_NAME_PATTERN;


@Service
public class VersionServiceImpl implements VersionService {

    private static final Logger logger = LogManager.getLogger(VersionServiceImpl.class);

    private final VersionRepository versionRepository;
    private final CompositionRepository compositionRepository;
    private final StorageService storageService;
    private final AudioMetadataService audioMetadataService;
    private final VersionInstrumentRepository versionInstrumentRepository;
    private final BranchService branchService;
    private final BranchRepository branchRepository;
    private final VersionNamingService versionNamingService;
    private final AnnotationRepository annotationRepository;
    private final AnnotationMapper annotationMapper;

    public VersionServiceImpl(VersionRepository versionRepository, CompositionRepository compositionRepository, StorageService storageService, AudioMetadataService audioMetadataService, VersionInstrumentRepository versionInstrumentRepository, BranchService branchService, BranchRepository branchRepository, VersionNamingService versionNamingService, AnnotationRepository annotationRepository, AnnotationMapper annotationMapper) {
        this.versionRepository = versionRepository;
        this.compositionRepository = compositionRepository;
        this.storageService = storageService;
        this.audioMetadataService = audioMetadataService;
        this.versionInstrumentRepository = versionInstrumentRepository;
        this.branchService = branchService;
        this.branchRepository = branchRepository;
        this.versionNamingService = versionNamingService;
        this.annotationRepository = annotationRepository;
        this.annotationMapper = annotationMapper;
    }

    @Override
    public AudioUploadResponseDto uploadAudioAndExtractMetadata(MultipartFile file, String desiredPathPrefix) throws Exception {

        logger.info("Starting audio upload and metadata extraction for file: {}", file.getOriginalFilename());

        // 1. Validation simple du fichier (peut être étendue)
        if (file.isEmpty()) {
            logger.warn("Upload attempt with empty file.");
            throw new IllegalArgumentException("File cannot be empty.");
        }

        // 2. Extraire les métadonnées
        AudioMetadataDto metadata;
        try {
            logger.debug("Extracting metadata from file...");
            metadata = audioMetadataService.extractMetadata(file);
            logger.info("Metadata extracted successfully: BPM={}, Duration={}s", metadata.getBpm(), metadata.getDurationSeconds());
        } catch (Exception e) {
            logger.error("Metadata extraction failed for file: {}", file.getOriginalFilename(), e);
            // Propager l'exception pour indiquer l'échec au Controller
            throw new Exception("Failed to extract audio metadata: " + e.getMessage(), e);
        }

        // 3. Générer un nom de fichier unique pour le stockage
        // 1. Générer un nom de fichier unique pour éviter les conflits d'écrasement dans GCS.
//    - file.getOriginalFilename(): Récupère le nom original du fichier (ex: "mon_audio.mp3").
//    - storageService.generateUniqueFileName(...): Utilise le nom original pour garder l'extension
//      mais remplace le nom par un identifiant aléatoire (UUID) (ex: "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx.mp3").
        String uniqueFileName = storageService.generateUniqueFileName(file.getOriginalFilename());



        // 2. Construire le chemin complet de destination DANS le bucket GCS.
//    Ce chemin combine un préfixe de dossier optionnel (`desiredPathPrefix`) et le nom de fichier unique.
//    - `desiredPathPrefix`: (Paramètre de la méthode parente, ex: `uploadAudioAndExtractMetadata`)
//      Représente une structure de dossiers souhaitée dans GCS (ex: "compositions/123/versions/").
//      C'est optionnel et doit être fourni par l'appelant de cette méthode. S'il n'est pas fourni ou est vide, aucun dossier ne sera utilisé.
//    - La logique entre parenthèses `(...)` gère ce `desiredPathPrefix`:
//        - `desiredPathPrefix != null && !desiredPathPrefix.isBlank()`: Vérifie si un préfixe a été fourni et s'il n'est pas vide.
//        - `?` : Si OUI (un préfixe est fourni)...
//            - `desiredPathPrefix.endsWith("/") ? desiredPathPrefix : desiredPathPrefix + "/"` : Vérifie si le préfixe se termine déjà par '/'.
//                - Si OUI, on le garde tel quel.
//                - Si NON, on ajoute un '/' à la fin pour séparer correctement le dossier du nom de fichier.
//        - `:` : Si NON (aucun préfixe fourni ou vide)...
//            - `""` : Utilise une chaîne vide comme préfixe (le fichier sera à la racine du bucket).
//    - `+ uniqueFileName`: Ajoute le nom de fichier unique généré à l'étape 1 à la fin du préfixe (ou de la chaîne vide).
//    - Résultat `destinationPath`: Contient le chemin complet dans le bucket (ex: "compositions/123/versions/fichier_unique.mp3" ou juste "fichier_unique.mp3").
        String destinationPath = (desiredPathPrefix != null && !desiredPathPrefix.isBlank() ? desiredPathPrefix.endsWith("/") ? desiredPathPrefix : desiredPathPrefix + "/" : "") + uniqueFileName;
        logger.debug("Generated unique destination path in bucket: {}", destinationPath);

        // 4. Uploader le fichier vers le service de stockage (GCS)
        String fileUrl;
        try {
            logger.debug("Uploading file to storage service...");
            fileUrl = storageService.uploadFile(file, destinationPath);
            logger.info("File uploaded successfully. URL: {}", fileUrl);
        } catch (IOException e) {
            logger.error("File upload failed for destination path: {}", destinationPath, e);
            throw new IOException("Failed to upload audio file: " + e.getMessage(), e);
        }

        AudioUploadResponseDto responseDto = new AudioUploadResponseDto(
                fileUrl,
                metadata.getBpm(),
                metadata.getDurationSeconds()
        );

        logger.info("Returning upload response: {}", responseDto);
        return responseDto;
    }

    @Override
    public NewVersionModalDto prepareNewVersionModalData(Long currentVersionId) {
        logger.info("Preparing data for new version modal based on version ID: {}", currentVersionId);

        // 1. & 2. Trouver version actuelle et infos contexte (inchangé)
        Version currentVersion = versionRepository.findById(currentVersionId) // Utilise une requête qui fetch branche/compo si possible
                .orElseThrow(() -> new EntityNotFoundException("Version not found with id: " + currentVersionId));
        Branch currentBranch = currentVersion.getBranch();
        if (currentBranch == null || currentBranch.getComposition() == null) {
            throw new IllegalStateException("Data inconsistency: Version " + currentVersionId + " or its context is invalid.");
        }
        Long compositionId = currentBranch.getComposition().getId();
        logger.debug("Context: Version='{}', Branch='{}' (ID={}), Composition ID={}",
                currentVersion.getName(), currentBranch.getName(), currentBranch.getId(), compositionId);


        // 3. Lister branches disponibles (inchangé)
        List<Branch> allBranches = branchRepository.findByCompositionIdOrderByNameAsc(compositionId);
        List<BranchSummaryDto> availableBranchesDto = allBranches.stream()
                .map(branch -> new BranchSummaryDto(branch.getId(), branch.getName(), branch.getDescription()))
                .collect(Collectors.toList());


        // --- MODIFICATION ICI ---
        // 4. Calculer le nom potentiel de la prochaine version SUR la branche ACTUELLE
        //    Utilise calculatePotentialNameForBranch qui contient la logique d'incrément pour cette branche
        String potentialNextVersionName;
        try {
            // Utilisation de la méthode revue qui cherche la dernière version et incrémente
            potentialNextVersionName = versionNamingService.calculatePotentialNameForBranch(currentBranch.getId());
            logger.debug("Calculated potential next version name on current branch (ID: {}): {}", currentBranch.getId(), potentialNextVersionName);
        } catch (Exception e) {
            logger.error("Could not calculate potential next version name for branch {}: {}", currentBranch.getId(), e.getMessage(), e);
            potentialNextVersionName = "V?.?-error"; // Erreur de calcul
        }

        // 5. Récupérer instruments actuels (Logique d'extraction des enums inchangée)
        //    Si tu changes le DTO pour les instruments -> ajuster ici
        List<VersionInstrument> currentVersionInstrumentsEntities = versionInstrumentRepository.findByVersion(currentVersion);
        List<VersionInstrumentPreDefined> previousVersionInstrumentsDto = currentVersionInstrumentsEntities.stream()
                .map(VersionInstrument::getInstrument)
                .distinct()
                .collect(Collectors.toList());
        logger.debug("Found {} distinct instruments in current version {}", previousVersionInstrumentsDto.size(), currentVersionId);


        // 6. Récupérer les annotations de la version précédente (si implémenté)
        List<Annotation> currentAnnotations = annotationRepository.findByVersionId(currentVersionId); // ou findByVersion(currentVersion)
        List<AnnotationResponseDto> previousVersionAnnotationsDto = annotationMapper.toAnnotationResponseDtoList(currentAnnotations);
        logger.debug("Found {} annotations in current version {}", previousVersionAnnotationsDto.size(), currentVersionId);


        // 7. Construire DTO final
     NewVersionModalDto modalDto = new NewVersionModalDto(); // Crée via constructeur vide

        // Puis utiliser les setters
        modalDto.setCurrentVersionId(currentVersionId); // <- Long attendu
        modalDto.setCurrentBranchId(currentBranch.getId()); // <- Long attendu
        modalDto.setCurrentBranchName(currentBranch.getName()); // <- String attendu
        modalDto.setAvailableBranches(availableBranchesDto); // <- List<BranchSummaryDto> attendue
        modalDto.setPotentialNextVersionName(potentialNextVersionName); // <- String attendu
        modalDto.setPreviousVersionInstruments(previousVersionInstrumentsDto); // <- List<VersionInstrumentPreDefined> attendue
        modalDto.setPreviousVersionAnnotations(previousVersionAnnotationsDto);

        logger.info("Successfully prepared data for new version modal.");
        return modalDto;
    }

    @Override
    public VersionDetailDto getVersionDetailsById(Long versionId) {
        logger.info("Fetching details for version ID: {}", versionId);

        // 1. Trouver la version principale
        Version version = versionRepository.findById(versionId)
                .orElseThrow(() -> {
                    logger.warn("Version not found with ID: {}", versionId);
                    return new EntityNotFoundException("Version not found with id: " + versionId);
                });


        Branch branch = version.getBranch();
        String branchDesc = null;
        if (branch != null) {
            branchDesc = branch.getDescription();
        }


        // 2. Récupérer les instruments associés
        //    On utilise le repo pour être sûr de les charger (surtout si LAZY fetch)
        List<VersionInstrument> instrumentEntities = versionInstrumentRepository.findByVersion(version);
        List<VersionInstrumentDto> instrumentDtos = instrumentEntities.stream()
                .map(entity -> new VersionInstrumentDto(entity.getInstrument()))
                .collect(Collectors.toList());
        logger.debug("Found {} instruments for version {}", instrumentDtos.size(), versionId);

        // 3. Récupérer les annotations associées (Exemple - Adapte !)
        List<Annotation> annotationEntities = annotationRepository.findByVersion(version); // Supposant cette méthode existe
        List<AnnotationResponseDto> annotationDtos = annotationMapper.toAnnotationResponseDtoList(annotationEntities);
        logger.debug("Found {} annotations for version {}", annotationDtos.size(), versionId);

        // 4. Mapper vers le DTO final
        VersionDetailDto dto = new VersionDetailDto(
                version.getId(),
                version.getName(),
                version.getBranch() != null ? version.getBranch().getName() : null,
                branchDesc,
                version.getAudioFileUrl(),
                version.getBpm(),
                version.getKey(),
                version.getDurationSeconds(),
                instrumentDtos,
                annotationDtos
        );

        logger.info("Successfully fetched details for version {}", versionId);
        return dto;
    }

    @Override
    public List<VersionSummaryDto> getVersionsForComposition(Long compositionId, Optional<Long> branchIdOpt) {
        logger.info("Fetching versions V.M.m for composition ID: {}. Branch filter: {}",
                compositionId, branchIdOpt.map(String::valueOf).orElse("All"));

        List<Version> versions;

        // Logique de récupération filtrée ou non (inchangée)
        if (branchIdOpt.isPresent()) {
            Long branchId = branchIdOpt.get();
            Branch branch = branchRepository.findById(branchId)
                    .orElseThrow(() -> new EntityNotFoundException("Branch not found with id: " + branchId));
            if (!branch.getComposition().getId().equals(compositionId)) {
                throw new IllegalArgumentException("Branch " + branchId + " does not belong to Composition " + compositionId);
            }
            versions = versionRepository.findByBranchId(branchId);
        } else {
            if (!compositionRepository.existsById(compositionId)) { // Suppose existence de compositionRepository
                throw new EntityNotFoundException("Composition not found with id: " + compositionId);
            }
            versions = versionRepository.findByBranch_Composition_Id(compositionId);
        }

        // --- MODIFICATION : Tri Sémantique V[M].[m] ---
        // Trier la liste en mémoire en utilisant le comparateur V.M.m
        versions.sort(semanticVersionComparatorMM());

        // Ordre décroissant (plus récent en premier)
        Collections.reverse(versions);



        // Mapper en DTOs (inchangé conceptuellement, mais dépend du DTO final)
        List<VersionSummaryDto> dtos = versions.stream()
                .map(v -> new VersionSummaryDto( // Adapte aux champs réels de VersionSummaryDto
                        v.getId(),
                        v.getName(),
                        v.getBranch() != null ? v.getBranch().getName() : null,
                        v.getCreatedDate() // Assure que la date est chargée/dispo
                ))
                .collect(Collectors.toList());


        logger.info("Found and sorted {} versions for composition {}", dtos.size(), compositionId);
        return dtos;
    }

    private Comparator<Version> semanticVersionComparatorMM() {

        return Comparator.comparing(Version::getName, (name1, name2) -> {
            Matcher m1 = VERSION_M_M_PATTERN.matcher(name1);
            Matcher m2 = VERSION_M_M_PATTERN.matcher(name2);

            if (m1.matches() && m2.matches()) {
                int major1 = Integer.parseInt(m1.group(1));
                int minor1 = Integer.parseInt(m1.group(2));
                int major2 = Integer.parseInt(m2.group(1));
                int minor2 = Integer.parseInt(m2.group(2));

                // Comparer Major d'abord
                int majorCompare = Integer.compare(major1, major2);
                if (majorCompare != 0) {
                    return majorCompare;
                }
                // Si Major égal, comparer Minor
                return Integer.compare(minor1, minor2);
            } else {
                // Gérer les formats invalides ou faire un tri alphabétique par défaut ?
                logger.warn("Could not parse version names '{}' and/or '{}' semantically. Falling back to string comparison.", name1, name2);
                return name1.compareTo(name2); // Fallback
            }
        });
    }




    @Override
    @Transactional
    // --- Signature MODIFIÉE ---
    public VersionResponseDto createVersion(Long compositionId, VersionCreateDto dto) {
        logger.info("Attempting to create new version for composition ID {}. DTO parentId: {}",
                compositionId, dto.getParentVersionId());

        // 1. Valider le DTO (peut rester simple maintenant, le reste est logique métier)
        validateDtoBasicConstraints(dto); // Helper simple pour contraintes DTO basiques si @Valid ne suffit pas

        Version parentVersion = null;
        Branch targetBranch;
        String newVersionName;
        Long actualCompositionId = compositionId; // Utilise l'ID passé, sauf si écrasé par le parent

        // --- Logique Différentielle ---
        if (dto.getParentVersionId() == null) {
            // === CAS A : TOUTE PREMIÈRE VERSION ===
            logger.info("Handling creation of the very first version for composition {}.", actualCompositionId);

            // Validation spécifique : DTO cible bien la branche 'main'
            if (dto.getBranchId() != null || (dto.getNewBranchName() != null && !MAIN_BRANCH_NAME.equalsIgnoreCase(dto.getNewBranchName()))) {
                throw new IllegalArgumentException("The very first version must be created on the 'main' branch.");
            }

            // Validation : Vérifier que l'ID de composition passé existe
            if (!compositionRepository.existsById(actualCompositionId)) {
                throw new EntityNotFoundException("Composition not found: " + actualCompositionId);
            }
            Composition composition = compositionRepository.findById(compositionId)
                    .orElseThrow(() -> new IllegalArgumentException("No composition with the id : {}"));

            // Trouver ou créer la branche "main" pour cette composition
            targetBranch = branchService.findOrCreateBranch(
                    composition,
                    null, MAIN_BRANCH_NAME, null,dto.getNewBranchDescription()
            );

            // Générer le nom V1.0
            newVersionName = versionNamingService.generateFirstEverVersionName();
            logger.info("Generated first ever version name: {}", newVersionName);

            // Pas d'actions sur annotations N-1
            logger.debug("Skipping actions on previous version annotations.");


        } else {
            // === CAS B : Version suivante (parent ID fourni) ===
            logger.debug("Handling creation of a subsequent version with parent ID: {}", dto.getParentVersionId());

            // 2. Trouver la version parente
            parentVersion = versionRepository.findById(dto.getParentVersionId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent version not found: " + dto.getParentVersionId()));
            logger.debug("Found parent version: '{}' on branch '{}'", parentVersion.getName(), parentVersion.getBranch().getName());

            // --- Important: Utiliser l'ID de composition du parent trouvé ---
            actualCompositionId = parentVersion.getBranch().getComposition().getId();
            logger.debug("Using composition ID {} from parent version context.", actualCompositionId);


            // Validation "Branche depuis Main"
            if (dto.getNewBranchName() != null && !MAIN_BRANCH_NAME.equalsIgnoreCase(parentVersion.getBranch().getName())) {
                throw new IllegalArgumentException("New branches can only be created from the '" + MAIN_BRANCH_NAME + "' branch.");
            }

            // 3. Actions sur Annotations N-1
            processPreviousVersionAnnotationActions(dto.getAnnotationIdsToResolve(), dto.getAnnotationIdsToDelete(), parentVersion);

            Composition actualComposition = compositionRepository.findById(compositionId)
                    .orElseThrow(() -> new IllegalArgumentException("No composition with the id : {}"));

            // 4. Trouver ou créer la branche cible
            targetBranch = branchService.findOrCreateBranch(
                    actualComposition, // Utilise l'ID de compo du parent
                    dto.getBranchId(),
                    dto.getNewBranchName(),
                    dto.getParentBranchId(),
                    dto.getNewBranchDescription()
            );

            // 5. Générer le nom de la nouvelle version
            newVersionName = versionNamingService.generateNextVersionName(parentVersion, targetBranch);
            logger.info("Generated next version name: {}", newVersionName);
        }
        // --- Fin de la Logique Différentielle ---


        // --- Suite Commune ---

        // 6. Créer l'entité Version (N)
        Version newVersion = new Version();

        // --- Lier la branche cible ---
        newVersion.setBranch(targetBranch);

        // --- Définir le nom généré ---
        newVersion.setName(newVersionName);

        // --- Définir l'ID parent (Long, peut être null) ---
        if (parentVersion != null) {
            // parentVersion est l'objet Version chargé précédemment (si ce n'est pas la première version)
            newVersion.setParentVersionId(parentVersion.getId());
        } else {
            // Cas de la TOUTE première version de la composition, pas de parent.
            newVersion.setParentVersionId(null);
        }

        // --- Utiliser l'URL fournie directement par le DTO ---
        // (Je suppose que ton VersionCreateDto a bien un getter getAudioFileUrl() maintenant)
        newVersion.setAudioFileUrl(dto.getUniqueFileName());

        // --- Peupler les autres métadonnées depuis le DTO ---
        newVersion.setBpm(dto.getBpm());
        newVersion.setKey(dto.getKey()); // Le nom de la colonne SQL est géré par @Column dans l'entité
        newVersion.setDurationSeconds(dto.getDurationSeconds());
        // setAuthor...

        // 7. Associer Instruments
       processAndAssociateInstruments(newVersion, dto.getVersionInstrumentPreDefinedList());

        // 8. Associer Annotations
        List<Annotation> newAnnotations = processAndAssociateNewAnnotations(newVersion, dto.getAnnotationsToCreate());
        newVersion.setAnnotations(newAnnotations);

        // 9. Sauvegarder Version N
        Version savedVersion = versionRepository.save(newVersion);
        logger.info("Successfully saved new version ID: {}, Name: {} for composition {}",
                savedVersion.getId(), savedVersion.getName(), actualCompositionId);


        // 10. Mapper vers DTO de réponse
        return mapVersionToResponseDto(savedVersion);
    }

    private void validateDtoBasicConstraints(VersionCreateDto dto) {

        logger.trace("Performing basic DTO constraint validation...");
        if (dto.getUniqueFileName() == null || dto.getUniqueFileName().isBlank()){
            throw new IllegalArgumentException("Unique file name is mandatory.");
        }
    }

    @Override
    @Transactional // S'assurer que la transaction englobe tout (GCS + DB)
    public void deleteVersion(Long versionId) {
        logger.info("Attempting to delete Version with ID: {}", versionId);

        // 1. Trouver la version à supprimer
        Version versionToDelete = versionRepository.findById(versionId)
                .orElseThrow(() -> {
                    logger.error("Version not found with ID: {}", versionId);
                    return new EntityNotFoundException("Version not found with id: " + versionId);
                });

        logger.debug("Found Version to delete: Name='{}', BranchId={}, AudioUrl='{}'",
                versionToDelete.getName(), versionToDelete.getBranch().getId(), versionToDelete.getAudioFileUrl());

        // 2. Essayer de supprimer le fichier audio associé sur GCS
        deleteAssociatedGcsFile(versionToDelete);

        // 3. Supprimer la version de la base de données
        try {
            versionRepository.delete(versionToDelete);
            logger.info("Successfully deleted Version with ID: {} from the database.", versionId);
        } catch (Exception e) {
            // Normalement, si GCS a échoué plus tôt et a lancé une exception (ce qui n'est pas le cas
            // par défaut dans l'implémentation actuelle), la transaction aurait déjà été marquée pour rollback.
            logger.error("Failed to delete Version ID: {} from database: {}", versionId, e.getMessage(), e);
            // L'exception BDD va provoquer un rollback si la transaction n'est pas déjà en rollback
            throw e; // Relancer pour que le contrôleur sache que ça a échoué
        }
    }



    /**
     * Attempts to delete the GCS file associated with the given version.
     * Logs warnings or errors but does not throw by default if GCS deletion fails.
     *
     * @param versionToDelete The version whose associated file should be deleted.
     */
    private void deleteAssociatedGcsFile(Version versionToDelete) {
        String audioFileUrl = versionToDelete.getAudioFileUrl();
        Long versionId = versionToDelete.getId(); // For logging

        if (audioFileUrl == null || audioFileUrl.isBlank()) {
            logger.warn("No audioFileUrl found for Version ID {}. Skipping GCS deletion.", versionId);
            return; // Nothing to delete
        }

        try {
            String objectPath = storageService.extractObjectPathFromUrl(audioFileUrl);
            if (objectPath != null) {
                logger.debug("Attempting deletion of GCS object: {} for Version ID {}", objectPath, versionId);
                boolean gcsDeleted = storageService.deleteFile(objectPath);
                if (gcsDeleted) {
                    logger.info("Successfully deleted GCS object '{}' associated with Version ID: {}", objectPath, versionId);
                } else {
                    // deleteFile logs the specific error. Log a warning about potential orphan.
                    logger.warn("Failed to delete GCS object '{}' for Version ID {}. Proceeding with database deletion. File might remain orphaned in GCS.", objectPath, versionId);
                    // !! IMPORTANT: Décision de design !!
                    // Si l'échec de GCS DOIT empêcher la suppression en BDD, il faudrait lancer une exception ici.
                    // Par exemple: throw new IOException("Failed to delete required GCS file: " + objectPath);
                    // Cela provoquerait un rollback de la transaction @Transactional.
                }
            } else {
                logger.warn("Could not extract object path from URL '{}' for Version ID {}. Skipping GCS deletion.", audioFileUrl, versionId);
            }
        } catch (URISyntaxException e) {
            logger.error("Invalid URL format for GCS file '{}' in Version ID {}. Cannot parse URL to delete from GCS. Error: {}", audioFileUrl, versionId, e.getMessage());
            // Selon la politique, on pourrait lancer une exception ici aussi pour bloquer.
            // throw new IllegalArgumentException("Invalid GCS URL format, cannot proceed with deletion: " + audioFileUrl, e);
        } catch (Exception e) {
            // Attrape toute autre erreur potentielle de GCS (permissions, etc.) venant de deleteFile
            logger.error("An unexpected error occurred during GCS file deletion for Version ID {}: {}", versionId, e.getMessage(), e);
            // Lancer une exception ici si l'échec GCS doit être bloquant.
            // throw new RuntimeException("GCS file deletion failed for Version " + versionId, e);
        }
    }

    private void validateVersionCreateDto(VersionCreateDto dto) {

        logger.debug("Validating VersionCreateDto...");
        if (dto.getUniqueFileName() == null || dto.getUniqueFileName().isBlank()) {
            logger.error("Validation failed: Audio file URL is missing.");
            throw new IllegalArgumentException("Audio file URL is required to create a version.");
        }
        if (dto.getBranchId() == null && (dto.getNewBranchName() == null || dto.getNewBranchName().isBlank())) {
            logger.error("Validation failed: Either branchId or newBranchName must be provided.");
            throw new IllegalArgumentException("Either branchId or newBranchName must be provided.");
        }
        if (dto.getBranchId() != null && (dto.getNewBranchName() != null && !dto.getNewBranchName().isBlank())) {
            logger.error("Validation failed: Cannot provide both branchId and newBranchName.");
            throw new IllegalArgumentException("Cannot provide both branchId and newBranchName.");
        }
        // Validation modifiée : ParentVersionId est nécessaire SEULEMENT si c'est une NOUVELLE branche ET qu'elle n'est pas la racine absolue (implicitement V1.0)
        // La logique de nommage gère le cas où parentVersionId est null pour V1.0.
        // Mais si on spécifie une nouvelle branche *et* qu'on s'attend à un nom autre que V1.0, le parent est nécessaire.
        if (dto.getNewBranchName() != null && !dto.getNewBranchName().isBlank() && dto.getParentVersionId() == null) {
            // On pourrait affiner cette règle. Si la branche est vraiment la PREMIÈRE de la compo, V1.0 est ok sans parentId.
            // Pour l'instant, on garde : nouvelle branche -> parentId requis pour Vmajor.1
            logger.warn("Validation warning (may be acceptable for first-ever branch): parentVersionId is null while creating a new branch (newBranchName is set). Version name might default to V1.0 if no other versions exist.");
            // Retiré l'exception pour plus de flexibilité, la logique de nommage gère ça.
            // throw new IllegalArgumentException("Parent Version ID is required when creating a new branch based on an existing version line.");
        }
        logger.debug("VersionCreateDto validation passed.");
    }

    private Comparator<Version> versionNameComparator() {
        return Comparator.comparing(Version::getName, (name1, name2) -> {
            Matcher matcher1 = VERSION_NAME_PATTERN.matcher(name1 != null ? name1 : "");
            Matcher matcher2 = VERSION_NAME_PATTERN.matcher(name2 != null ? name2 : "");

            if (matcher1.matches() && matcher2.matches()) {
                int major1 = Integer.parseInt(matcher1.group(1));
                int minor1 = Integer.parseInt(matcher1.group(2));
                int major2 = Integer.parseInt(matcher2.group(1));
                int minor2 = Integer.parseInt(matcher2.group(2));

                if (major1 != major2) {
                    return Integer.compare(major1, major2); // Compare les majeurs
                } else {
                    return Integer.compare(minor1, minor2); // Si majeurs égaux, compare les mineurs
                }
            } else {
                // Gérer les noms non valides (les mettre à la fin/début ou comparaison simple)
                return String.CASE_INSENSITIVE_ORDER.compare(name1, name2);
            }
        });
    }
    private Version initializeNewVersionEntity(Branch branch, String versionName) {
        // ... (Logique inchangée) ...
        logger.debug("Initializing new Version entity for branch {} with name {}", branch.getId(), versionName);
        Version newVersion = new Version();
        newVersion.setBranch(branch);
        newVersion.setName(versionName);
        // Assurez-vous que les collections sont initialisées si @OneToMany(cascade=...) est utilisé
        newVersion.setInstruments(new HashSet<>()); // Utiliser Set si la relation est un Set
        newVersion.setAnnotations(new ArrayList<>()); // Utiliser List si la relation est une List
        newVersion.setMetadata(new HashMap<>()); // Initialiser metadata
        newVersion.setCreatedDate(LocalDateTime.now()); // Explicitement pour trace, même si JPA Auditing peut le faire
        return newVersion;
    }

    private void populateVersionDetailsFromDto(Version version, VersionCreateDto dto) {

        logger.debug("Populating details for version '{}' from DTO...", version.getName());

        version.setAudioFileUrl(dto.getUniqueFileName());
        version.setDurationSeconds(dto.getDurationSeconds());
        version.setKey(dto.getKey());
        // Attention à la source BPM (AudioUploadResponse ou VersionCreateDto ?)
        // Ici on utilise VersionCreateDto, assure-toi que c'est la source voulue.
        if (dto.getBpm() != null && !dto.getBpm().isBlank()) {
            try {
                version.setBpm(dto.getBpm().trim()); // Supposant que BPM est String dans l'entité aussi
            } catch (NumberFormatException e) {
                logger.warn("Could not parse BPM value '{}' from DTO for version {}. Setting BPM to null.", dto.getBpm(), version.getName());
                version.setBpm(null);
            }
        } else {
            version.setBpm(null);
        }
        version.setMetadata(dto.getMetadata() != null ? new HashMap<>(dto.getMetadata()) : new HashMap<>()); // Copie défensive + init
        logger.debug("Version details populated.");
    }

    private void processAndAssociateInstruments(Version newVersion, Set<VersionInstrumentPreDefined> instrumentsFromDto) {

        logger.debug("Populating instruments for new version '{}' based solely on DTO input.", newVersion.getName());


        if (newVersion.getInstruments() == null) {
            newVersion.setInstruments(new HashSet<>());
        }
        // Vider la collection garantit que SEULS les instruments du DTO seront présents
        newVersion.getInstruments().clear();

        if (instrumentsFromDto != null && !instrumentsFromDto.isEmpty()) {
            logger.info("Assigning {} instruments to version '{}' from DTO.", instrumentsFromDto.size(), newVersion.getName());

            for (VersionInstrumentPreDefined instrumentEnum : instrumentsFromDto) {

                VersionInstrument versionInstrument = new VersionInstrument();
                versionInstrument.setVersion(newVersion);
                versionInstrument.setInstrument(instrumentEnum);
                newVersion.getInstruments().add(versionInstrument);

                // Alternativement, si tu gardes la méthode helper dans Version.java:
                // newVersion.addInstrument(instrumentEnum);
                // (Assure-toi que la méthode addInstrument fait exactement la création et l'ajout ci-dessus)
            }
        } else {
            logger.info("No instruments provided in DTO for version '{}'. Assigning empty instrument set.", newVersion.getName());
        }

        logger.debug("Instrument processing complete for version '{}'. Final instrument count: {}",
                newVersion.getName(), newVersion.getInstruments().size());
    }
    private VersionResponseDto mapVersionToResponseDto(Version version) {
        if (version == null) {
            return null;
        }

        VersionResponseDto dto = new VersionResponseDto();

        // Mapping des champs simples
        dto.setId(version.getId());
        dto.setName(version.getName());
        dto.setAuthor(version.getAuthor());
        dto.setBpm(version.getBpm());
        dto.setKey(version.getKey()); // Assure-toi que le setter existe pour 'key'
        dto.setDurationSeconds(version.getDurationSeconds());
        dto.setAudioFileUrl(version.getAudioFileUrl());
        // Attention avec les Map et @ElementCollection si LAZY - Initialisation peut être nécessaire
        // Si la relation metadata est LAZY:
        // Hibernate.initialize(version.getMetadata());
        dto.setMetadata(version.getMetadata() != null ? new HashMap<>(version.getMetadata()) : null); // Copie défensive
        dto.setCreatedDate(version.getCreatedDate());

        // Mapping de la Branche (relation ManyToOne)
        Branch branch = version.getBranch();
        if (branch != null) {
            // Si LAZY, décommenter la ligne suivante :
            // Hibernate.initialize(branch);
            dto.setBranchId(branch.getId());
            dto.setBranchName(branch.getName());
            dto.setBranchDescription(branch.getDescription());
        }

        // Mapping des Instruments (relation OneToMany LAZY)
        Set<VersionInstrument> instruments = version.getInstruments();
        Set<String> instrumentNames = Collections.emptySet(); // Valeur par défaut
        if (instruments != null) {
            // Force l'initialisation si la collection est LAZY (TRÈS IMPORTANT)
            Hibernate.initialize(instruments);
            instrumentNames = instruments.stream()
                    .map(versionInstrument -> versionInstrument.getInstrument() != null ? versionInstrument.getInstrument().name() : null)
                    .filter(Objects::nonNull) // Ignore les cas où l'enum serait null (sécurité)
                    .collect(Collectors.toSet());
        }
        dto.setInstruments(instrumentNames);

        // TODO: Ajouter le mapping des annotations ici, en initialisant la collection si LAZY
        // List<Annotation> annotations = version.getAnnotations();
        // if (annotations != null) {
        //     Hibernate.initialize(annotations);
        //     // ... map vers List<AnnotationViewDto> ...
        // }
        // dto.setAnnotations(...);


        return dto;
    }


    private void processPreviousVersionAnnotationActions(List<Long> idsToResolve, List<Long> idsToDelete, Version parentVersion) {
        Instant now = Instant.now(); // Pour le soft delete

        // Résoudre
        if (idsToResolve != null && !idsToResolve.isEmpty()) {
            List<Annotation> annotationsToResolve = annotationRepository.findAllById(idsToResolve);
            annotationsToResolve.forEach(ann -> {
                if (!ann.getVersion().getId().equals(parentVersion.getId())) {
                    logger.warn("Attempted to resolve annotation {} which does not belong to parent version {}", ann.getId(), parentVersion.getId());
                    throw new IllegalArgumentException("Annotation " + ann.getId() + " does not belong to parent version " + parentVersion.getId());
                }
                ann.setAnnotationStatus(AnnotationStatus.RESOLUE);
                logger.debug("Marking annotation {} as RESOLVED", ann.getId());
                // save implicite par @Transactional
            });
            // annotationRepository.saveAll(annotationsToResolve); // Ou save explicite si pas sûr de @Transactional
        }

        // Supprimer (Soft Delete - nécessite @SQLDelete/@Where sur l'entité Annotation)
        if (idsToDelete != null && !idsToDelete.isEmpty()) {
            List<Annotation> annotationsToDelete = annotationRepository.findAllById(idsToDelete);
            annotationsToDelete.forEach(ann -> {
                if (!ann.getVersion().getId().equals(parentVersion.getId())) {
                    logger.warn("Attempted to delete annotation {} which does not belong to parent version {}", ann.getId(), parentVersion.getId());
                    throw new IllegalArgumentException("Annotation " + ann.getId() + " does not belong to parent version " + parentVersion.getId());
                }
                logger.debug("Marking annotation {} for deletion", ann.getId());
                // Si @SQLDelete("UPDATE annotations SET supression_date = NOW() WHERE id = ?") est sur l'entité:
                annotationRepository.delete(ann); // Hibernate intercepte et fait l'UPDATE
                // Sinon, manuellement:
                // ann.setSupressionDate(LocalDateTime.now());
                // annotationRepository.save(ann);
            });
            // annotationRepository.deleteAll(annotationsToDelete); // Appel explicite si @SQLDelete est utilisé
        }
    }


    // --- La méthode demandée ---

    /**
     * Traite la liste des DTOs d'annotations à créer pour la nouvelle version,
     * les mappe en entités Annotation liées à cette nouvelle version,
     * mais ne les persiste pas directement (la cascade depuis la sauvegarde
     * de la Version parente s'en chargera).
     *
     * @param newVersion L'entité Version (nouvelle, non encore persistée ou en cours de transaction) à laquelle associer les annotations.
     * @param annotationDtos La liste de DTOs (NewVersionAnnotationDto) décrivant les annotations à créer.
     * @return Une liste d'entités Annotation (non persistées mais liées à newVersion), prêtes à être associées. Retourne une liste vide si annotationDtos est null ou vide.
     */
    private List<Annotation> processAndAssociateNewAnnotations(Version newVersion, List<NewVersionAnnotationDto> annotationDtos) {
        // 1. Vérification initiale
        if (annotationDtos == null || annotationDtos.isEmpty()) {
            logger.debug("No annotations provided to create for new version ID TEMP (Name: {})", newVersion.getName());
            // Retourner une liste vide mutable pour être sûr
            return new ArrayList<>();
        }

        logger.debug("Processing {} DTOs to create annotations for new version '{}'", annotationDtos.size(), newVersion.getName());

        // 2. Appel du Mapper pour la conversion
        // La méthode 'fromNewVersionAnnotationDtoList' dans le mapper doit prendre la liste de DTOs
        // et l'objet 'newVersion' pour faire la liaison dans chaque nouvelle entité Annotation.
        try {
            List<Annotation> newAnnotationEntities = annotationMapper.fromNewVersionAnnotationDtoList(annotationDtos, newVersion);
            logger.debug("Mapped {} DTOs to {} new Annotation entities.", annotationDtos.size(), newAnnotationEntities.size());
            return newAnnotationEntities;
        } catch (Exception e) {
            // Gérer une éventuelle exception du mapper (ex: IllegalArgumentException si DTO invalide non catché avant)
            logger.error("Error mapping NewVersionAnnotationDtos to Annotation entities for version '{}': {}", newVersion.getName(), e.getMessage(), e);
            // Que faire ici ? Propager l'exception ou retourner liste vide/lever une exception métier ?
            // Propager est souvent préférable pour indiquer un problème dans le processus de création.
            throw new RuntimeException("Failed to process annotations for the new version due to a mapping error.", e);
        }
    }

}