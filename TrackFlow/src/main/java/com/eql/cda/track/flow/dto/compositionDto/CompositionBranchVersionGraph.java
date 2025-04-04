package com.eql.cda.track.flow.dto.compositionDto;

import com.eql.cda.track.flow.dto.branchDto.GraphBranchDto;
import com.eql.cda.track.flow.dto.versionDto.GraphVersionDto;

import java.util.List;

public class CompositionBranchVersionGraph {

    private Long compositionId;
    private String compositionTitle;
    private List<GraphBranchDto> branches; // Contient { id, name }
    private List<GraphVersionDto> versions; // Contient { id, name, date, author, branchId, parentVersionId (si applicable) }
}
