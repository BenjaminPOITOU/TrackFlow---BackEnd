package com.eql.cda.track.flow.service.implementation;

import com.eql.cda.track.flow.dto.branchDto.BranchSummaryDto;
import com.eql.cda.track.flow.entity.Branch;
import com.eql.cda.track.flow.entity.Composition;
import com.eql.cda.track.flow.repository.BranchRepository;
import com.eql.cda.track.flow.repository.CompositionRepository;
import com.eql.cda.track.flow.service.BranchService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BranchServiceImpl implements BranchService {

    private static final Logger logger = LogManager.getLogger();

    BranchRepository branchRepository;
    CompositionRepository compositionRepository;

    @Autowired
    public BranchServiceImpl(BranchRepository branchRepository, CompositionRepository compositionRepository) {
        this.branchRepository = branchRepository;
        this.compositionRepository = compositionRepository;
    }

    @Override
    public List<BranchSummaryDto> getAllBranches(Long compositionId) {

        Objects.requireNonNull(compositionId, "Composition ID cannot be null");
        if(!compositionRepository.existsById(compositionId)){
            throw new EntityNotFoundException("Composition not found with ID: " + compositionId + ", cannot delete.");
        }

        List<Branch> branches = branchRepository.findAllByCompositionId(compositionId);

       return branches.stream()
               .map(this::mapEntityToSummaryDto)
               .collect(Collectors.toList());


    }

    @Override
    @Transactional // Important pour que la création soit dans la même transaction si nécessaire
    public Branch findOrCreateBranch(Composition composition, Long branchId, String newBranchName, Long parentBranchId, String branchDescription) {
        Branch branch;
        Long compositionId = composition.getId(); // Récupère l'ID pour la recherche

        if (branchId != null) {
            // --- Logique pour trouver par ID (reste inchangée) ---
            logger.debug("Finding existing branch with ID: {} for Composition {}", branchId, compositionId);
            branch = branchRepository.findById(branchId)
                    .orElseThrow(() -> {
                        logger.error("Branch not found with ID: {}", branchId);
                        return new EntityNotFoundException("Branch not found with id: " + branchId);
                    });
            if (!branch.getComposition().getId().equals(compositionId)) {
                logger.error("Branch {} does not belong to Composition {}", branchId, compositionId);
                throw new IllegalArgumentException("Branch " + branchId + " does not belong to Composition " + compositionId);
            }
            logger.info("Using existing branch: ID={}, Name={} for Composition {}", branch.getId(), branch.getName(), compositionId);

        } else if (newBranchName != null && !newBranchName.isBlank()) {
            // --- Logique pour trouver OU créer par nom --- (MODIFIÉE)
            String cleanBranchName = newBranchName.trim(); // Nettoyer le nom
            logger.debug("Attempting to find or create branch with name: '{}' for Composition {}", cleanBranchName, compositionId);

            // 1. Essayer de trouver la branche par nom ET composition ID
            Optional<Branch> existingBranchOpt = branchRepository.findByCompositionIdAndName(compositionId, cleanBranchName);

            if (existingBranchOpt.isPresent()) {
                // 2a. Si trouvée, utiliser la branche existante
                branch = existingBranchOpt.get();
                logger.info("Found existing branch by name: ID={}, Name={} for Composition {}", branch.getId(), branch.getName(), compositionId);
                // Optionnel : Mettre à jour la description si une nouvelle est fournie ?
                if (branchDescription != null && !branchDescription.equals(branch.getDescription())) {
                    logger.debug("Updating description for existing branch {} to '{}'", branch.getId(), branchDescription);
                    branch.setDescription(branchDescription);
                    branch = branchRepository.save(branch); // Sauvegarder la mise à jour de description
                }

            } else {
                // 2b. Si non trouvée, créer la nouvelle branche
                logger.debug("Branch '{}' not found for Composition {}. Creating new branch. ParentBranchId: {}", cleanBranchName, compositionId, parentBranchId);
                branch = new Branch();
                branch.setName(cleanBranchName);
                branch.setComposition(composition);
                branch.setDescription(branchDescription); // Utiliser la description fournie

                // Valider et lier la branche parente si nécessaire (logique inchangée pour l'instant)
                if (parentBranchId != null) {
                    Branch parentBranch = findAndValidateParentBranch(parentBranchId, compositionId);
                    // Lier si tu as un champ parent: branch.setParentBranch(parentBranch);
                }

                // Sauvegarder la nouvelle branche
                branch = branchRepository.save(branch);
                logger.info("Created new branch: ID={}, Name={} for Composition {}", branch.getId(), branch.getName(), compositionId);
            }

        } else {
            // --- Logique d'erreur (reste inchangée) ---
            logger.error("Logic error: findOrCreateBranch called without branchId or newBranchName for Composition {}", compositionId);
            throw new IllegalArgumentException("Internal error: Either branchId or newBranchName must be provided for branch creation/retrieval.");
        }
        return branch;
    }

    private Branch findAndValidateParentBranch(Long parentBranchId, Long compositionId) {
        logger.debug("Finding parent branch with ID: {}", parentBranchId);
        Branch parentBranch = branchRepository.findById(parentBranchId)
                .orElseThrow(() -> {
                    logger.error("Parent Branch not found with ID: {}", parentBranchId);
                    return new EntityNotFoundException("Parent Branch not found with id: " + parentBranchId);
                });
        // Valider l'appartenance de la branche parente à la même composition
        if (!parentBranch.getComposition().getId().equals(compositionId)) {
            logger.error("Parent Branch {} does not belong to Composition {}", parentBranchId, compositionId);
            throw new IllegalArgumentException("Parent Branch " + parentBranchId + " does not belong to Composition " + compositionId);
        }
        logger.debug("Parent Branch {} found and validated for Composition {}", parentBranchId, compositionId);
        return parentBranch;
    }

    private BranchSummaryDto mapEntityToSummaryDto(Branch branche) {

        if (branche == null) {
            return null;
        }

        BranchSummaryDto branchSummaryDto = new BranchSummaryDto();
        branchSummaryDto.setId(branche.getId());
        branchSummaryDto.setName(branche.getName());

        return branchSummaryDto;


    }
}
