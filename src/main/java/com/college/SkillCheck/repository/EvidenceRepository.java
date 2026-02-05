package com.college.SkillCheck.repository;

import com.college.SkillCheck.model.Evidence;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvidenceRepository extends JpaRepository<Evidence, Long> {
    List<Evidence> findByStudent_Id(Long studentId);
}
