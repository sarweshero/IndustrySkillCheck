package com.college.SkillCheck.auth;

import com.college.SkillCheck.auth.dto.AdminCreateStudentDTO;
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
        user.setActive(Boolean.TRUE.equals(dto.getActive()));
        user.setCreatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return userRepository.save(user);
    }

    public User adminUpdateStudent(Long id, AdminCreateStudentDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!user.getEmail().equalsIgnoreCase(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole("STUDENT");
        user.setActive(Boolean.TRUE.equals(dto.getActive()));
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
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
        return userRepository.save(user);
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
