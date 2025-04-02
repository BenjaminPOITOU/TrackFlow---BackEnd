package com.eql.cda.track.flow.service.implementation;

import com.eql.cda.track.flow.dto.compositionDto.CompositionCreateDto;
import com.eql.cda.track.flow.dto.compositionDto.CompositionSummaryDto;
import com.eql.cda.track.flow.dto.compositionDto.CompositionUpdateDto;
import com.eql.cda.track.flow.dto.compositionDto.CompositionViewDto;
import com.eql.cda.track.flow.entity.Branch;
import com.eql.cda.track.flow.entity.Composition;
import com.eql.cda.track.flow.entity.Project;
import com.eql.cda.track.flow.entity.ProjectMusicalGenderPreDefined;
import com.eql.cda.track.flow.entity.Version;
import com.eql.cda.track.flow.repository.CompositionRepository;
import com.eql.cda.track.flow.repository.ProjectRepository;
import com.eql.cda.track.flow.repository.UserRepository;
import com.eql.cda.track.flow.service.CompositionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import org.springframework.data.domain.Pageable;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


import static com.eql.cda.track.flow.validation.Constants.RECENT_COMPOSITION_COUNT;


@Service
public class CompositionServiceImpl implements CompositionService {

    CompositionRepository compositionRepository;
    ProjectRepository projectRepository;
    UserRepository userRepository;



    @Override
    @Transactional
    public CompositionViewDto createComposition(Long projectId, CompositionCreateDto compositionCreateDto) {

        try {
            if (StringUtils.hasText(compositionCreateDto.getIllustration())) {
                new java.net.URL(compositionCreateDto.getIllustration()).toURI();
            }
        } catch (java.net.MalformedURLException | java.net.URISyntaxException e) {
            throw new IllegalArgumentException("Illustration URL format is invalid.", e);
        }

             Project parentProject = projectRepository.findById(projectId)
                        .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + projectId));


        Composition composition = new Composition();

        composition.setTitle(compositionCreateDto.getTitle().trim());
        composition.setDescription(compositionCreateDto.getDescription() != null ? compositionCreateDto.getDescription().trim() : null );
        composition.setIllustration(compositionCreateDto.getIllustration() != null ? compositionCreateDto.getIllustration(): null);
        composition.setSubGenders(compositionCreateDto.getSubGender());
        composition.setCompositionStatus(compositionCreateDto.getCompositionStatus());
        composition.setProject(parentProject);
        composition.setBranches(null);
        composition.setCompositionStatus(compositionCreateDto.getCompositionStatus() !=null ? compositionCreateDto.getCompositionStatus(): null);

        Composition compositionCreated = compositionRepository.save(composition);
        return mapEntityToViewDto(compositionCreated);
    }

    @Override
    @Transactional
    public List<CompositionSummaryDto> getRecentSummaryCompositionDto(Long userId) {
        Objects.requireNonNull(userId, "Project ID cannot be null");
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found with ID: " + userId);
        }

        Pageable pageable = PageRequest.of(0, RECENT_COMPOSITION_COUNT, Sort.by(Sort.Direction.DESC, "lastUpdateDate"));
        Page<Composition> recentCompositionsPage = compositionRepository.findAllByProjectUserId(userId, pageable);

        List<Composition> compositions = recentCompositionsPage.getContent();
        return compositions.stream()
                .map(this::mapEntityToSummaryDto) // Appel de la méthode de mapping
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<CompositionSummaryDto> getAllCompositions(Long projectId) {

        Objects.requireNonNull(projectId, "Project ID cannot be null");

        if (!projectRepository.existsById(projectId)) {
            throw new EntityNotFoundException("Project not found with ID: " + projectId);
                }

        List<Composition> compositions = compositionRepository.findByProjectId(projectId);


        return compositions.stream()
                .map(this::mapEntityToSummaryDto)
                .collect(Collectors.toList());


    }

    @Override
    @Transactional
    public CompositionViewDto getCurrentComposition(Long compositionId) {

        Objects.requireNonNull(compositionId, "Composition ID cannot be null");
        Composition composition = compositionRepository.findById(compositionId)
                .orElseThrow(() -> new EntityNotFoundException("Composition not found with id " + compositionId)
                );

        return mapEntityToViewDto(composition);
    }

    @Override
    @Transactional
    public CompositionViewDto updateComposition(Long compositionId, CompositionUpdateDto compositionUpdateDto) {

        Objects.requireNonNull(compositionId, "Composition ID cannot be null");
        Objects.requireNonNull(compositionUpdateDto, "Update DTO cannot be null");


        Composition compositionToUpdate = compositionRepository.findById(compositionId)
                .orElseThrow(() -> new EntityNotFoundException("Composition not found with ID: " + compositionId));


        boolean changed = false;


        if (compositionUpdateDto.getTitle() != null) {
            String trimmedTitle = compositionUpdateDto.getTitle().trim();
              if (!StringUtils.hasText(trimmedTitle)) {
                throw new IllegalArgumentException("Composition title cannot be updated to empty or blank.");
            }
            if (!Objects.equals(compositionToUpdate.getTitle(), trimmedTitle)) {
                compositionToUpdate.setTitle(trimmedTitle);
                changed = true;
            }
        }

        if (compositionUpdateDto.getDescription() != null) {
            String trimmedDescription = compositionUpdateDto.getDescription().trim();
            if (!Objects.equals(compositionToUpdate.getDescription(), trimmedDescription)) {
                compositionToUpdate.setDescription(trimmedDescription);
                changed = true;
            }
        }

        if (compositionUpdateDto.getIllustration() != null) {
              if (!Objects.equals(compositionToUpdate.getIllustration(), compositionUpdateDto.getIllustration())) {
                compositionToUpdate.setIllustration(compositionUpdateDto.getIllustration());
                changed = true;
            }
        }

        if (compositionUpdateDto.getCompositionStatus() != null) {
            if (compositionToUpdate.getCompositionStatus() != compositionUpdateDto.getCompositionStatus()) {
                compositionToUpdate.setCompositionStatus(compositionUpdateDto.getCompositionStatus());
                changed = true;
            }
        }

        if (compositionUpdateDto.getSubGender() != null) {
            List<String> newSubGenders = new ArrayList<>(compositionUpdateDto.getSubGender());
             compositionToUpdate.setSubGenders(newSubGenders);
             changed = true;
        }

        Composition savedComposition;
        if (changed) {
            savedComposition = compositionRepository.save(compositionToUpdate);
        } else {
           savedComposition = compositionToUpdate;
        }

            return mapEntityToViewDto(savedComposition);
    }

    @Override
    @Transactional
    public void deleteComposition(Long compositionId) {

        Objects.requireNonNull(compositionId, "Composition ID cannot be null");


        if (!compositionRepository.existsById(compositionId)) {
            throw new EntityNotFoundException("Composition not found with ID: " + compositionId + ", cannot delete.");
        }

          compositionRepository.deleteById(compositionId);

    }



    private CompositionSummaryDto mapEntityToSummaryDto(Composition composition) {

        if (composition == null) {
            return null;
        }

        CompositionSummaryDto dto = new CompositionSummaryDto();
        dto.setId(composition.getId());
        dto.setTitle(composition.getTitle());
        dto.setLastUpdateDate(composition.getLastUpdateDate());

        if (composition.getSubGenders() != null) {
            dto.setSubGenders(new ArrayList<>(composition.getSubGenders()));
        } else {
            dto.setSubGenders(new ArrayList<>());
        }


        dto.setTotalBranches(countBranches(composition));
        dto.setTotalVersions(countTotalVersions(composition));


        return dto;
    }
    private CompositionViewDto mapEntityToViewDto(Composition composition) {
        if (composition == null) {
            return null;
        }

        CompositionViewDto dto = new CompositionViewDto();

        dto.setId(composition.getId());
        dto.setTitle(composition.getTitle());
        dto.setCompositionStatus(composition.getCompositionStatus());
        dto.setDescription(composition.getDescription());
        dto.setIllustration(composition.getIllustration());
        dto.setCreatedDate(composition.getCreatedDate());
        dto.setLastUpdateDate(composition.getLastUpdateDate());


        Project project = composition.getProject();
        if (project != null) {
            dto.setProjectId(project.getId());
            dto.setProjectTitle(project.getTitle());

            Set<ProjectMusicalGenderPreDefined> genders = project.getProjectMusicalGendersPreDefined();
            if (genders != null) {
                dto.setProjectMusicalGenderPreDefinedList(new HashSet<>(genders)); // Copie dans un nouveau Set
            } else {
                dto.setProjectMusicalGenderPreDefinedList(Collections.emptySet());
            }
        } else {
            dto.setProjectId(null);
            dto.setProjectTitle(null);
            dto.setProjectMusicalGenderPreDefinedList(Collections.emptySet());
        }

        if (composition.getSubGenders() != null) {
            dto.setSubGenders(new ArrayList<>(composition.getSubGenders()));
        } else {
            dto.setSubGenders(new ArrayList<>());
        }

        // Assurez-vous que les méthodes countBranches et countTotalVersions existent et sont appelables ici
        dto.setTotalBranches(countBranches(composition));
        dto.setTotalVersions(countTotalVersions(composition));

        return dto;
    }


    private int countBranches(Composition composition) {
        if (composition == null) {
            return 0;
        }
        Collection<Branch> branches = composition.getBranches(); // Peut déclencher le chargement LAZY
        return (branches != null) ? branches.size() : 0;
    }
    private int countTotalVersions(Composition composition) {
        if (composition == null) {
            return 0;
        }

        Collection<Branch> branches = composition.getBranches(); // Peut déclencher le chargement LAZY de 'branches'
        if (branches == null || branches.isEmpty()) {
            return 0;
        }

        int totalVersions = 0;
        for (Branch branch : branches) {
            if (branch != null) {
                // IMPORTANT: Accéder à branch.getVersions() peut déclencher
                // le chargement LAZY de la collection 'versions' de CETTE branche.
                Collection<Version> versions = branch.getVersions();
                if (versions != null) {
                    totalVersions += versions.size();
                }
            }
        }
        return totalVersions;
    }



    @Autowired
    public void setCompositionRepository(CompositionRepository compositionRepository) {
        this.compositionRepository = compositionRepository;
    }
    @Autowired
    public void setProjectRepository(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
