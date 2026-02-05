package com.college.SkillCheck.controller;

import com.college.SkillCheck.dto.CompetencyResponseDTO;
import com.college.SkillCheck.service.CompetencyService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/competencies")
@Validated
public class CompetencyController {

    private final CompetencyService competencyService;

    public CompetencyController(CompetencyService competencyService) {
        this.competencyService = competencyService;
    }

    @PostMapping("/evaluate/{studentId}")
    public ResponseEntity<List<CompetencyResponseDTO>> evaluateCompetencies(@PathVariable Long studentId) {
        return ResponseEntity.ok(competencyService.evaluateCompetencies(studentId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetencyResponseDTO> getCompetency(@PathVariable Long id) {
        return ResponseEntity.ok(competencyService.getCompetency(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<CompetencyResponseDTO>> getCompetenciesByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(competencyService.getCompetenciesByStudent(studentId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompetencyResponseDTO> updateCompetencyScore(@PathVariable Long id,
                                                                       @RequestParam @NotNull Integer score) {
        return ResponseEntity.ok(competencyService.updateCompetencyScore(id, score));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompetency(@PathVariable Long id) {
        competencyService.deleteCompetency(id);
        return ResponseEntity.noContent().build();
    }
}
