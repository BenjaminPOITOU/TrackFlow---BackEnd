package com.eql.cda.track.flow.controller;


import com.eql.cda.track.flow.dto.branchDto.BranchSummaryDto;
import com.eql.cda.track.flow.service.BranchService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/{compositionId}/branches")
public class BranchController {

    private BranchService branchService;

    @Autowired
    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }




    @GetMapping
    public ResponseEntity<List<BranchSummaryDto>> getAllBranchesFromCompositionId(
            @PathVariable Long compositionId) {
        try{
            List<BranchSummaryDto> allBranches = branchService.getAllBranches(compositionId);
            return ResponseEntity.ok(allBranches);
        }catch (EntityNotFoundException e) {

            return ResponseEntity.notFound().build();
            }
        }
    }


