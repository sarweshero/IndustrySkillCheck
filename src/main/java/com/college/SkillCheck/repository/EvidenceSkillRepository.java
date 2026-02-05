package com.college.SkillCheck.repository;

import com.college.SkillCheck.model.EvidenceSkill;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface EvidenceSkillRepository extends JpaRepository<EvidenceSkill, Long> {
    List<EvidenceSkill> findByEvidence_Student_Id(Long studentId);

    @Modifying
    void deleteByEvidence_Id(Long evidenceId);
}
