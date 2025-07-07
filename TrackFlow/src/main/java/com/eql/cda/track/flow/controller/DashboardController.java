package com.eql.cda.track.flow.controller;

import com.eql.cda.track.flow.dto.compositionDto.CompositionSummaryDto;
import com.eql.cda.track.flow.dto.projectDto.ProjectSummaryDto;
import com.eql.cda.track.flow.service.CompositionService;
import com.eql.cda.track.flow.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles REST requests for data aggregations displayed on the user's dashboard.
 * This controller provides endpoints for widgets like "Recent Projects" or "Recent Compositions",
 * which are not tied to a specific resource's CRUD lifecycle but rather provide a snapshot for a user view.
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final ProjectService projectService;
    private final CompositionService compositionService;

    /**
     * Constructs the controller with its required service dependencies.
     * @param projectService The service for project-related data.
     * @param compositionService The service for composition-related data.
     */
    @Autowired
    public DashboardController(ProjectService projectService, CompositionService compositionService) {
        this.projectService = projectService;
        this.compositionService = compositionService;
    }

    /**
     * Retrieves a paginated list of the most recent projects for the currently authenticated user.
     * The user's identity is resolved from the security context by the service layer.
     *
     * @param pageable Pagination and sorting information, configured by default to return the 5 newest projects.
     * @return A {@link ResponseEntity} containing a {@link Page} of {@link ProjectSummaryDto}.
     */
    @GetMapping("/recent-projects")
    public ResponseEntity<Page<ProjectSummaryDto>> getRecentProjects(
            @PageableDefault(size = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProjectSummaryDto> recentProjects = projectService.findRecentProjectsForCurrentUser(pageable);
        return ResponseEntity.ok(recentProjects);
    }

    /**
     * Retrieves a paginated list of the most recently updated compositions for the currently authenticated user.
     * The user's identity is resolved from the security context by the service layer.
     *
     * @param pageable Pagination and sorting information, configured by default to return the 5 most recently updated compositions.
     * @return A {@link ResponseEntity} containing a {@link Page} of {@link CompositionSummaryDto}.
     */
    @GetMapping("/recent-compositions")
    public ResponseEntity<Page<CompositionSummaryDto>> getRecentCompositions(
            @PageableDefault(size = 5, sort = "lastUpdateDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CompositionSummaryDto> recentCompositions = compositionService.findRecentCompositionsForCurrentUser(pageable);
        return ResponseEntity.ok(recentCompositions);
    }
}