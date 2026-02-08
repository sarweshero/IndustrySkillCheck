package com.college.SkillCheck.service;

import com.college.SkillCheck.dto.UserRequestDTO;
import com.college.SkillCheck.exception.BadRequestException;
import com.college.SkillCheck.exception.ResourceNotFoundException;
import com.college.SkillCheck.model.User;
import com.college.SkillCheck.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setDept(dto.getDept());
        user.setBio(dto.getBio());
        user.setYearOfStudy(dto.getYearOfStudy());
        user.setGraduationYear(dto.getGraduationYear());
        user.setCgpa(dto.getCgpa());
        user.setGithubUrl(dto.getGithubUrl());
        user.setLinkedinUrl(dto.getLinkedinUrl());
        user.setPortfolioUrl(dto.getPortfolioUrl());
        user.setResumeUrl(dto.getResumeUrl());
        user.setSkill(dto.getSkill());
        user.setProfilePictureUrl(dto.getProfilePictureUrl());
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User updateUser(Long id, UserRequestDTO dto) {
        User user = getUserById(id);
        if (!user.getEmail().equalsIgnoreCase(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setBio(dto.getBio());
        user.setDept(dto.getDept());
        user.setYearOfStudy(dto.getYearOfStudy());
        user.setGraduationYear(dto.getGraduationYear());
        user.setCgpa(dto.getCgpa());
        user.setGithubUrl(dto.getGithubUrl());
        user.setLinkedinUrl(dto.getLinkedinUrl());
        user.setPortfolioUrl(dto.getPortfolioUrl());
        user.setResumeUrl(dto.getResumeUrl());
        user.setSkill(dto.getSkill());
        user.setProfilePictureUrl(dto.getProfilePictureUrl());
        return userRepository.save(user);
    }

    public User deactivateUser(Long id) {
        User user = getUserById(id);
        user.setActive(false);
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
