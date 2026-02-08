package com.college.SkillCheck.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SkillRequestDTO {

    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 2000)
    private String category;

    @NotBlank
    @Size(max = 255)
    private String description;

    public SkillRequestDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
