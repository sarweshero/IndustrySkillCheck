package com.college.SkillCheck.dto;

public class SkillSummaryDTO {

    private Long skillId;
    private String skillName;
    private Integer totalScore;

    public SkillSummaryDTO() {
    }

    public SkillSummaryDTO(Long skillId, String skillName, Integer totalScore) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.totalScore = totalScore;
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

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }
}
