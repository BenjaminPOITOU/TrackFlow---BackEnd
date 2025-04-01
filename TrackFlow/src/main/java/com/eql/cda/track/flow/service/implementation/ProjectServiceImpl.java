package com.eql.cda.track.flow.service.implementation;


import com.eql.cda.track.flow.dto.projectDto.ProjectCreateDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectUpdateDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectViewDto;
import com.eql.cda.track.flow.entity.Project;
import com.eql.cda.track.flow.entity.ProjectMusicalGenderAdded;
import com.eql.cda.track.flow.entity.ProjectMusicalGenderPreDefined;
import com.eql.cda.track.flow.entity.ProjectPurpose;
import com.eql.cda.track.flow.entity.ProjectTag;
import com.eql.cda.track.flow.entity.User;
import com.eql.cda.track.flow.repository.ProjectRepository;
import com.eql.cda.track.flow.repository.UserRepository;
import com.eql.cda.track.flow.service.ProjectService;
import com.eql.cda.track.flow.validation.Constants;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import org.springframework.data.domain.Pageable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.eql.cda.track.flow.validation.Constants.RECENT_PROJECT_COUNT;

@Service
public class ProjectServiceImpl implements ProjectService {

    ProjectRepository projectRepository;
    UserRepository userRepository;


    @Override
    @Transactional
    public ProjectCreateDto createProject(Long userId, ProjectCreateDto projectCreateDto) {

                                    /// START VERIFICATIONS ///

            if (projectCreateDto == null) {
                throw new IllegalArgumentException("Project creation data cannot be null.");
            }

            if (!StringUtils.hasText(projectCreateDto.getTitle())) {
                throw new IllegalArgumentException("Project title is mandatory and cannot be empty or blank.");
            }

            if (projectCreateDto.getProjectStatus() == null) {
                throw new IllegalArgumentException("Project status is mandatory.");
            }

            // 5. Vérification de l'URL d'Illustration (Optionnel, mais si fournie, non vide)
            if (projectCreateDto.getIllustration() != null && !StringUtils.hasText(projectCreateDto.getIllustration())) {
                throw new IllegalArgumentException("Illustration URL, if provided, cannot be blank.");
            }
            // Optionnel plus avancé : Vérifier si l'URL a un format valide (plus complexe)
             try {
               if (StringUtils.hasText(projectCreateDto.getIllustration())) {
                   new java.net.URL(projectCreateDto.getIllustration()).toURI();
                }
             } catch (java.net.MalformedURLException | java.net.URISyntaxException e) {
                throw new IllegalArgumentException("Illustration URL format is invalid.", e);
             }


            if (projectCreateDto.getProjectType() == null) {
                throw new IllegalArgumentException("Project type is mandatory.");
            }

                                    /// END VERIFICATIONS ///



        User creator = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Creator user not found with ID: " + userId));

        Project newProject = new Project();

        newProject.setUser(creator);

        newProject.setTitle(projectCreateDto.getTitle().trim()); // trim() pour enlever les espaces avant/après
        newProject.setDescription(projectCreateDto.getDescription() != null ? projectCreateDto.getDescription().trim() : null);
        newProject.setIllustration(projectCreateDto.getIllustration()); // Supposons que l'URL est stockée telle quelle
        newProject.setProjectStatus(projectCreateDto.getProjectStatus());
        newProject.setProjectType(projectCreateDto.getProjectType());
        newProject.setProjectCommercialStatus(projectCreateDto.getProjectCommercialStatus());


        newProject.setProjectPurposes(projectCreateDto.getProjectPurposes() != null ? new HashSet<>(projectCreateDto.getProjectPurposes()) : new HashSet<>());
        newProject.setProjectMusicalGendersPreDefined(projectCreateDto.getProjectMusicalGendersPreDefined() != null ? new HashSet<>(projectCreateDto.getProjectMusicalGendersPreDefined()) : new HashSet<>());
        newProject.setProjectMusicalGenderAddedSet(projectCreateDto.getProjectMusicalGenderAddedSet() != null ? new HashSet<>(projectCreateDto.getProjectMusicalGenderAddedSet()) : new HashSet<>());
        newProject.setProjectTags(projectCreateDto.getProjectTagSet() != null ? new HashSet<>(projectCreateDto.getProjectTagSet()) : new HashSet<>());



        Project savedProject = projectRepository.save(newProject);

        return mapToProjectCreateDto(savedProject);

    }


    @Override
    @Transactional
    public ProjectViewDto getCurrentProjectInfo(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Project ID cannot be null.");
        }

          Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + id));
        return mapEntityToProjectViewDto(project);
    }


    @Override
    @Transactional
    public List<ProjectViewDto> getRecentProjects() {
        //Pageable encapsule les informations dont la base
        // de données a besoin pour savoir quelle page
        // renvoyer et comment trier les résultats avant de les découper en pages :
        Pageable pageable = PageRequest.of(
                0,                                      // Numéro de la page (0 pour la première)
                RECENT_PROJECT_COUNT,                   // Nombre d'éléments par page
                Sort.by(Sort.Direction.DESC, "createdDate") // Tri par createdDate descendant
        );

        // 2. Exécuter la requête via le repository
        //    findAll(Pageable) retourne un objet Page<Project>
        Page<Project> recentProjectsPage = projectRepository.findAll(pageable);

        // 3. Extraire la liste des projets depuis l'objet Page
        List<Project> recentProjects = recentProjectsPage.getContent();

        // 4. Mapper la liste d'entités Project vers une liste de ProjectViewDto
        return recentProjects.stream() // Crée un stream à partir de la liste
                .map(this::mapEntityToProjectViewDto) // Applique la méthode de mapping à chaque projet
                .collect(Collectors.toList()); // Collec
    }

    @Override
    @Transactional
    public void updateProject(Long id, ProjectUpdateDto projectUpdateDto) {

        // 1. Validation des entrées de base
        if (id == null) {
            throw new IllegalArgumentException("Project ID cannot be null for update.");
        }
        if (projectUpdateDto == null) {
            throw new IllegalArgumentException("Project update data cannot be null.");
        }

        // 2. Récupérer l'entité existante
        Project projectToUpdate = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + id + " for update."));

        // TODO: Ajouter une vérification des permissions ici si nécessaire
        // (Ex: L'utilisateur actuel a-t-il le droit de modifier CE projet ?)
        // checkPermissionToUpdate(currentUser, projectToUpdate);

        // 3. Appliquer les modifications (seulement si la valeur est fournie dans le DTO)
        boolean changed = false; // Flag pour savoir si une modification a eu lieu

        // Titre
        if (projectUpdateDto.getTitle() != null && StringUtils.hasText(projectUpdateDto.getTitle())) {
            String newTitle = projectUpdateDto.getTitle().trim();
            // Re-valider la longueur
            if (newTitle.length() > Constants.PROJECT_TITLE_MAX_LENGTH) {
                throw new IllegalArgumentException(Constants.PROJECT_TITLE_MAX_LENGTH_MSG);
            }
            // Vérifier si la valeur a réellement changé avant de setter
            if (!newTitle.equals(projectToUpdate.getTitle())) {
                projectToUpdate.setTitle(newTitle);
                changed = true;
            }
        }

        // Description (Attention: permet potentiellement de mettre à jour vers null ou vide si DTO le contient explicitement)
        if (projectUpdateDto.getDescription() != null) {
            String newDescription = projectUpdateDto.getDescription().trim();
            // Re-valider la longueur
            if (newDescription.length() > Constants.PROJECT_DESC_MAX_LENGTH) {
                throw new IllegalArgumentException(Constants.PROJECT_DESC_MAX_LENGTH_MSG);
            }
            // Vérifier si la valeur a réellement changé (y compris passage de valeur à null/vide ou inverse)
            if (!Objects.equals(newDescription, projectToUpdate.getDescription())) {
                projectToUpdate.setDescription(newDescription);
                changed = true;
            }
        }

        // Illustration URL
        if (projectUpdateDto.getIllustration() != null) {
            // Ajouter validation de format URL si nécessaire
            if (!Objects.equals(projectUpdateDto.getIllustration(), projectToUpdate.getIllustration())) {
                projectToUpdate.setIllustration(projectUpdateDto.getIllustration());
                changed = true;
            }
        }

        // Project Status
        if (projectUpdateDto.getProjectStatus() != null) {
            // Ajouter validation métier si nécessaire (ex: peut-on passer de PUBLISHED à DRAFT ?)
            if (projectUpdateDto.getProjectStatus() != projectToUpdate.getProjectStatus()) {
                projectToUpdate.setProjectStatus(projectUpdateDto.getProjectStatus());
                changed = true;
            }
        }

        // Project Type
        if (projectUpdateDto.getProjectType() != null) {
            if (projectUpdateDto.getProjectType() != projectToUpdate.getProjectType()) {
                projectToUpdate.setProjectType(projectUpdateDto.getProjectType());
                changed = true;
            }
        }

        // Project Commercial Status
        if (projectUpdateDto.getProjectCommercialStatus() != null) {
            if (projectUpdateDto.getProjectCommercialStatus() != projectToUpdate.getProjectCommercialStatus()) {
                projectToUpdate.setProjectCommercialStatus(projectUpdateDto.getProjectCommercialStatus());
                changed = true;
            }
        }

        // Collections (Exemple pour ProjectPurposes - Remplace entièrement le set existant)
        // Adaptez si vous avez besoin d'une logique d'ajout/suppression partielle
        if (projectUpdateDto.getProjectPurposes() != null) {
            // Convertir le nouveau set pour comparaison et affectation
            HashSet<ProjectPurpose> newPurposes = new HashSet<>(projectUpdateDto.getProjectPurposes());
            // Vérifier si le set a changé avant de remplacer
            if (!Objects.equals(newPurposes, projectToUpdate.getProjectPurposes())) {
                projectToUpdate.setProjectPurposes(newPurposes);
                changed = true;
            }
        }

        // Répétez pour les autres collections (MusicalGendersPreDefined, MusicalGenderAddedSet, ProjectTags) si elles sont dans ProjectUpdateDto
        if (projectUpdateDto.getProjectMusicalGendersPreDefined() != null) {
            HashSet<ProjectMusicalGenderPreDefined> newGenders = new HashSet<>(projectUpdateDto.getProjectMusicalGendersPreDefined());
            if (!Objects.equals(newGenders, projectToUpdate.getProjectMusicalGendersPreDefined())) {
                projectToUpdate.setProjectMusicalGendersPreDefined(newGenders);
                changed = true;
            }
        }
        if (projectUpdateDto.getProjectMusicalGenderAddedSet() != null) {
            HashSet<ProjectMusicalGenderAdded> newAddedGenders = new HashSet<>(projectUpdateDto.getProjectMusicalGenderAddedSet());
            if (!Objects.equals(newAddedGenders, projectToUpdate.getProjectMusicalGenderAddedSet())) {
                projectToUpdate.setProjectMusicalGenderAddedSet(newAddedGenders);
                changed = true;
            }
        }
        // Supposons que ProjectUpdateDto ait aussi un champ pour les tags
        if (projectUpdateDto.getProjectTagSet() != null) {
            HashSet<ProjectTag> newTags = new HashSet<>(projectUpdateDto.getProjectTagSet());
            if (!Objects.equals(newTags, projectToUpdate.getProjectTags())) {
                projectToUpdate.setProjectTags(newTags);
                changed = true;
            }
        }


        // 4. Sauvegarder si des changements ont été détectés
        //    Grâce à @Transactional, Hibernate pourrait détecter les changements (dirty checking)
        //    et sauvegarder automatiquement au commit. Cependant, appeler save() explicitement
        //    est plus clair et garantit le déclenchement des callbacks (comme @LastModifiedDate).
        if (changed) {
            projectRepository.save(projectToUpdate);
        }
        // Si rien n'a changé, on ne fait rien (pas d'écriture inutile en BDD).
    }

    @Override
    @Transactional // Transaction nécessaire pour la mise à jour
    public void archiveProject(long id) {
        // 1. Validation de base de l'ID
        if (id <= 0) {
            throw new IllegalArgumentException("Project ID must be positive for archiving.");
        }

        // 2. Récupérer l'entité existante
        Project projectToArchive = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + id + " for archiving."));

        // TODO: Ajouter une vérification des permissions ici si nécessaire
        // (Ex: L'utilisateur actuel a-t-il le droit d'archiver CE projet ?)
        // checkPermissionToArchive(currentUser, projectToArchive);

        // 3. Vérifier si le projet est déjà archivé
        //    (Assurez-vous que votre entité Project a une méthode isArchived() ou getArchived())
        if (projectToArchive.getArchived()) {
            throw new IllegalStateException("Project with ID: " + id + " is already archived.");
        }

        projectToArchive.setArchived(true);
        projectRepository.save(projectToArchive);


    }


    @Override
    @Transactional
    public void deleteProject(long id) {

        if (!projectRepository.existsById(id)) {
            throw new EntityNotFoundException("Project not found with ID: " + id + " for deletion.");
        }
        projectRepository.deleteById(id);

    }

    private ProjectCreateDto mapToProjectCreateDto(Project project) {
        ProjectCreateDto dto = new ProjectCreateDto();
        dto.setId(project.getId());
        dto.setTitle(project.getTitle());
        dto.setDescription(project.getDescription());
        dto.setIllustration(project.getIllustration());
        dto.setProjectStatus(project.getProjectStatus());
        dto.setProjectType(project.getProjectType());
        dto.setProjectCommercialStatus(project.getProjectCommercialStatus());
        dto.setProjectPurposes(project.getProjectPurposes());
        dto.setProjectMusicalGendersPreDefined(project.getProjectMusicalGendersPreDefined());
        dto.setProjectMusicalGenderAddedSet(project.getProjectMusicalGenderAddedSet());
        dto.setProjectTagSet(project.getProjectTags());
        dto.setCreationDate(project.getCreationDate());

        return dto;
    }
    private ProjectViewDto mapEntityToProjectViewDto(Project project) {

        ProjectViewDto dto = new ProjectViewDto();

        dto.setTitle(project.getTitle());
        dto.setDescription(project.getDescription());
        dto.setIllustration(project.getIllustration());
        dto.setProjectStatus(project.getProjectStatus());
        dto.setProjectType(project.getProjectType());
        dto.setProjectCommercialStatus(project.getProjectCommercialStatus());
        dto.setCompositionsTotal(project.getCompositions().size());

        dto.setProjectPurposes(new HashSet<>(project.getProjectPurposes()));
        dto.setProjectMusicalGendersPreDefined(new HashSet<>(project.getProjectMusicalGendersPreDefined()));
        dto.setProjectMusicalGenderAddedSet(new HashSet<>(project.getProjectMusicalGenderAddedSet()));
        dto.setProjectTagSet(new HashSet<>(project.getProjectTags()));


        dto.setCreationDate(project.getCreationDate());
        dto.setUpdateDate(project.getLastUpdateDate());
        return dto;
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
