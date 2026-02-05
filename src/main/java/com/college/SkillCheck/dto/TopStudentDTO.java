package com.college.SkillCheck.dto;

public class TopStudentDTO {

    private Long studentId;
    private String studentName;
    private Integer totalScore;

    public TopStudentDTO() {
    }

    public TopStudentDTO(Long studentId, String studentName, Integer totalScore) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.totalScore = totalScore;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }
}
