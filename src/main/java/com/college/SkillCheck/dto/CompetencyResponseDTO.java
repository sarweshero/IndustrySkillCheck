package com.college.SkillCheck.dto;

import java.time.LocalDateTime;

public class CompetencyResponseDTO {

    private Long id;
    private Long studentId;
    private Long skillId;
    private String skillName;
    private Integer score;
    private LocalDateTime lastUpdated;

    public CompetencyResponseDTO() {
    }

    public CompetencyResponseDTO(Long id, Long studentId, Long skillId, String skillName, Integer score,
                                 LocalDateTime lastUpdated) {
        this.id = id;
        this.studentId = studentId;
        this.skillId = skillId;
        this.skillName = skillName;
        this.score = score;
        this.lastUpdated = lastUpdated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
