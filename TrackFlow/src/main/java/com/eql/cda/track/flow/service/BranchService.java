package com.eql.cda.track.flow.service;

import com.eql.cda.track.flow.dto.branchDto.BranchSummaryDto;
import com.eql.cda.track.flow.entity.Branch;
import com.eql.cda.track.flow.entity.Composition;

import java.util.List;

public interface BranchService {
    List<BranchSummaryDto> getAllBranches(Long compositionId);
    Branch findOrCreateBranch(Composition composition, Long branchId, String newBranchName, Long parentBranchId, String branchDescription);


}
