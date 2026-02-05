package com.college.SkillCheck.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.college.SkillCheck.dto.EvidenceRequestDTO;
import com.college.SkillCheck.exception.BadRequestException;
import com.college.SkillCheck.model.Evidence;
import com.college.SkillCheck.model.Skill;
import com.college.SkillCheck.model.User;
import com.college.SkillCheck.repository.EvidenceRepository;
import com.college.SkillCheck.repository.EvidenceSkillRepository;
import com.college.SkillCheck.repository.SkillRepository;
import com.college.SkillCheck.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EvidenceServiceTest {

    @Mock
    private EvidenceRepository evidenceRepository;

    @Mock
    private EvidenceSkillRepository evidenceSkillRepository;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EvidenceService evidenceService;

    @Test
    void submitEvidenceCreatesEvidenceSkills() {
        User student = new User();
        student.setId(1L);
        student.setRole("STUDENT");

        Skill skillOne = new Skill();
        skillOne.setId(10L);
        Skill skillTwo = new Skill();
        skillTwo.setId(20L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(student));
        when(skillRepository.findById(10L)).thenReturn(Optional.of(skillOne));
        when(skillRepository.findById(20L)).thenReturn(Optional.of(skillTwo));
        when(evidenceRepository.save(any(Evidence.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(evidenceSkillRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        EvidenceRequestDTO dto = new EvidenceRequestDTO();
        dto.setStudentId(1L);
        dto.setType("PROJECT");
        dto.setReference("https://example.com");

        EvidenceRequestDTO.EvidenceSkillRequest skillRequestOne = new EvidenceRequestDTO.EvidenceSkillRequest();
        skillRequestOne.setSkillId(10L);
        skillRequestOne.setWeight(3);

        EvidenceRequestDTO.EvidenceSkillRequest skillRequestTwo = new EvidenceRequestDTO.EvidenceSkillRequest();
        skillRequestTwo.setSkillId(20L);
        skillRequestTwo.setWeight(2);

        dto.setSkills(List.of(skillRequestOne, skillRequestTwo));

        Evidence evidence = evidenceService.submitEvidence(dto);

        assertNotNull(evidence);
        verify(evidenceSkillRepository).saveAll(argThat(list ->
                StreamSupport.stream(list.spliterator(), false).count() == 2));
    }

    @Test
    void submitEvidenceRejectsNonStudent() {
        User staff = new User();
        staff.setId(2L);
        staff.setRole("ADMIN");

        when(userRepository.findById(2L)).thenReturn(Optional.of(staff));

        EvidenceRequestDTO dto = new EvidenceRequestDTO();
        dto.setStudentId(2L);
        dto.setType("PROJECT");
        dto.setReference("https://example.com");

        EvidenceRequestDTO.EvidenceSkillRequest skillRequest = new EvidenceRequestDTO.EvidenceSkillRequest();
        skillRequest.setSkillId(10L);
        skillRequest.setWeight(1);
        dto.setSkills(List.of(skillRequest));

        assertThrows(BadRequestException.class, () -> evidenceService.submitEvidence(dto));
    }
}
