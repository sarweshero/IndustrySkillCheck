package com.college.SkillCheck.controller.student;

import com.college.SkillCheck.auth.AuthService;
import com.college.SkillCheck.auth.dto.RegisterStudentDTO;
import com.college.SkillCheck.auth.dto.UpdateStudentProfileDTO;
import com.college.SkillCheck.dto.CompetencyResponseDTO;
import com.college.SkillCheck.dto.EvidenceRequestDTO;
import com.college.SkillCheck.dto.StudentEvidenceRequestDTO;
import com.college.SkillCheck.model.Evidence;
import com.college.SkillCheck.model.User;
import com.college.SkillCheck.security.CustomUserDetails;
import com.college.SkillCheck.service.CompetencyService;
import com.college.SkillCheck.service.EvidenceService;
import com.college.SkillCheck.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final AuthService authService;
    private final EvidenceService evidenceService;
    private final CompetencyService competencyService;
    private final UserService userService;

    public StudentController(AuthService authService,
                             EvidenceService evidenceService,
                             CompetencyService competencyService,
                             UserService userService) {
        this.authService = authService;
        this.evidenceService = evidenceService;
        this.competencyService = competencyService;
        this.userService = userService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<User> getProfile() {
        return ResponseEntity.ok(userService.getUserById(getCurrentUserId()));
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<User> updateProfile(@Valid @RequestBody UpdateStudentProfileDTO dto) {
        Long studentId = getCurrentUserId();
        return ResponseEntity.ok(authService.updateStudentProfile(studentId, dto));
    }

    @PostMapping("/evidence")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Evidence> submitEvidence(@Valid @RequestBody StudentEvidenceRequestDTO dto) {
        Long studentId = getCurrentUserId();
        EvidenceRequestDTO requestDTO = toEvidenceRequest(dto, studentId);
        Evidence created = evidenceService.submitEvidenceForStudent(requestDTO, studentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/competency")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<CompetencyResponseDTO>> getCompetencies() {
        Long studentId = getCurrentUserId();
        return ResponseEntity.ok(competencyService.getCompetenciesByStudent(studentId));
    }

    private Long getCurrentUserId() {
        CustomUserDetails userDetails = getCurrentUserDetails();
        return userDetails.getId();
    }

    private CustomUserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return (CustomUserDetails) principal;
        }
        throw new IllegalStateException("Authenticated user not found");
    }

    private EvidenceRequestDTO toEvidenceRequest(StudentEvidenceRequestDTO dto, Long studentId) {
        EvidenceRequestDTO request = new EvidenceRequestDTO();
        request.setStudentId(studentId);
        request.setType(dto.getType());
        request.setReference(dto.getReference());
        if (dto.getSkills() != null) {
            List<EvidenceRequestDTO.EvidenceSkillRequest> skills = dto.getSkills().stream()
                    .map(skill -> {
                        EvidenceRequestDTO.EvidenceSkillRequest requestSkill =
                                new EvidenceRequestDTO.EvidenceSkillRequest();
                        requestSkill.setSkillId(skill.getSkillId());
                        requestSkill.setWeight(skill.getWeight());
                        return requestSkill;
                    })
                    .toList();
            request.setSkills(skills);
        }
        return request;
    }
}
