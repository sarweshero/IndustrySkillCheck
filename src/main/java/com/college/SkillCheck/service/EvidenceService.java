package com.college.SkillCheck.service;

import com.college.SkillCheck.dto.EvidenceRequestDTO;
import com.college.SkillCheck.exception.BadRequestException;
import com.college.SkillCheck.exception.ResourceNotFoundException;
import com.college.SkillCheck.model.Evidence;
import com.college.SkillCheck.model.EvidenceSkill;
import com.college.SkillCheck.model.Skill;
import com.college.SkillCheck.model.User;
import com.college.SkillCheck.repository.EvidenceRepository;
import com.college.SkillCheck.repository.EvidenceSkillRepository;
import com.college.SkillCheck.repository.SkillRepository;
import com.college.SkillCheck.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EvidenceService {

    private final EvidenceRepository evidenceRepository;
    private final EvidenceSkillRepository evidenceSkillRepository;
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;

    public EvidenceService(EvidenceRepository evidenceRepository,
                           EvidenceSkillRepository evidenceSkillRepository,
                           SkillRepository skillRepository,
                           UserRepository userRepository) {
        this.evidenceRepository = evidenceRepository;
        this.evidenceSkillRepository = evidenceSkillRepository;
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Evidence submitEvidence(EvidenceRequestDTO dto) {
        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return submitEvidenceForStudent(dto, student);
    }

    @Transactional
    public Evidence submitEvidenceForStudent(EvidenceRequestDTO dto, Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return submitEvidenceForStudent(dto, student);
    }

    private Evidence submitEvidenceForStudent(EvidenceRequestDTO dto, User student) {
        if (!"STUDENT".equalsIgnoreCase(student.getRole())) {
            throw new BadRequestException("Evidence can only be submitted for students");
        }
        if (dto.getSkills() == null || dto.getSkills().isEmpty()) {
            throw new BadRequestException("At least one skill is required");
        }
        Evidence evidence = new Evidence();
        evidence.setStudent(student);
        evidence.setType(dto.getType());
        evidence.setReference(dto.getReference());
        evidence.setSubmittedAt(LocalDateTime.now());
        Evidence savedEvidence = evidenceRepository.save(evidence);

        List<EvidenceSkill> evidenceSkills = new ArrayList<>();
        for (EvidenceRequestDTO.EvidenceSkillRequest skillRequest : dto.getSkills()) {
            if (skillRequest.getSkillId() == null || skillRequest.getWeight() == null) {
                throw new BadRequestException("Skill id and weight are required");
            }
            Skill skill = skillRepository.findById(skillRequest.getSkillId())
                    .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));
            EvidenceSkill evidenceSkill = new EvidenceSkill();
            evidenceSkill.setEvidence(savedEvidence);
            evidenceSkill.setSkill(skill);
            evidenceSkill.setWeight(skillRequest.getWeight());
            evidenceSkills.add(evidenceSkill);
        }
        evidenceSkillRepository.saveAll(evidenceSkills);
        return savedEvidence;
    }

    public Evidence getEvidenceById(Long id) {
        return evidenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evidence not found"));
    }

    public List<Evidence> getEvidenceByStudent(Long studentId) {
        return evidenceRepository.findByStudent_Id(studentId);
    }

    @Transactional
    public void deleteEvidence(Long id) {
        Evidence evidence = getEvidenceById(id);
        evidenceSkillRepository.deleteByEvidence_Id(evidence.getId());
        evidenceRepository.delete(evidence);
    }
}
