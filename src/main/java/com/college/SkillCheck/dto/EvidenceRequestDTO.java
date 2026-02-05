package com.college.SkillCheck.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public class EvidenceRequestDTO {

    @NotNull
    private Long studentId;

    @NotBlank
    @Size(max = 100)
    private String type;

    @NotBlank
    @Size(max = 255)
    private String reference;

    @NotNull
    @Size(min = 1)
    @Valid
    private List<EvidenceSkillRequest> skills;

    public EvidenceRequestDTO() {
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<EvidenceSkillRequest> getSkills() {
        return skills;
    }

    public void setSkills(List<EvidenceSkillRequest> skills) {
        this.skills = skills;
    }

    public static class EvidenceSkillRequest {

        @NotNull
        private Long skillId;

        @NotNull
        private Integer weight;

        public EvidenceSkillRequest() {
        }

        public Long getSkillId() {
            return skillId;
        }

        public void setSkillId(Long skillId) {
            this.skillId = skillId;
        }

        public Integer getWeight() {
            return weight;
        }

        public void setWeight(Integer weight) {
            this.weight = weight;
        }
    }
}
