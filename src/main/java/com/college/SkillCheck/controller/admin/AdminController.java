package com.college.SkillCheck.controller.admin;

import com.college.SkillCheck.auth.AuthService;
import com.college.SkillCheck.auth.dto.AdminCreateStudentDTO;
import com.college.SkillCheck.dto.CompetencyResponseDTO;
import com.college.SkillCheck.dto.SkillRequestDTO;
import com.college.SkillCheck.dto.SkillSummaryDTO;
import com.college.SkillCheck.dto.TopStudentDTO;
import com.college.SkillCheck.model.Skill;
import com.college.SkillCheck.model.User;
import com.college.SkillCheck.service.CompetencyService;
import com.college.SkillCheck.service.SkillService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AuthService authService;
    private final SkillService skillService;
    private final CompetencyService competencyService;

    public AdminController(AuthService authService,
                           SkillService skillService,
                           CompetencyService competencyService) {
        this.authService = authService;
        this.skillService = skillService;
        this.competencyService = competencyService;
    }

    @PostMapping("/students")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> createStudent(@Valid @RequestBody AdminCreateStudentDTO dto) {
        User created = authService.adminCreateStudent(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/students/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateStudent(@PathVariable Long id, @Valid @RequestBody AdminCreateStudentDTO dto) {
        return ResponseEntity.ok(authService.adminUpdateStudent(id, dto));
    }

    @DeleteMapping("/students/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateStudent(@PathVariable Long id) {
        authService.deactivateStudent(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/skills")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Skill> createSkill(@Valid @RequestBody SkillRequestDTO dto) {
        Skill created = skillService.createSkill(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/skills/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Skill> updateSkill(@PathVariable Long id, @Valid @RequestBody SkillRequestDTO dto) {
        return ResponseEntity.ok(skillService.updateSkill(id, dto));
    }

    @PostMapping("/evaluate/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CompetencyResponseDTO>> evaluateCompetencies(@PathVariable Long studentId) {
        return ResponseEntity.ok(competencyService.evaluateCompetencies(studentId));
    }

    @GetMapping("/top-students")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TopStudentDTO>> getTopStudents() {
        return ResponseEntity.ok(competencyService.getTopStudents());
    }

    @GetMapping("/skill-summary")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SkillSummaryDTO>> getSkillSummary() {
        return ResponseEntity.ok(competencyService.getSkillSummary());
    }
}
