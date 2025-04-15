package com.eql.cda.track.flow.controller;/*package com.eql.cda.track.flow.controller;


import com.eql.cda.track.flow.dto.projectDto.ProjectSummaryDto;
// Importe les DTOs Summary pour Composition et Annotation
import com.eql.cda.track.flow.dto.compositionDto.CompositionSummaryDto; // Supposé
import com.eql.cda.track.flow.dto.annotationDto.AnnotationSummaryDto; // Supposé
import com.eql.cda.track.flow.service.ProjectService;
import com.eql.cda.track.flow.service.CompositionService; // Supposé
import com.eql.cda.track.flow.service.AnnotationService; // Supposé
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // Pour obtenir l'utilisateur connecté
import org.springframework.security.core.userdetails.UserDetails; // Interface standard
// Importe ton UserDetails personnalisé si tu en as un pour récupérer l'ID facilement
// import com.yourpackage.security.YourUserDetailsImplementation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;*/

import com.eql.cda.track.flow.service.AnnotationService;
import com.eql.cda.track.flow.service.CompositionService;
import com.eql.cda.track.flow.service.ProjectService;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard") // Chemin de base pour le dashboard
// @CrossOrigin(...) // Si tu n'utilises pas la config globale
public class DashboardController {

   // private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    private final ProjectService projectService;
    private final CompositionService compositionService; // Injecte le service Composition
    private final AnnotationService annotationService;   // Injecte le service Annotation

    @Autowired
    public DashboardController(ProjectService projectService,
                               CompositionService compositionService,
                               AnnotationService annotationService) {
        this.projectService = projectService;
        this.compositionService = compositionService;
        this.annotationService = annotationService;
    }

    }

    /**
     * Récupère les projets récents pour l'utilisateur authentifié.
     */

/*
    @GetMapping("/recent-projects")
    public ResponseEntity<Page<ProjectSummaryDto>> getRecentProjects(
            @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
            Authentication authentication // Injecte l'objet d'authentification
    ) {
        Long userId = getAuthenticatedUserId(authentication); // Fonction helper pour obtenir l'ID
        logger.info("GET /api/dashboard/recent-projects - Request for authenticated user ID {}, pageable: {}", userId, pageable);

        try {
            // Appeler une méthode de service qui prend l'ID utilisateur
            Page<ProjectSummaryDto> recentProjects = projectService.findRecentProjectsForAuthenticatedUser(userId, pageable);
            return ResponseEntity.ok(recentProjects);
        } catch (Exception e) {
            logger.error("Error retrieving recent projects for user ID {}: {}", userId, e.getMessage(), e);
            // Renvoyer une erreur serveur générique, ou plus spécifique si l'exception est connue
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving recent projects", e);
        }
    }

    /**
     * Récupère les compositions récentes pour l'utilisateur authentifié.
     */


/*
    @GetMapping("/recent-compositions")
    public ResponseEntity<Page<CompositionSummaryDto>> getRecentCompositions(
            @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable, // Adapte le tri si nécessaire
            Authentication authentication
    ) {
        Long userId = getAuthenticatedUserId(authentication);
        logger.info("GET /api/dashboard/recent-compositions - Request for authenticated user ID {}, pageable: {}", userId, pageable);

        try {
            // Appeler la méthode du service Composition
            Page<CompositionSummaryDto> recentCompositions = compositionService.findRecentCompositionsForAuthenticatedUser(userId, pageable);
            return ResponseEntity.ok(recentCompositions);
        } catch (Exception e) {
            logger.error("Error retrieving recent compositions for user ID {}: {}", userId, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving recent compositions", e);
        }
    }

    /**
     * Récupère les annotations récentes pour l'utilisateur authentifié.
     */


/*
    @GetMapping("/recent-annotations")
    public ResponseEntity<Page<AnnotationSummaryDto>> getRecentAnnotations(
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable, // Adapte le tri/taille
            Authentication authentication
    ) {
        Long userId = getAuthenticatedUserId(authentication);
        logger.info("GET /api/dashboard/recent-annotations - Request for authenticated user ID {}, pageable: {}", userId, pageable);

        try {
            // Appeler la méthode du service Annotation
            Page<AnnotationSummaryDto> recentAnnotations = annotationService.findRecentAnnotationsForAuthenticatedUser(userId, pageable);
            return ResponseEntity.ok(recentAnnotations);
        } catch (Exception e) {
            logger.error("Error retrieving recent annotations for user ID {}: {}", userId, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving recent annotations", e);
        }
    }


    // --- Helper Method pour obtenir l'ID de l'utilisateur authentifié ---
    private Long getAuthenticatedUserId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Attempted to access dashboard resource without authentication.");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            // Essayer d'obtenir l'ID. Ceci dépend de VOTRE implémentation de UserDetails.
            // Si votre UserDetails a une méthode getId(), utilisez-la.
            // Exemple si vous avez une classe personnalisée:
            // if (principal instanceof YourUserDetailsImplementation) {
            //    return ((YourUserDetailsImplementation) principal).getId();
            // }

            // Sinon, on pourrait devoir récupérer l'utilisateur depuis la BDD via le username
            String username = ((UserDetails) principal).getUsername();
            // User user = userRepository.findByLogin(username)... ; return user.getId();
            // C'est moins performant, il vaut mieux stocker l'ID dans UserDetails.
            // Pour l'exemple, on va supposer qu'on doit le rechercher (à optimiser)
            logger.warn("UserDetails does not directly expose ID. Falling back to username lookup for '{}'. Consider storing ID in UserDetails.", username);
            // Supposons qu'on a un userRepository injecté ou une méthode de service pour ça.
            // Adaptez ceci :
            // return userRepository.findIdByLogin(username).orElseThrow(() -> ...);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot determine user ID from UserDetails principal.");


        } else if (principal instanceof String) {
            // Parfois, le principal est juste le username String
            logger.warn("Principal is a String ('{}'). Need to fetch user ID from database.", principal);
            // return userRepository.findIdByLogin((String) principal).orElseThrow(() -> ...);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot determine user ID from String principal.");

        } else {
            logger.error("Unknown principal type: {}", principal.getClass().getName());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot determine authenticated user");
        }
    }
    /*
 */
