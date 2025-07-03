package com.eql.cda.track.flow.service.implementation;

import com.eql.cda.track.flow.dto.branchDto.BranchCreateDto;
import com.eql.cda.track.flow.dto.branchDto.BranchSummaryDto;
import com.eql.cda.track.flow.dto.branchDto.BranchUpdateDto;
import com.eql.cda.track.flow.entity.Branch;
import com.eql.cda.track.flow.entity.Composition;
import com.eql.cda.track.flow.repository.BranchRepository;
import com.eql.cda.track.flow.repository.CompositionRepository;
import com.eql.cda.track.flow.service.BranchService;
import com.eql.cda.track.flow.service.mapper.BranchMapper;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link BranchService} interface. This class orchestrates
 * all business logic related to branches, interacting with the persistence layer,
 * handling validation, and mapping data.
 */
@Service
@Transactional
public class BranchServiceImpl implements BranchService {

    private static final Logger log = LoggerFactory.getLogger(BranchServiceImpl.class);

    private final BranchRepository branchRepository;
    private final CompositionRepository compositionRepository;
    private final BranchMapper branchMapper;

    /**
     * Constructs the service with its required dependencies.
     *
     * @param branchRepository The repository for branch data access.
     * @param compositionRepository The repository for composition data access to validate context.
     * @param branchMapper The mapper for converting between branch entities and DTOs.
     */
    @Autowired
    public BranchServiceImpl(BranchRepository branchRepository, CompositionRepository compositionRepository, BranchMapper branchMapper) {
        this.branchRepository = branchRepository;
        this.compositionRepository = compositionRepository;
        this.branchMapper = branchMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BranchSummaryDto createBranch(Long projectId, Long compositionId, BranchCreateDto branchCreateDto) {
        Composition composition = findCompositionAndValidateContext(projectId, compositionId);

        Branch branch = branchMapper.toEntity(branchCreateDto);
        branch.setComposition(composition);

        if (branchCreateDto.getBranchParentId() != null) {
            Branch parentBranch = branchRepository.findById(branchCreateDto.getBranchParentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent branch not found with id: " + branchCreateDto.getBranchParentId()));
            branch.setParent(parentBranch);
        }

        Branch savedBranch = branchRepository.save(branch);
        log.info("Created branch with ID {} for composition ID {}", savedBranch.getId(), compositionId);

        return branchMapper.toSummaryDto(savedBranch);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<BranchSummaryDto> getAllBranchesForComposition(Long projectId, Long compositionId) {
        findCompositionAndValidateContext(projectId, compositionId);
        List<Branch> branches = branchRepository.findByCompositionIdOrderByLastUpdateDateDesc(compositionId);
        return branches.stream()
                .map(branchMapper::toSummaryDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BranchSummaryDto updateBranch(Long projectId, Long compositionId, Long branchId, BranchUpdateDto branchUpdateDto) {
        Branch branchToUpdate = findBranchAndValidateContext(projectId, compositionId, branchId);

        branchMapper.updateFromDto(branchUpdateDto, branchToUpdate);

        if (branchUpdateDto.getParentBranchId() != null) {
            if (Objects.equals(branchUpdateDto.getParentBranchId(), branchId)) {
                throw new IllegalArgumentException("A branch cannot be its own parent.");
            }
            Branch newParent = branchRepository.findById(branchUpdateDto.getParentBranchId())
                    .orElseThrow(() -> new EntityNotFoundException("New parent branch not found with id: " + branchUpdateDto.getParentBranchId()));
            branchToUpdate.setParent(newParent);
        }

        Branch updatedBranch = branchRepository.save(branchToUpdate);
        log.info("Updated branch with ID {}", updatedBranch.getId());

        return branchMapper.toSummaryDto(updatedBranch);
    }

    private Composition findCompositionAndValidateContext(Long projectId, Long compositionId) {
        Composition composition = compositionRepository.findById(compositionId)
                .orElseThrow(() -> new EntityNotFoundException("Composition not found with id: " + compositionId));

        if (composition.getProject() == null || !Objects.equals(composition.getProject().getId(), projectId)) {
            log.warn("Access violation: Composition {} does not belong to project {}", compositionId, projectId);
            throw new AccessDeniedException("Composition does not belong to the specified project.");
        }
        return composition;
    }

    private Branch findBranchAndValidateContext(Long projectId, Long compositionId, Long branchId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException("Branch not found with id: " + branchId));

        Composition composition = branch.getComposition();
        if (composition == null || !Objects.equals(composition.getId(), compositionId)) {
            log.warn("Access violation: Branch {} does not belong to composition {}", branchId, compositionId);
            throw new AccessDeniedException("Branch does not belong to the specified composition.");
        }

        if (composition.getProject() == null || !Objects.equals(composition.getProject().getId(), projectId)) {
            log.warn("Access violation: The project context is incorrect for branch {}", branchId);
            throw new AccessDeniedException("Branch does not belong to a composition in the specified project.");
        }

        return branch;
    }
}