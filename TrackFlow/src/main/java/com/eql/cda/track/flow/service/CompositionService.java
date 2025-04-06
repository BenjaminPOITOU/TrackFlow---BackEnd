package com.eql.cda.track.flow.service;

import com.eql.cda.track.flow.dto.compositionDto.CompositionCreateDto;
import com.eql.cda.track.flow.dto.compositionDto.CompositionSummaryDto;
import com.eql.cda.track.flow.dto.compositionDto.CompositionUpdateDto;
import com.eql.cda.track.flow.dto.compositionDto.CompositionViewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface CompositionService {

    CompositionViewDto createComposition(Long projectId, CompositionCreateDto compositionCreateDto);
    Page<CompositionSummaryDto> findRecentCompositionsForUser(String username, Pageable pageable);
    List<CompositionSummaryDto> getAllCompositions(Long projectId);
    CompositionViewDto getCurrentComposition(Long compositionId);
    CompositionViewDto updateComposition(Long compositionId, CompositionUpdateDto compositionUpdateDto);
    void deleteComposition(Long compositionId);
}
