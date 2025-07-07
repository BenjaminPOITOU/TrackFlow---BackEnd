

import com.eql.cda.track.flow.dto.versionDto.VersionCreateDto;
import com.eql.cda.track.flow.dto.versionDto.VersionSummaryDto;
import com.eql.cda.track.flow.dto.versionDto.VersionUpdateDto;
import com.eql.cda.track.flow.dto.versionDto.VersionViewDto;
import com.eql.cda.track.flow.service.VersionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VersionServiceTest {

    @Mock
    private VersionService versionService;

    private Long projectId;
    private Long compositionId;
    private Long branchId;
    private Long versionId;
    private VersionCreateDto versionCreateDto;
    private VersionUpdateDto versionUpdateDto;
    private VersionViewDto versionViewDto;
    private VersionSummaryDto versionSummaryDto;

    @BeforeEach
    void setUp() {

        projectId = 1L;
        compositionId = 1L;
        branchId = 1L;
        versionId = 1L;


        versionCreateDto = new VersionCreateDto();
        versionCreateDto.setVersionName("Version 1.0");
        versionCreateDto.setAudioFileUrl("https://example.com/audio.mp3");
        versionCreateDto.setDurationSeconds(180);
        versionCreateDto.setBpm("120");
        versionCreateDto.setKey("C Major");


        versionUpdateDto = new VersionUpdateDto();
        versionUpdateDto.setName("Version 1.1");
        versionUpdateDto.setBpm("125");
        versionUpdateDto.setKey("G Major");


        versionViewDto = new VersionViewDto();
        versionViewDto.setId(versionId);
        versionViewDto.setName("Version 1.0");
        versionViewDto.setAuthor("John Doe");
        versionViewDto.setAudioFileUrl("https://example.com/audio.mp3");
        versionViewDto.setDurationSeconds(180);
        versionViewDto.setBpm("120");
        versionViewDto.setKey("C Major");
        versionViewDto.setCreatedDate(Instant.now());
        versionViewDto.setBranchId(branchId);
        versionViewDto.setBranchName("Main Branch");
        versionViewDto.setBranchDescription("Main development branch");
        versionViewDto.setParentVersionId(null);
        versionViewDto.setInstruments(new HashSet<>());
        versionViewDto.setAnnotations(new ArrayList<>());


        versionSummaryDto = new VersionSummaryDto();
        versionSummaryDto.setId(versionId);
        versionSummaryDto.setName("Version 1.0");
        versionSummaryDto.setBranchName("Main Branch");
        versionSummaryDto.setCreatedDate(Instant.now());
    }

    @Test
    void testCreateVersion_Success() {

        when(versionService.createVersion(anyLong(), anyLong(), anyLong(), any(VersionCreateDto.class)))
                .thenReturn(versionViewDto);


        VersionViewDto result = versionService.createVersion(projectId, compositionId, branchId, versionCreateDto);


        assertNotNull(result);
        assertEquals(versionViewDto.getId(), result.getId());
        assertEquals(versionViewDto.getName(), result.getName());
        assertEquals(versionViewDto.getAuthor(), result.getAuthor());
        assertEquals(versionViewDto.getAudioFileUrl(), result.getAudioFileUrl());
        assertEquals(versionViewDto.getDurationSeconds(), result.getDurationSeconds());
        assertEquals(versionViewDto.getBpm(), result.getBpm());
        assertEquals(versionViewDto.getKey(), result.getKey());

        verify(versionService, times(1)).createVersion(projectId, compositionId, branchId, versionCreateDto);
    }

    @Test
    void testCreateVersion_InvalidData() {

        VersionCreateDto invalidDto = new VersionCreateDto();


        when(versionService.createVersion(anyLong(), anyLong(), anyLong(), any(VersionCreateDto.class)))
                .thenThrow(new IllegalArgumentException("Invalid version data"));


        assertThrows(IllegalArgumentException.class, () -> {
            versionService.createVersion(projectId, compositionId, branchId, invalidDto);
        });

        verify(versionService, times(1)).createVersion(projectId, compositionId, branchId, invalidDto);
    }

    @Test
    void testCreateVersion_BranchNotFound() {

        Long nonExistentBranchId = 999L;

        when(versionService.createVersion(anyLong(), anyLong(), eq(nonExistentBranchId), any(VersionCreateDto.class)))
                .thenThrow(new RuntimeException("Branch not found"));


        assertThrows(RuntimeException.class, () -> {
            versionService.createVersion(projectId, compositionId, nonExistentBranchId, versionCreateDto);
        });

        verify(versionService, times(1)).createVersion(projectId, compositionId, nonExistentBranchId, versionCreateDto);
    }

    @Test
    void testGetVersionsByBranch_Success() {

        List<VersionSummaryDto> expectedVersions = Arrays.asList(
                versionSummaryDto,
                createVersionSummaryDto(2L, "Version 1.1"),
                createVersionSummaryDto(3L, "Version 2.0")
        );

        when(versionService.getVersionsByBranch(anyLong(), anyLong(), anyLong()))
                .thenReturn(expectedVersions);


        List<VersionSummaryDto> result = versionService.getVersionsByBranch(projectId, compositionId, branchId);


        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(expectedVersions.get(0).getName(), result.get(0).getName());
        assertEquals(expectedVersions.get(1).getName(), result.get(1).getName());
        assertEquals(expectedVersions.get(2).getName(), result.get(2).getName());
        assertEquals(expectedVersions.get(0).getBranchName(), result.get(0).getBranchName());
        assertEquals(expectedVersions.get(1).getBranchName(), result.get(1).getBranchName());
        assertEquals(expectedVersions.get(2).getBranchName(), result.get(2).getBranchName());

        verify(versionService, times(1)).getVersionsByBranch(projectId, compositionId, branchId);
    }

    @Test
    void testGetVersionsByBranch_EmptyBranch() {

        when(versionService.getVersionsByBranch(anyLong(), anyLong(), anyLong()))
                .thenReturn(Arrays.asList());


        List<VersionSummaryDto> result = versionService.getVersionsByBranch(projectId, compositionId, branchId);


        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(versionService, times(1)).getVersionsByBranch(projectId, compositionId, branchId);
    }

    @Test
    void testGetVersionById_Success() {

        when(versionService.getVersionById(anyLong(), anyLong(), anyLong(), anyLong()))
                .thenReturn(versionViewDto);


        VersionViewDto result = versionService.getVersionById(projectId, compositionId, branchId, versionId);


        assertNotNull(result);
        assertEquals(versionViewDto.getId(), result.getId());
        assertEquals(versionViewDto.getName(), result.getName());
        assertEquals(versionViewDto.getAuthor(), result.getAuthor());
        assertEquals(versionViewDto.getBranchName(), result.getBranchName());
        assertEquals(versionViewDto.getAudioFileUrl(), result.getAudioFileUrl());

        verify(versionService, times(1)).getVersionById(projectId, compositionId, branchId, versionId);
    }

    @Test
    void testGetVersionById_NotFound() {

        Long nonExistentVersionId = 999L;

        when(versionService.getVersionById(anyLong(), anyLong(), anyLong(), eq(nonExistentVersionId)))
                .thenThrow(new RuntimeException("Version not found"));


        assertThrows(RuntimeException.class, () -> {
            versionService.getVersionById(projectId, compositionId, branchId, nonExistentVersionId);
        });

        verify(versionService, times(1)).getVersionById(projectId, compositionId, branchId, nonExistentVersionId);
    }

    @Test
    void testGetLatestVersionByBranch_Success() {

        when(versionService.getLatestVersionByBranch(anyLong(), anyLong(), anyLong()))
                .thenReturn(Optional.of(versionViewDto));


        Optional<VersionViewDto> result = versionService.getLatestVersionByBranch(projectId, compositionId, branchId);


        assertTrue(result.isPresent());
        assertEquals(versionViewDto.getId(), result.get().getId());
        assertEquals(versionViewDto.getName(), result.get().getName());
        assertEquals(versionViewDto.getAuthor(), result.get().getAuthor());
        assertEquals(versionViewDto.getBranchName(), result.get().getBranchName());

        verify(versionService, times(1)).getLatestVersionByBranch(projectId, compositionId, branchId);
    }

    @Test
    void testGetLatestVersionByBranch_EmptyBranch() {

        when(versionService.getLatestVersionByBranch(anyLong(), anyLong(), anyLong()))
                .thenReturn(Optional.empty());


        Optional<VersionViewDto> result = versionService.getLatestVersionByBranch(projectId, compositionId, branchId);


        assertTrue(result.isEmpty());

        verify(versionService, times(1)).getLatestVersionByBranch(projectId, compositionId, branchId);
    }

    @Test
    void testUpdateVersion_Success() {

        VersionViewDto updatedVersion = new VersionViewDto();
        updatedVersion.setId(versionId);
        updatedVersion.setName("Version 1.1");
        updatedVersion.setAuthor("John Doe");
        updatedVersion.setAudioFileUrl("https://example.com/updated-audio.mp3");
        updatedVersion.setDurationSeconds(200);
        updatedVersion.setBpm("125");
        updatedVersion.setKey("G Major");
        updatedVersion.setCreatedDate(Instant.now());
        updatedVersion.setBranchId(branchId);
        updatedVersion.setBranchName("Main Branch");

        when(versionService.updateVersion(anyLong(), anyLong(), anyLong(), anyLong(), any(VersionUpdateDto.class)))
                .thenReturn(updatedVersion);


        VersionViewDto result = versionService.updateVersion(projectId, compositionId, branchId, versionId, versionUpdateDto);


        assertNotNull(result);
        assertEquals(updatedVersion.getId(), result.getId());
        assertEquals(updatedVersion.getName(), result.getName());
        assertEquals(updatedVersion.getAuthor(), result.getAuthor());
        assertEquals(updatedVersion.getAudioFileUrl(), result.getAudioFileUrl());
        assertEquals(updatedVersion.getDurationSeconds(), result.getDurationSeconds());
        assertEquals(updatedVersion.getBpm(), result.getBpm());
        assertEquals(updatedVersion.getKey(), result.getKey());

        verify(versionService, times(1)).updateVersion(projectId, compositionId, branchId, versionId, versionUpdateDto);
    }

    @Test
    void testUpdateVersion_NotFound() {

        Long nonExistentVersionId = 999L;

        when(versionService.updateVersion(anyLong(), anyLong(), anyLong(), eq(nonExistentVersionId), any(VersionUpdateDto.class)))
                .thenThrow(new RuntimeException("Version not found"));


        assertThrows(RuntimeException.class, () -> {
            versionService.updateVersion(projectId, compositionId, branchId, nonExistentVersionId, versionUpdateDto);
        });

        verify(versionService, times(1)).updateVersion(projectId, compositionId, branchId, nonExistentVersionId, versionUpdateDto);
    }

    @Test
    void testDeleteVersion_Success() {

        doNothing().when(versionService).deleteVersion(anyLong(), anyLong(), anyLong(), anyLong());


        versionService.deleteVersion(projectId, compositionId, branchId, versionId);


        verify(versionService, times(1)).deleteVersion(projectId, compositionId, branchId, versionId);
    }

    @Test
    void testDeleteVersion_NotFound() {

        Long nonExistentVersionId = 999L;

        doThrow(new RuntimeException("Version not found"))
                .when(versionService).deleteVersion(anyLong(), anyLong(), anyLong(), eq(nonExistentVersionId));

        assertThrows(RuntimeException.class, () -> {
            versionService.deleteVersion(projectId, compositionId, branchId, nonExistentVersionId);
        });

        verify(versionService, times(1)).deleteVersion(projectId, compositionId, branchId, nonExistentVersionId);
    }

    @Test
    void testDeleteVersion_WithInvalidIds() {

        Long invalidProjectId = -1L;

        doThrow(new IllegalArgumentException("Invalid project ID"))
                .when(versionService).deleteVersion(eq(invalidProjectId), anyLong(), anyLong(), anyLong());


        assertThrows(IllegalArgumentException.class, () -> {
            versionService.deleteVersion(invalidProjectId, compositionId, branchId, versionId);
        });

        verify(versionService, times(1)).deleteVersion(invalidProjectId, compositionId, branchId, versionId);
    }


    private VersionSummaryDto createVersionSummaryDto(Long id, String name) {
        VersionSummaryDto dto = new VersionSummaryDto();
        dto.setId(id);
        dto.setName(name);
        dto.setBranchName("Main Branch");
        dto.setCreatedDate(Instant.now());
        return dto;
    }

    @Test
    void testCreateVersion_WithInstruments() {
        VersionCreateDto createDtoWithInstruments = new VersionCreateDto();
        createDtoWithInstruments.setVersionName("Version with Instruments");
        createDtoWithInstruments.setAudioFileUrl("https://example.com/audio-with-instruments.mp3");
        createDtoWithInstruments.setDurationSeconds(240);
        createDtoWithInstruments.setBpm("130");
        createDtoWithInstruments.setKey("D Minor");

        VersionViewDto versionWithInstruments = new VersionViewDto();
        versionWithInstruments.setId(2L);
        versionWithInstruments.setName("Version with Instruments");
        versionWithInstruments.setAuthor("Jane Doe");
        versionWithInstruments.setInstruments(new HashSet<>());
        versionWithInstruments.setAnnotations(new ArrayList<>());

        when(versionService.createVersion(anyLong(), anyLong(), anyLong(), any(VersionCreateDto.class)))
                .thenReturn(versionWithInstruments);


        VersionViewDto result = versionService.createVersion(projectId, compositionId, branchId, createDtoWithInstruments);


        assertNotNull(result);
        assertEquals(versionWithInstruments.getId(), result.getId());
        assertEquals(versionWithInstruments.getName(), result.getName());
        assertEquals(versionWithInstruments.getAuthor(), result.getAuthor());
        assertNotNull(result.getInstruments());
        assertNotNull(result.getAnnotations());

        verify(versionService, times(1)).createVersion(projectId, compositionId, branchId, createDtoWithInstruments);
    }

    @Test
    void testGetVersionById_WithParentVersion() {

        VersionViewDto versionWithParent = new VersionViewDto();
        versionWithParent.setId(versionId);
        versionWithParent.setName("Child Version");
        versionWithParent.setAuthor("John Doe");
        versionWithParent.setParentVersionId(5L);
        versionWithParent.setBranchId(branchId);
        versionWithParent.setBranchName("Feature Branch");
        versionWithParent.setBranchDescription("Feature development branch");

        when(versionService.getVersionById(anyLong(), anyLong(), anyLong(), anyLong()))
                .thenReturn(versionWithParent);


        VersionViewDto result = versionService.getVersionById(projectId, compositionId, branchId, versionId);


        assertNotNull(result);
        assertEquals(versionWithParent.getId(), result.getId());
        assertEquals(versionWithParent.getName(), result.getName());
        assertEquals(versionWithParent.getParentVersionId(), result.getParentVersionId());
        assertEquals(versionWithParent.getBranchName(), result.getBranchName());
        assertEquals(versionWithParent.getBranchDescription(), result.getBranchDescription());

        verify(versionService, times(1)).getVersionById(projectId, compositionId, branchId, versionId);
    }
}