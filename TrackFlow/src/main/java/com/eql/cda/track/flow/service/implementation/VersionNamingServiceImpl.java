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

    public static final String MAIN_BRANCH_NAME = "main";

    private final VersionRepository versionRepository;
    private final BranchRepository branchRepository;

    public VersionNamingServiceImpl(VersionRepository versionRepository, BranchRepository branchRepository) {
        this.versionRepository = versionRepository;
        this.branchRepository = branchRepository;
    }

    @Override
    public String generateNextVersionName(Version parentVersion, Branch targetBranch) {
        logger.debug("Generating next version name for target branch '{}', originating from parent version '{}'",
                targetBranch.getName(), parentVersion.getName());

        // 1. Rechercher la dernière version sur la branche CIBLE (TRI M.m !)
        // Assumons que la méthode repo trie maintenant V[M].[m] correctement.
        Optional<Version> latestVersionOnTargetBranchOpt = versionRepository.findTopByBranchOrderByNameDesc(targetBranch);

        if (latestVersionOnTargetBranchOpt.isPresent()) {
            // CAS 1: Branche EXISTANTE -> Incrémenter (MAJEUR si main, MINOR sinon)
            Version latestVersion = latestVersionOnTargetBranchOpt.get();
            logger.debug("Found latest version '{}' on target branch '{}'. Incrementing.", latestVersion.getName(), targetBranch.getName());
            return incrementExistingVersion(latestVersion.getName(), targetBranch.getName());
        } else {
            // CAS 2: PREMIÈRE version sur cette branche cible.
            // Selon la nouvelle règle, parentVersion DOIT être sur 'main'.
            logger.debug("No existing version on branch '{}'. Generating first name based on parent '{}' (must be on main).",
                    targetBranch.getName(), parentVersion.getName());
            // La validation que parent est sur main et en V.X.0 est faite DANS la méthode helper.
            return generateFirstVersionNameForNewBranch(parentVersion);
        }
    }

    /**
     * Génère le nom de la PREMIÈRE version pour une NOUVELLE branche (qui DOIT être créée depuis 'main').
     * Logique: Si la source sur main est V[M].0, la nouvelle branche commence à V[M].1.
     *
     * @param mainSourceVersion La version sur la branche 'main' depuis laquelle la nouvelle branche est créée.
     * @return Le premier nom de version pour la nouvelle branche (ex: "V2.1").
     */
    private String generateFirstVersionNameForNewBranch(Version mainSourceVersion) {
        String sourceName = mainSourceVersion.getName();
        String sourceBranchName = mainSourceVersion.getBranch().getName(); // Vérifier nullité avant

        // Validation: S'assurer que la source est bien sur 'main' (règle métier)
        if(!MAIN_BRANCH_NAME.equalsIgnoreCase(sourceBranchName)) {
            logger.error("Attempted to generate new branch name from a non-main source version '{}' on branch '{}'. This violates the new rule.", sourceName, sourceBranchName);
            // Lever une exception car c'est une violation des préconditions
            throw new IllegalArgumentException("Cannot create new branch from non-main branch source: " + sourceName);
        }

        Matcher matcher = VERSION_NAME_PATTERN.matcher(sourceName);
        if (!matcher.matches() || matcher.group(2) == null || !matcher.group(2).equals("0")) {
            logger.error("Cannot parse source version name '{}' from main branch (expected V[M].0 format). Fallback or error?", sourceName);
            // Fallback risqué, levons une exception
            throw new IllegalArgumentException("Invalid source version format from main branch: " + sourceName + ". Expected V[M].0.");
        }

        int major = Integer.parseInt(matcher.group(1));
        // Le mineur est 0 par définition de main, on le fixe à 1 pour la nouvelle branche.
        int newMinor = 1;

        logger.debug("Source version '{}' from main. New branch starts at V{}.{}", sourceName, major, newMinor);
        return String.format("V%d.%d", major, newMinor);
    }


    private String incrementExistingVersion(String latestVersionName, String branchName) {

        Matcher matcher = VERSION_NAME_PATTERN.matcher(latestVersionName);
        if (!matcher.matches()) {
            logger.error("Cannot parse existing version name '{}' (expected V.M.m format). Fallback.", latestVersionName);
            return latestVersionName + "-next"; // Fallback
        }

        int major = Integer.parseInt(matcher.group(1));
        int minor = (matcher.group(2) != null) ? Integer.parseInt(matcher.group(2)) : 0; // Minor=0 si absent

        logger.trace("Parsed version '{}': Major={}, Minor={}", latestVersionName, major, minor);

        if (MAIN_BRANCH_NAME.equalsIgnoreCase(branchName)) {
            // Incrémente MAJEUR, reset Minor
            major++;
            minor = 0;
            logger.debug("Incrementing MAJOR on '{}' branch to V{}.0", branchName, major);
            return String.format("V%d.0", major);
        } else {
            // Incrémente MINOR
            minor++;
            logger.debug("Incrementing MINOR on '{}' branch to V{}.{}", branchName, major, minor);
            return String.format("V%d.%d", major, minor);
        }
    }




    /**
     * Calcule le nom potentiel de la PROCHAINE version si une nouvelle était créée sur la branche spécifiée.
     * ATTENTION: Fonctionne correctement SEULEMENT s'il existe déjà une version sur cette branche.
     * Si c'est la première version sur la branche, le calcul est APPROXIMATIF car la version parente
     * n'est pas connue par cette méthode.
     *
     * @param branchId L'ID de la branche pour laquelle calculer le nom potentiel.
     * @return Le nom de version potentiel calculé (peut être un placeholder si c'est la première version).
     */
    @Override
    public String calculatePotentialNameForBranch(Long branchId) {
        logger.debug("Calculating potential next V.M.m name for branch ID: {}", branchId);

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found: " + branchId));

        Optional<Version> latestVersionOpt = versionRepository.findTopByBranchOrderByNameDesc(branch);

        if (latestVersionOpt.isPresent()) {
            // CAS OK: Incrémente la dernière V.M.m trouvée sur cette branche
            Version latestVersion = latestVersionOpt.get();
            logger.debug("Found latest V.M.m '{}' on branch '{}'. Calculating next.", latestVersion.getName(), branch.getName());
            return incrementExistingVersion(latestVersion.getName(), branch.getName()); // Retourne V[M].0 ou V[M].[m+1]
        } else {
            // CAS ELSE: Première version -> on doit deviner le parent sur 'main'
            logger.debug("No existing V.M.m on branch '{}'. Need to determine potential first name (V[M].1).", branch.getName());

            // --- Approche Heuristique: Trouver la DERNIÈRE V[M].0 sur 'main' ---
            // Besoin d'une méthode repo: findLatestVersionOnMainBranch() ou similaire
            // Qui trouve la plus haute V[M].0. Doit trier !
            Optional<Version> latestMainVersionOpt = versionRepository.findLatestVersionOnMainBranch();
            if (latestMainVersionOpt.isPresent()) {
                Version potentialMainParent = latestMainVersionOpt.get();
                logger.debug("Using latest main version '{}' as potential parent for potential first name on branch '{}'.",
                        potentialMainParent.getName(), branch.getName());
                // Appeler le helper qui attend un parent main V[M].0
                try {
                    return generateFirstVersionNameForNewBranch(potentialMainParent); // Devrait retourner V[M].1
                } catch (IllegalArgumentException e) {
                    // Si le dernier sur main n'est pas V.X.0 (ne devrait pas arriver)
                    logger.error("Latest version on main ('{}') is not in V[M].0 format. Cannot calculate potential name. Error: {}",
                            potentialMainParent.getName(), e.getMessage());
                    return "V?.?-error";
                }
            } else {
                // Pas de version sur 'main' du tout ? Très peu probable sauf début projet.
                logger.warn("No version found on main branch at all. Assuming first potential version is V1.1 for branch '{}'.", branch.getName());
                return "V1.1";
            }
            // Toujours PAS IDEAL: Si l'utilisateur crée depuis une V2.0 alors que V3.0 existe,
            // le potentiel calculé ici sera basé sur V3.0. L'API avec parentId explicite reste la meilleure solution.
        }

    }
    @Override
    public String generateFirstEverVersionName() {
        final String firstVersionName = "V1.0";
        logger.info("Generating the first ever version name: {}", firstVersionName);
        return firstVersionName;
    }

}
