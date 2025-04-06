package com.eql.cda.track.flow.service.implementation;


import com.eql.cda.track.flow.dto.projectDto.ProjectCreateDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectSummaryDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectUpdateDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectViewDto;
import com.eql.cda.track.flow.entity.Project;
import com.eql.cda.track.flow.entity.ProjectMusicalGenderPreDefined;
import com.eql.cda.track.flow.entity.ProjectPurpose;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.eql.cda.track.flow.validation.Constants.RECENT_PROJECT_COUNT;

@Service
public class ProjectServiceImpl implements ProjectService {

    ///  CLASSES - INVERSION DE DEPENDANCE ///
    ProjectRepository projectRepository;
    UserRepository userRepository;


    /// METHODES - COUCHE SERVICE ///


        @Override
        @Transactional
        public ProjectCreateDto createProject(Long userId, ProjectCreateDto projectCreateDto){// <-- Retourne ProjectViewDto

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
            if (projectCreateDto.getIllustration() != null && !StringUtils.hasText(projectCreateDto.getIllustration())) {
            throw new IllegalArgumentException("Illustration URL, if provided, cannot be blank.");
        }
            try {
            if (StringUtils.hasText(projectCreateDto.getIllustration())) {
                new URL(projectCreateDto.getIllustration()).toURI();
            }
        } catch (MalformedURLException | URISyntaxException e) {
            throw new IllegalArgumentException("Illustration URL format is invalid.", e);
        }
            if (projectCreateDto.getProjectType() == null) {
            throw new IllegalArgumentException("Project type is mandatory.");
        }
        /// END VERIFICATIONS ///

        // Récupération de l'utilisateur créateur
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Creator user not found with ID: " + userId));

        // Création de la nouvelle entité Project
        Project newProject = new Project();

        // --- Mapping des champs simples ---
            newProject.setUser(creator);
            newProject.setTitle(projectCreateDto.getTitle().trim());
            newProject.setDescription(projectCreateDto.getDescription() != null ? projectCreateDto.getDescription().trim() : null);
            newProject.setIllustration(projectCreateDto.getIllustration());
            newProject.setProjectStatus(projectCreateDto.getProjectStatus());
            newProject.setProjectType(projectCreateDto.getProjectType());
            newProject.setProjectCommercialStatus(projectCreateDto.getProjectCommercialStatus());

            newProject.setProjectPurposes(projectCreateDto.getProjectPurposes() != null ? new HashSet<>(projectCreateDto.getProjectPurposes()) : new HashSet<>());
            newProject.setProjectMusicalGendersPreDefined(projectCreateDto.getProjectMusicalGendersPreDefined() != null ? new HashSet<>(projectCreateDto.getProjectMusicalGendersPreDefined()) : new HashSet<>());



        Project savedProject = projectRepository.save(newProject);

        // --- Retourne le DTO de vue ---
        // Tu as besoin d'une méthode pour mapper l'entité Project vers ProjectViewDto
            return mapEntityToCreateDto(savedProject); // <-- Appelle ta méthode de mapping
    }
        @Override
        @Transactional
        public ProjectViewDto getCurrentProjectInfo (Long id){
            if (id == null) {
                throw new IllegalArgumentException("Project ID cannot be null.");
            }

            Project project = projectRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + id));
            return mapEntityToProjectViewDto(project);
        }

        @Override
        @Transactional
        public Page<ProjectSummaryDto> findRecentProjectsForUser(String username, Pageable pageable) {
            User user = userRepository.findByLogin(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
            Page<Project> recentProjectsPage = projectRepository.findByUser(user, pageable);
            return recentProjectsPage.map(this::mapEntityToProjectSummaryDto);
        }

        @Override
        @Transactional
        public List<ProjectViewDto> getAllProjects (Long userId){

            if (userId == null) {
                throw new IllegalArgumentException("User ID cannot be null");
            }

            List<Project> userProjects = projectRepository.findByUserIdWithFullDetails(userId);

            if (userProjects == null || userProjects.isEmpty()) {
                return new ArrayList<>();
            }
            List<ProjectViewDto> projectDtos = userProjects.stream()
                    .map(this::mapEntityToProjectViewDto)
                    .collect(Collectors.toList());

            // Retourne la liste des DTOs mappés
            return projectDtos;

        }

        @Override
        @Transactional
        public void updateProject (Long projectId, ProjectUpdateDto projectUpdateDto){

            // 1. Validation des entrées de base
            if (projectId == null) {
                throw new IllegalArgumentException("Project ID cannot be null for update.");
            }
            if (projectUpdateDto == null) {
                throw new IllegalArgumentException("Project update data cannot be null.");
            }

            // 2. Récupérer l'entité existante
            Project projectToUpdate = projectRepository.findById(projectId)
                    .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + projectId + " for update."));

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
                Set<ProjectPurpose> newPurposes = new HashSet<>(projectUpdateDto.getProjectPurposes());
                // Vérifier si le set a changé avant de remplacer
                if (!Objects.equals(newPurposes, projectToUpdate.getProjectPurposes())) {
                    projectToUpdate.setProjectPurposes(newPurposes);
                    changed = true;
                }
            }

            // Répétez pour les autres collections (MusicalGendersPreDefined, MusicalGenderAddedSet, ProjectTags) si elles sont dans ProjectUpdateDto
            if (projectUpdateDto.getProjectMusicalGendersPreDefined() != null) {
                Set<ProjectMusicalGenderPreDefined> newGenders = new HashSet<>(projectUpdateDto.getProjectMusicalGendersPreDefined());
                if (!Objects.equals(newGenders, projectToUpdate.getProjectMusicalGendersPreDefined())) {
                    projectToUpdate.setProjectMusicalGendersPreDefined(newGenders);
                    changed = true;
                }
            }


            if (changed) {
                projectRepository.save(projectToUpdate);
            }
            // Si rien n'a changé, on ne fait rien (pas d'écriture inutile en BDD).
        }

        @Override
        @Transactional
        public void archiveProject ( long projectId){
            // 1. Validation de base de l'ID
            if (projectId <= 0) {
                throw new IllegalArgumentException("Project ID must be positive for archiving.");
            }

            // 2. Récupérer l'entité existante
            Project projectToArchive = projectRepository.findById(projectId)
                    .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + projectId + " for archiving."));

            // TODO: Ajouter une vérification des permissions ici si nécessaire
            // (Ex: L'utilisateur actuel a-t-il le droit d'archiver CE projet ?)
            // checkPermissionToArchive(currentUser, projectToArchive);

            // 3. Vérifier si le projet est déjà archivé
            //    (Assurez-vous que votre entité Project a une méthode isArchived() ou getArchived())
            if (projectToArchive.getArchived()) {
                throw new IllegalStateException("Project with ID: " + projectId + " is already archived.");
            }

            projectToArchive.setArchived(true);
            projectRepository.save(projectToArchive);


        }

        @Override
        @Transactional
        public void deleteProject ( long projectId){

            if (!projectRepository.existsById(projectId)) {
                throw new EntityNotFoundException("Project not found with ID: " + projectId + " for deletion.");
            }
            projectRepository.deleteById(projectId);

        }


        private ProjectSummaryDto mapEntityToProjectSummaryDto (Project project){

            if (project == null) {
                return null;
            }

            ProjectSummaryDto dto = new ProjectSummaryDto();

            dto.setId(project.getId());
            dto.setTitle(project.getTitle());
            dto.setProjectStatus(project.getProjectStatus());
            dto.setLastUpdateDate(project.getLastUpdateDate()); // Assurez-vous que le champ s'appelle bien ainsi

            // Construire la liste simplifiée des genres musicaux
            List<String> genreNames = new ArrayList<>();
            Set<ProjectMusicalGenderPreDefined> predefinedGenres = project.getProjectMusicalGendersPreDefined();
            if (predefinedGenres != null) {
                predefinedGenres.forEach(genreEnum -> genreNames.add(genreEnum.name())); // Ou une autre représentation textuelle si l'enum a une méthode dédiée
            }

            dto.setMusicalGenres(genreNames);

            return dto;
        }
        private ProjectCreateDto mapEntityToCreateDto (Project project){
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
            dto.setCreationDate(project.getCreatedDate());

            return dto;
        }
        private ProjectViewDto mapEntityToProjectViewDto (Project project){
            System.out.println("Attempting to map Project ID: " + (project != null ? project.getId() : "NULL PROJECT")); // Log d'entrée
            if (project == null) {
                System.err.println("mapEntityToProjectViewDto received a null project object!");
                return null; // Ou lancer une exception si ce n'est pas attendu
            }

            ProjectViewDto dto = new ProjectViewDto();
            dto.setId(project.getId());
            dto.setTitle(project.getTitle());


             dto.setDescription(project.getDescription());
             dto.setIllustration(project.getIllustration());
             dto.setProjectStatus(project.getProjectStatus());
             dto.setProjectType(project.getProjectType());
             dto.setProjectCommercialStatus(project.getProjectCommercialStatus());
             dto.setCompositionsTotal(project.getCompositions() != null ? project.getCompositions().size() : 0); // ATTENTION ICI !
             dto.setProjectPurposes(project.getProjectPurposes() != null ? new HashSet<>(project.getProjectPurposes()) : new HashSet<>()); // ATTENTION ICI !
             dto.setProjectMusicalGendersPreDefined(project.getProjectMusicalGendersPreDefined() != null ? new HashSet<>(project.getProjectMusicalGendersPreDefined()) : new HashSet<>()); // ATTENTION ICI !
             dto.setCreationDate(project.getCreatedDate());
             dto.setUpdateDate(project.getLastUpdateDate());

            System.out.println("Finished basic mapping for Project ID: " + project.getId()); // Log de sortie
            return dto;
        }


        ///  SETTERS - INVERSION DE DEPENDANCE ///

        @Autowired
        public void setProjectRepository (ProjectRepository projectRepository){
            this.projectRepository = projectRepository;
        }

        @Autowired
        public void setUserRepository (UserRepository userRepository){
            this.userRepository = userRepository;
        }
    }

