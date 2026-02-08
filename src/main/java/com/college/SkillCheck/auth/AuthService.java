package com.college.SkillCheck.auth;

import com.college.SkillCheck.auth.dto.AdminCreateStudentDTO;
import com.college.SkillCheck.auth.dto.AdminUpdateStudentDTO;
import com.college.SkillCheck.auth.dto.LoginRequestDTO;
import com.college.SkillCheck.auth.dto.LoginResponseDTO;
import com.college.SkillCheck.auth.dto.RegisterStudentDTO;
import com.college.SkillCheck.auth.dto.UpdateStudentProfileDTO;
import com.college.SkillCheck.exception.BadRequestException;
import com.college.SkillCheck.exception.ResourceNotFoundException;
import com.college.SkillCheck.model.User;
import com.college.SkillCheck.repository.UserRepository;
import com.college.SkillCheck.security.CustomUserDetails;
import com.college.SkillCheck.security.JwtService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }
    

    public User registerStudent(RegisterStudentDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole("STUDENT");
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
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
        return userRepository.save(user);
    }

    public User adminCreateStudent(AdminCreateStudentDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole("STUDENT");
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
        user.setActive(Boolean.TRUE.equals(dto.getActive()));
        user.setCreatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return userRepository.save(user);
    }

    public User adminUpdateStudent(Long id, AdminUpdateStudentDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (dto.getEmail() != null) {
            String email = dto.getEmail().trim();
            if (email.isEmpty()) {
                throw new BadRequestException("Email cannot be blank");
            }
            if (!user.getEmail().equalsIgnoreCase(email) && userRepository.existsByEmail(email)) {
                throw new BadRequestException("Email already exists");
            }
            user.setEmail(email);
        }

        if (dto.getName() != null) {
            String name = dto.getName().trim();
            if (name.isEmpty()) {
                throw new BadRequestException("Name cannot be blank");
            }
            user.setName(name);
        }

        if (dto.getDept() != null) {
            String dept = dto.getDept().trim();
            user.setDept(dept);
        }

        if (dto.getBio() != null) {
            user.setBio(dto.getBio());
        }

        if (dto.getYearOfStudy() != null) {
            user.setYearOfStudy(dto.getYearOfStudy());
        }

        if (dto.getGraduationYear() != null) {
            user.setGraduationYear(dto.getGraduationYear());
        }

        if (dto.getCgpa() != null) {
            user.setCgpa(dto.getCgpa());
        }

        if (dto.getGithubUrl() != null) {
            String githubUrl = dto.getGithubUrl().trim();
            user.setGithubUrl(githubUrl.isEmpty() ? null : githubUrl);
        }

        if (dto.getLinkedinUrl() != null) {
            String linkedinUrl = dto.getLinkedinUrl().trim();
            user.setLinkedinUrl(linkedinUrl.isEmpty() ? null : linkedinUrl);
        }

        if (dto.getPortfolioUrl() != null) {
            String portfolioUrl = dto.getPortfolioUrl().trim();
            user.setPortfolioUrl(portfolioUrl.isEmpty() ? null : portfolioUrl);
        }

        if (dto.getResumeUrl() != null) {
            String resumeUrl = dto.getResumeUrl().trim();
            user.setResumeUrl(resumeUrl.isEmpty() ? null : resumeUrl);
        }

        if (dto.getSkill() != null) {
            user.setSkill(dto.getSkill());
        }

        if (dto.getProfilePictureUrl() != null) {
            String profilePictureUrl = dto.getProfilePictureUrl().trim();
            user.setProfilePictureUrl(profilePictureUrl.isEmpty() ? null : profilePictureUrl);
        }

        if (dto.getActive() != null) {
            user.setActive(dto.getActive());
        }

        if (dto.getPassword() != null) {
            String password = dto.getPassword().trim();
            if (password.length() < 6) {
                throw new BadRequestException("Password must be at least 6 characters");
            }
            user.setPassword(passwordEncoder.encode(password));
        }
        return userRepository.save(user);
    }

    public void deactivateStudent(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setActive(false);
        userRepository.save(user);
    }

    public User updateStudentProfile(Long id, UpdateStudentProfileDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (dto.getEmail() != null) {
            String email = dto.getEmail().trim();
            if (email.isEmpty()) {
                throw new BadRequestException("Email cannot be blank");
            }
            if (!user.getEmail().equalsIgnoreCase(email) && userRepository.existsByEmail(email)) {
                throw new BadRequestException("Email already exists");
            }
            user.setEmail(email);
        }

        if (dto.getName() != null) {
            String name = dto.getName().trim();
            if (name.isEmpty()) {
                throw new BadRequestException("Name cannot be blank");
            }
            user.setName(name);
        }

        if (dto.getPassword() != null) {
            String password = dto.getPassword().trim();
            if (password.length() < 6) {
                throw new BadRequestException("Password must be at least 6 characters");
            }
            user.setPassword(passwordEncoder.encode(password));
        }

        if (dto.getDept() != null) {
            String dept = dto.getDept().trim();
            user.setDept(dept);
        }

        if (dto.getBio() != null) {
            user.setBio(dto.getBio());
        }

        if (dto.getYearOfStudy() != null) {
            user.setYearOfStudy(dto.getYearOfStudy());
        }

        if (dto.getGraduationYear() != null) {
            user.setGraduationYear(dto.getGraduationYear());
        }

        if (dto.getCgpa() != null) {
            user.setCgpa(dto.getCgpa());
        }

        if (dto.getGithubUrl() != null) {
            user.setGithubUrl(dto.getGithubUrl());
        }

        if (dto.getLinkedinUrl() != null) {
            user.setLinkedinUrl(dto.getLinkedinUrl());
        }

        if (dto.getPortfolioUrl() != null) {
            user.setPortfolioUrl(dto.getPortfolioUrl());
        }

        if (dto.getResumeUrl() != null) {
            user.setResumeUrl(dto.getResumeUrl());
        }

        if (dto.getSkill() != null) {
            user.setSkill(dto.getSkill());
        }

        if (dto.getProfilePictureUrl() != null) {
            user.setProfilePictureUrl(dto.getProfilePictureUrl());
        }
        return userRepository.save(user);
    }

    public List<User> getStudents(boolean active) {
        return userRepository.findByRoleIgnoreCaseAndActive("STUDENT", active);
    }

    public User AdmingetStudentById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        // if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
        //   throw new ResourceNotFoundException("Student not found");
        // }
        return user;
    }

    public User getStudentById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!"STUDENT".equalsIgnoreCase(user.getRole())) {
            throw new ResourceNotFoundException("Student not found");
        }
        return user;
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getPassword() == null) {
            throw new BadRequestException("User is not registered for login");
        }
        String token = jwtService.generateToken(new CustomUserDetails(user));
        return new LoginResponseDTO(token, user.getRole());
    }
}
