package com.eql.cda.track.flow.service;

import com.eql.cda.track.flow.entity.Branch;
import com.eql.cda.track.flow.entity.Version;

import java.util.Optional;

public interface VersionNamingService {

    String generateNextVersionName(Version parentVersion,Branch branch);
    String calculatePotentialNameForBranch(Long branchId);
    String generateFirstEverVersionName();

}
