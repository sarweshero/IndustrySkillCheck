package com.college.SkillCheck.service;

import com.college.SkillCheck.dto.SkillRequestDTO;
import com.college.SkillCheck.exception.BadRequestException;
import com.college.SkillCheck.exception.ResourceNotFoundException;
import com.college.SkillCheck.model.Skill;
import com.college.SkillCheck.repository.SkillRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill createSkill(SkillRequestDTO dto) {
        if (skillRepository.existsByName(dto.getName())) {
            throw new BadRequestException("Skill name already exists");
        }
        Skill skill = new Skill();
        skill.setName(dto.getName());
        skill.setDescription(dto.getDescription());
        skill.setActive(true);
        skill.setCreatedAt(LocalDateTime.now());
        return skillRepository.save(skill);
    }

    public Skill updateSkill(Long id, SkillRequestDTO dto) {
        Skill skill = getSkillById(id);
        if (!skill.getName().equalsIgnoreCase(dto.getName()) && skillRepository.existsByName(dto.getName())) {
            throw new BadRequestException("Skill name already exists");
        }
        skill.setName(dto.getName());
        skill.setDescription(dto.getDescription());
        return skillRepository.save(skill);
    }

    public Skill deactivateSkill(Long id) {
        Skill skill = getSkillById(id);
        skill.setActive(false);
        return skillRepository.save(skill);
    }

    public Skill getSkillById(Long id) {
        return skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));
    }

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }
}
