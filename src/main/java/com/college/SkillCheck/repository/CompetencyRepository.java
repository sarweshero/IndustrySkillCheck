package com.college.SkillCheck.repository;

import com.college.SkillCheck.model.Competency;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompetencyRepository extends JpaRepository<Competency, Long> {
    @Query("select c from Competency c where c.student.id = :studentId and c.skill.id = :skillId")
    Optional<Competency> findByStudentIdAndSkillId(@Param("studentId") Long studentId,
                                                   @Param("skillId") Long skillId);

    List<Competency> findByStudent_Id(Long studentId);
}
