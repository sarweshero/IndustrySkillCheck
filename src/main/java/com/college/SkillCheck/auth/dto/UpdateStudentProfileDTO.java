package com.college.SkillCheck.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UpdateStudentProfileDTO {

    @Size(max = 100)
    private String name;

    @Email
    @Size(max = 150)
    private String email;

    @Size(min = 6, max = 100)
    private String password;

    public UpdateStudentProfileDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
