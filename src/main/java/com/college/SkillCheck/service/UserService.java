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
