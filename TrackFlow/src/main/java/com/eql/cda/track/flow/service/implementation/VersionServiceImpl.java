package com.eql.cda.track.flow.service.implementation;


import com.eql.cda.track.flow.dto.AudioMetadataDto;
import com.eql.cda.track.flow.dto.annotationDto.AnnotationViewDto;
import com.eql.cda.track.flow.dto.branchDto.BranchSummaryDto;
import com.eql.cda.track.flow.dto.versionDto.AudioUploadResponseDto;
import com.eql.cda.track.flow.dto.versionDto.NewVersionModalDto;
import com.eql.cda.track.flow.dto.versionDto.VersionCreateDto;
import com.eql.cda.track.flow.dto.versionDto.VersionDetailDto;
import com.eql.cda.track.flow.dto.versionDto.VersionInstrumentDto;
import com.eql.cda.track.flow.dto.versionDto.VersionResponseDto;
import com.eql.cda.track.flow.dto.versionDto.VersionSummaryDto;
import com.eql.cda.track.flow.entity.Annotation;
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
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
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


    public VersionServiceImpl(VersionRepository versionRepository, CompositionRepository compositionRepository, StorageService storageService, AudioMetadataService audioMetadataService, VersionInstrumentRepository versionInstrumentRepository, BranchService branchService, BranchRepository branchRepository, VersionNamingService versionNamingService, AnnotationRepository annotationRepository) {
        this.versionRepository = versionRepository;
        this.compositionRepository = compositionRepository;
        this.storageService = storageService;
        this.audioMetadataService = audioMetadataService;
        this.versionInstrumentRepository = versionInstrumentRepository;
        this.branchService = branchService;
        this.branchRepository = branchRepository;
        this.versionNamingService = versionNamingService;
        this.annotationRepository = annotationRepository;
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

        // 1. Trouver la version actuelle
        Version currentVersion = versionRepository.findById(currentVersionId)
                .orElseThrow(() -> {
                    logger.error("Current version not found with ID: {}", currentVersionId);
                    return new EntityNotFoundException("Version not found with id: " + currentVersionId);
                });
        logger.debug("Found current version: Name='{}', BranchId={}", currentVersion.getName(), currentVersion.getBranch().getId());

        // 2. Extraire les infos de base (Branche, Composition)
        Branch currentBranch = currentVersion.getBranch();
        if (currentBranch == null) {
            // Cas très improbable si les données sont cohérentes, mais sécurité
            logger.error("Version {} has no associated branch!", currentVersionId);
            throw new IllegalStateException("Data inconsistency: Version " + currentVersionId + " has no branch.");
        }
        Composition composition = currentBranch.getComposition();
        if (composition == null) {
            logger.error("Branch {} has no associated composition!", currentBranch.getId());
            throw new IllegalStateException("Data inconsistency: Branch " + currentBranch.getId() + " has no composition.");
        }
        Long compositionId = composition.getId();
        logger.debug("Context: Current Branch ID={}, Name='{}', Composition ID={}", currentBranch.getId(), currentBranch.getName(), compositionId);


        // 3. Lister toutes les branches de la composition
        List<Branch> allBranches = branchRepository.findByCompositionIdOrderByNameAsc(compositionId); // Ou autre tri si désiré
        // Mapper en DTOs simples pour le frontend
        List<BranchSummaryDto> availableBranchesDto = allBranches.stream()
                .map(branch -> new BranchSummaryDto(branch.getId(), branch.getName()))
                .collect(Collectors.toList());
        logger.debug("Found {} available branches for composition {}.", availableBranchesDto.size(), compositionId);


        // 4. Calculer le nom potentiel de la prochaine version SUR la branche ACTUELLE
        //    On utilise le service de nommage, en lui disant quelle est la dernière version connue (la currentVersion)
        //    et sur quelle branche on se base (la currentBranch). On ne spécifie pas de parentVersionId car on est sur la même branche.
        String potentialNextVersionName;
        try {
            potentialNextVersionName = versionNamingService.generateNextVersionName(
                    currentBranch,
                    null, // Pas de parentVersionId ici, on incrémente sur la branche actuelle
                    Optional.of(currentVersion) // La version actuelle est la base pour l'incrémentation Vmajor.minor+1
            );
            logger.debug("Calculated potential next version name on current branch: {}", potentialNextVersionName);
        } catch (Exception e) {
            logger.error("Could not calculate potential next version name based on version {}: {}", currentVersionId, e.getMessage());
            potentialNextVersionName = "N/A"; // Ou une autre valeur indiquant une erreur
        }


        // 5. Récupérer les instruments de la version ACTUELLE
        //    Il faut utiliser le VersionInstrumentRepository pour charger les entités liées
        List<VersionInstrument> currentVersionInstrumentsEntities = versionInstrumentRepository.findByVersion(currentVersion);
        // Extraire juste les enums pour les passer au DTO
        List<VersionInstrumentPreDefined> previousVersionInstrumentsDto = currentVersionInstrumentsEntities.stream()
                .map(VersionInstrument::getInstrument) // Extrait l'enum de chaque entité VersionInstrument
                .distinct() // S'assurer qu'il n'y a pas de doublons (normalement non, mais sécurité)
                .collect(Collectors.toList());
        logger.debug("Found {} instruments in current version {}: {}", previousVersionInstrumentsDto.size(), currentVersionId, previousVersionInstrumentsDto);


        // 6. Récupérer les annotations (PLACEHOLDER - À implémenter plus tard)
        // List<AnnotationSummaryDto> previousVersionAnnotationsDto = Collections.emptyList(); // Placeholder
        // logger.debug("Annotation fetching is pending implementation.");


        // 7. Construire et retourner le DTO final
        NewVersionModalDto modalDto = new NewVersionModalDto(
                currentVersionId,
                currentBranch.getId(),
                currentBranch.getName(),
                availableBranchesDto,
                potentialNextVersionName,
                previousVersionInstrumentsDto
                //, previousVersionAnnotationsDto // Ajouter quand prêt
        );

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

        // 2. Récupérer les instruments associés
        //    On utilise le repo pour être sûr de les charger (surtout si LAZY fetch)
        List<VersionInstrument> instrumentEntities = versionInstrumentRepository.findByVersion(version);
        List<VersionInstrumentDto> instrumentDtos = instrumentEntities.stream()
                .map(entity -> new VersionInstrumentDto(entity.getInstrument()))
                .collect(Collectors.toList());
        logger.debug("Found {} instruments for version {}", instrumentDtos.size(), versionId);

        // 3. Récupérer les annotations associées (Exemple - Adapte !)
        //    Besoin d'un AnnotationRepository avec une méthode findByVersion
        List<Annotation> annotationEntities = annotationRepository.findByVersion(version); // Supposant cette méthode existe
        List<AnnotationViewDto> annotationDtos = annotationEntities.stream()
                .map(entity -> new AnnotationViewDto(entity.getId(), entity.getContent(), entity.getTimePosition(), entity.getAnnotationCategory().name())) // Adapte aux champs réels
                .collect(Collectors.toList());
        logger.debug("Found {} annotations for version {}", annotationDtos.size(), versionId);

        // 4. Mapper vers le DTO final
        VersionDetailDto dto = new VersionDetailDto(
                version.getId(),
                version.getName(),
                version.getBranch() != null ? version.getBranch().getName() : null,
                version.getDescription(),
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
        logger.info("Fetching versions for composition ID: {}. Branch filter: {}",
                compositionId, branchIdOpt.map(String::valueOf).orElse("All"));

        List<Version> versions;

        if (branchIdOpt.isPresent()) {
            // -- Cas 1: Filtrer par branche spécifique --
            Long branchId = branchIdOpt.get();
            // Vérifier que la branche existe et appartient bien à la composition (sécurité/validation)
            Branch branch = branchRepository.findById(branchId)
                    .orElseThrow(() -> new EntityNotFoundException("Branch not found with id: " + branchId));
            if (!branch.getComposition().getId().equals(compositionId)) {
                throw new IllegalArgumentException("Branch " + branchId + " does not belong to Composition " + compositionId);
            }
            logger.debug("Filtering by branch ID: {}", branchId);
            versions = versionRepository.findByBranchId(branchId);

        } else {
            // -- Cas 2: Toutes les branches de la composition --
            // Vérifier que la composition existe (optionnel, peut échouer silencieusement)
            if (!compositionRepository.existsById(compositionId)) {
                throw new EntityNotFoundException("Composition not found with id: " + compositionId);
            }
            logger.debug("Fetching versions for all branches of composition ID: {}", compositionId);
            versions = versionRepository.findByBranch_Composition_Id(compositionId);
        }

        // -- Tri en Java (important pour V1.10 vs V1.2) --
        versions.sort(Collections.reverseOrder(versionNameComparator()));; // reversed() pour descendant (plus récent en premier)

        // -- Mapper en DTOs --
        List<VersionSummaryDto> dtos = versions.stream()
                .map(v -> new VersionSummaryDto(
                        v.getId(),
                        v.getName(),
                        v.getAuthor(),
                        v.getBranch() != null ? v.getBranch().getName() : null,
                        v.getCreatedDate()
                ))
                .collect(Collectors.toList());

        logger.info("Found {} versions matching criteria for composition {}", dtos.size(), compositionId);
        return dtos;
    }


    private String calculatePotentialNameForBranch(Long branchId) {
        Branch targetBranch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with id: " + branchId));
        // 2. Trouver dernière version sur cette branche
        Optional<Version> latestVersionOnTargetBranch = versionRepository.findTopByBranchOrderByIdDesc(targetBranch);
        // 3. Appeler la logique de nommage existante (ou une helper interne)
        return this.versionNamingService.generateNextVersionName(targetBranch, null, latestVersionOnTargetBranch);
    }



    @Override
    @Transactional
    public VersionResponseDto createVersion(Long compositionId, VersionCreateDto dto) {
        logger.info("Starting version creation process for composition ID: {}", compositionId);
        logger.debug("Received VersionCreateDto: {}", dto);

        // 1. Validation DTO (méthode privée)
        validateVersionCreateDto(dto);

        // 2. Récupérer la Composition (méthode privée)
        Composition composition = compositionRepository.findById(compositionId)
                .orElseThrow(() -> new EntityNotFoundException("Composition not found with ID: " + compositionId));

        // 3. Trouver ou Créer la Branche (délégué à BranchService)
        Branch branch = branchService.findOrCreateBranch(
                composition,
                dto.getBranchId(),
                dto.getNewBranchName(),
                dto.getParentBranchId() // Note: La logique de validation parentBranch vs composition est dans BranchServiceImpl
        );

        // 4. Récupérer la dernière version sur la branche (si elle existe)
        Optional<Version> latestVersionOpt = versionRepository.findTopByBranchOrderByIdDesc(branch);

        // 5. Générer le nom de la nouvelle version (délégué à VersionNamingService)
        String nextVersionName = versionNamingService.generateNextVersionName(
                branch,
                dto.getParentVersionId(), // Requis pour générer Vmajor.1 si nouvelle branche
                latestVersionOpt
        );

        // 6. Initialiser la nouvelle entité Version (méthode privée)
        Version newVersion = initializeNewVersionEntity(branch, nextVersionName);

        // 7. Peupler les détails depuis le DTO (méthode privée)
        populateVersionDetailsFromDto(newVersion, dto); // Pass compositionId if needed for logging in helper

        // 8. Traiter et associer les instruments (méthode privée)
        processAndAssociateInstruments(newVersion, dto.getVersionInstrumentPreDefinedList());


        // 10. Sauvegarder la nouvelle version (cascade pour les instruments)
        Version savedVersion = versionRepository.save(newVersion);
        logger.info("Successfully created and saved Version with ID: {} and Name: {}", savedVersion.getId(), savedVersion.getName());
        logger.debug("Final saved version details (check associations): {}", savedVersion); // Adjust logging if needed
        VersionResponseDto responseDto = mapVersionToResponseDto(savedVersion); // <--- Appel de la méthode de mapping
        logger.debug("Mapped saved Version ID {} to VersionResponseDto for response.", responseDto.getId());
        return responseDto;
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
        // ... (Logique de validation inchangée) ...
        logger.debug("Validating VersionCreateDto...");
        if (dto.getAudioFileUrl() == null || dto.getAudioFileUrl().isBlank()) {
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
        // ... (Logique inchangée, retiré compositionId si non utilisé dans les logs ici) ...
        logger.debug("Populating details for version '{}' from DTO...", version.getName());
        version.setDescription(dto.getDescription());
        version.setAudioFileUrl(dto.getAudioFileUrl());
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

    private void processAndAssociateInstruments(Version newVersion, List<VersionInstrumentPreDefined> instrumentsFromDto) {

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
        dto.setDescription(version.getDescription());
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
}