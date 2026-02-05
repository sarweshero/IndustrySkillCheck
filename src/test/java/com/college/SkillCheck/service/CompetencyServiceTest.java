package com.college.SkillCheck.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.college.SkillCheck.dto.CompetencyResponseDTO;
import com.college.SkillCheck.model.Competency;
import com.college.SkillCheck.model.EvidenceSkill;
import com.college.SkillCheck.model.Skill;
import com.college.SkillCheck.model.User;
import com.college.SkillCheck.repository.CompetencyRepository;
import com.college.SkillCheck.repository.EvidenceSkillRepository;
import com.college.SkillCheck.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CompetencyServiceTest {

    @Mock
    private CompetencyRepository competencyRepository;

    @Mock
    private EvidenceSkillRepository evidenceSkillRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CompetencyService competencyService;

    @Test
    void evaluateCompetenciesAggregatesScores() {
        User student = new User();
        student.setId(1L);

        Skill skill = new Skill();
        skill.setId(5L);
        skill.setName("Java");

        EvidenceSkill evidenceSkillOne = new EvidenceSkill();
        evidenceSkillOne.setSkill(skill);
        evidenceSkillOne.setWeight(2);

        EvidenceSkill evidenceSkillTwo = new EvidenceSkill();
        evidenceSkillTwo.setSkill(skill);
        evidenceSkillTwo.setWeight(4);

        when(userRepository.findById(1L)).thenReturn(Optional.of(student));
        when(evidenceSkillRepository.findByEvidence_Student_Id(1L))
                .thenReturn(List.of(evidenceSkillOne, evidenceSkillTwo));
        when(competencyRepository.findByStudentIdAndSkillId(1L, 5L)).thenReturn(Optional.empty());
        when(competencyRepository.save(any(Competency.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<CompetencyResponseDTO> responses = competencyService.evaluateCompetencies(1L);

        assertEquals(1, responses.size());
        assertEquals(6, responses.get(0).getScore());
        assertEquals("Java", responses.get(0).getSkillName());
    }

    @Test
    void evaluateCompetenciesReturnsEmptyWhenNoEvidence() {
        User student = new User();
        student.setId(2L);

        when(userRepository.findById(2L)).thenReturn(Optional.of(student));
        when(evidenceSkillRepository.findByEvidence_Student_Id(2L)).thenReturn(List.of());

        List<CompetencyResponseDTO> responses = competencyService.evaluateCompetencies(2L);

        assertTrue(responses.isEmpty());
    }
}
