package com.college.SkillCheck.service;

import com.college.SkillCheck.dto.CompetencyResponseDTO;
import com.college.SkillCheck.exception.ResourceNotFoundException;
import com.college.SkillCheck.model.Competency;
import com.college.SkillCheck.model.EvidenceSkill;
import com.college.SkillCheck.model.Skill;
import com.college.SkillCheck.model.User;
import com.college.SkillCheck.repository.CompetencyRepository;
import com.college.SkillCheck.repository.EvidenceSkillRepository;
import com.college.SkillCheck.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompetencyService {

    private final CompetencyRepository competencyRepository;
    private final EvidenceSkillRepository evidenceSkillRepository;
    private final UserRepository userRepository;

    public CompetencyService(CompetencyRepository competencyRepository,
                             EvidenceSkillRepository evidenceSkillRepository,
                             UserRepository userRepository) {
        this.competencyRepository = competencyRepository;
        this.evidenceSkillRepository = evidenceSkillRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<CompetencyResponseDTO> evaluateCompetencies(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        List<EvidenceSkill> evidenceSkills = evidenceSkillRepository.findByEvidence_Student_Id(studentId);
        Map<Skill, Integer> scoreMap = new HashMap<>();
        for (EvidenceSkill evidenceSkill : evidenceSkills) {
            Skill skill = evidenceSkill.getSkill();
            Integer current = scoreMap.getOrDefault(skill, 0);
            scoreMap.put(skill, current + evidenceSkill.getWeight());
        }

        List<CompetencyResponseDTO> responses = new ArrayList<>();
        for (Map.Entry<Skill, Integer> entry : scoreMap.entrySet()) {
            Skill skill = entry.getKey();
            Integer score = entry.getValue();
            responses.add(toResponse(saveCompetency(student, skill, score)));
        }
        return responses;
    }

    public CompetencyResponseDTO getCompetency(Long id) {
        Competency competency = competencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Competency not found"));
        return toResponse(competency);
    }

    public List<CompetencyResponseDTO> getCompetenciesByStudent(Long studentId) {
        List<Competency> competencies = competencyRepository.findByStudent_Id(studentId);
        List<CompetencyResponseDTO> responses = new ArrayList<>();
        for (Competency competency : competencies) {
            responses.add(toResponse(competency));
        }
        return responses;
    }

    public CompetencyResponseDTO updateCompetencyScore(Long id, Integer score) {
        Competency competency = competencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Competency not found"));
        competency.setScore(score);
        competency.setLastUpdated(LocalDateTime.now());
        return toResponse(competencyRepository.save(competency));
    }

    public void deleteCompetency(Long id) {
        Competency competency = competencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Competency not found"));
        competencyRepository.delete(competency);
    }

    private CompetencyResponseDTO toResponse(Competency competency) {
        return new CompetencyResponseDTO(
                competency.getId(),
                competency.getStudent().getId(),
                competency.getSkill().getId(),
                competency.getSkill().getName(),
                competency.getScore(),
                competency.getLastUpdated()
        );
    }

    private Competency saveCompetency(User student, Skill skill, Integer score) {
        Competency competency = competencyRepository.findByStudentIdAndSkillId(student.getId(), skill.getId())
                .orElse(null);
        if (competency == null) {
            Competency newCompetency = new Competency();
            newCompetency.setStudent(student);
            newCompetency.setSkill(skill);
            newCompetency.setScore(score);
            newCompetency.setLastUpdated(LocalDateTime.now());
            try {
                return competencyRepository.save(newCompetency);
            } catch (DataIntegrityViolationException ex) {
                competency = competencyRepository.findByStudentIdAndSkillId(student.getId(), skill.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Competency not found after conflict"));
            }
        }
        competency.setScore(score);
        competency.setLastUpdated(LocalDateTime.now());
        return competencyRepository.save(competency);
    }
}
