package com.eql.cda.track.flow.service.implementation;

import com.eql.cda.track.flow.entity.Branch;
import com.eql.cda.track.flow.entity.Version;
import com.eql.cda.track.flow.repository.BranchRepository;
import com.eql.cda.track.flow.repository.VersionRepository;
import com.eql.cda.track.flow.service.VersionNamingService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;

import static com.eql.cda.track.flow.validation.Constants.VERSION_NAME_PATTERN;


@Service
public class VersionNamingServiceImpl implements VersionNamingService {
    private static final Logger logger = LogManager.getLogger(VersionNamingServiceImpl.class);

    private static final String MAIN_BRANCH_NAME = "main";

    private final VersionRepository versionRepository;
    private final BranchRepository branchRepository;

    public VersionNamingServiceImpl(VersionRepository versionRepository, BranchRepository branchRepository) {
        this.versionRepository = versionRepository;
        this.branchRepository = branchRepository;
    }

    @Override
    public String generateNextVersionName(Branch branch, Long parentVersionId, Optional<Version> latestVersionOpt) {
        String branchName = branch.getName(); // Pour la lisibilité
        logger.debug("Generating next version name for branch '{}' (ID: {}). ParentVersionId provided: {}. Latest version present: {}",
                branchName, branch.getId(), parentVersionId, latestVersionOpt.isPresent());

        if (latestVersionOpt.isPresent()) {
            // --- CAS 1: Branche EXISTANTE avec au moins une version ---
            Version latestVersion = latestVersionOpt.get();
            logger.debug("Found latest version on branch '{}': '{}' (ID: {})", branchName, latestVersion.getName(), latestVersion.getId());
            return incrementExistingVersion(latestVersion.getName(), branchName);

        } else {
            // --- CAS 2: PREMIÈRE version sur cette branche ---
            logger.debug("No existing version found on branch '{}'. Determining first version name.", branchName);
            if (parentVersionId != null) {
                // Sous-cas 2a: Nouvelle branche basée sur une version parente explicite
                logger.debug("Generating first version name for new branch '{}' based on parent version ID: {}", branchName, parentVersionId);
                return generateNameForNewBranchFromParent(parentVersionId, branchName);
            } else {
                // Sous-cas 2b: Première version absolue (ou branche "main" sans parent spécifié)
                if (MAIN_BRANCH_NAME.equalsIgnoreCase(branchName)) {
                    logger.info("First version on '{}' branch (or no parent specified). Starting with V1.0", branchName);
                    return "V1.0"; // Standard pour la branche principale initiale
                } else {
                    // Cas potentiellement problématique : première version sur une branche non-main SANS parent ?
                    // Devrait être normalement évité par la validation DTO qui requiert parentVersionId si newBranchName est fourni.
                    logger.warn("Attempting to create first version on non-main branch '{}' without explicit parentVersionId. This might indicate a logic issue. Defaulting to V1.0, review context.", branchName);
                    return "V1.0"; // Ou lancer une exception si ce cas est interdit
                    // throw new IllegalStateException("Cannot create first version on non-main branch '" + branchName + "' without specifying a parentVersionId.");
                }
            }
        }
    }

    private String generateNameForNewBranchFromParent(Long parentVersionId, String newBranchName) {

        Version parentVersion = versionRepository.findById(parentVersionId)
                .orElseThrow(() -> new EntityNotFoundException("Parent version specified (ID: " + parentVersionId + ") not found for new branch '" + newBranchName + "'."));

        String parentName = parentVersion.getName();
        logger.debug("Generating first name for new branch '{}' based on parent '{}' (ID: {})", newBranchName, parentName, parentVersionId);

        Matcher matcher = VERSION_NAME_PATTERN.matcher(parentName);
        if (!matcher.matches()) {
            logger.error("Cannot generate name for new branch: Could not parse parent version name '{}' (ID: {}).", parentName, parentVersionId);
            throw new IllegalStateException("Invalid parent version name format: " + parentName);
        }

        int major = Integer.parseInt(matcher.group(1));
        int minor = matcher.group(2) != null ? Integer.parseInt(matcher.group(2)) : 0;
        // Le patch du parent n'influence pas directement le nom de la nouvelle branche dans ce modèle

        String nextName;
        if (matcher.group(2) == null || minor == 0) {
            // Cas 1: Parent est V[M] (groupe 2 est null) OU V[M].0 (minor est 0)
            // -> La nouvelle branche démarre au niveau Minor (.1)
            nextName = String.format("V%d.1", major);
            logger.debug("Parent version '{}' implies branching at minor level.", parentName);
        } else {
            // Si parent est V[M].[m] ou V[M].[m].[p] -> Nouvelle branche V[M].[m].1
            nextName = String.format("V%d.%d.1", major, minor);
        }

        logger.info("First version name for new branch '{}' determined from parent '{}': {}", newBranchName, parentName, nextName);
        return nextName;
    }

    private String incrementExistingVersion(String currentVersionName, String branchName) {

        Matcher matcher = VERSION_NAME_PATTERN.matcher(currentVersionName);
        if (!matcher.matches()) {
            logger.error("Cannot increment version: Could not parse existing version name '{}' on branch '{}'.", currentVersionName, branchName);
            throw new IllegalStateException("Invalid version name format found in database: " + currentVersionName);
        }

        int major = Integer.parseInt(matcher.group(1));
        // group(2) (Minor) et group(3) (Patch) peuvent être null s'ils ne sont pas présents
        int minor = matcher.group(2) != null ? Integer.parseInt(matcher.group(2)) : 0;
        int patch = matcher.group(3) != null ? Integer.parseInt(matcher.group(3)) : 0;

        if (MAIN_BRANCH_NAME.equalsIgnoreCase(branchName)) {
            // Logique pour la branche "main" -> Incrémente Major, reset Minor/Patch à 0
            int nextMajor = major + 1;
            String nextName = String.format("V%d.0", nextMajor);
            logger.info("Incrementing version on '{}' branch: '{}' -> '{}'", branchName, currentVersionName, nextName);
            return nextName;
        } else {
            // Logique pour les branches "feature" -> Incrémente le dernier niveau présent
            if (matcher.group(3) != null) {
                // Si Patch existe (V.M.P) -> Incrémente Patch
                int nextPatch = patch + 1;
                String nextName = String.format("V%d.%d.%d", major, minor, nextPatch);
                logger.info("Incrementing patch version on '{}' branch: '{}' -> '{}'", branchName, currentVersionName, nextName);
                return nextName;
            } else if (matcher.group(2) != null) {
                // Si Minor existe mais pas Patch (V.M) -> Incrémente Minor
                int nextMinor = minor + 1;
                String nextName = String.format("V%d.%d", major, nextMinor);
                logger.info("Incrementing minor version on '{}' branch: '{}' -> '{}'", branchName, currentVersionName, nextName);
                return nextName;
            } else {
                // Si Seul Major existe (V) - Ne devrait pas arriver sur une branche non-main selon notre logique, mais sécurité
                logger.warn("Found only major version '{}' on non-main branch '{}'. Incrementing minor.", currentVersionName, branchName);
                String nextName = String.format("V%d.1", major); // V1 -> V1.1
                return nextName;
            }
        }
    }


    @Override
    @Transactional
    public String calculatePotentialNameForBranch(Long branchId) {
        logger.debug("Calculating potential next version name for branch ID: {}", branchId);
        Branch targetBranch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with id: " + branchId));

        Optional<Version> latestVersionOnTargetBranch = versionRepository.findTopByBranchOrderByIdDesc(targetBranch);

        // Appel la logique principale mais sans parentVersionId explicite,
        // car on est sur une branche *existante* dont on cherche la suite.
        return this.generateNextVersionName(targetBranch, null, latestVersionOnTargetBranch);  }

}
