package com.college.SkillCheck.controller;

import com.college.SkillCheck.dto.EvidenceRequestDTO;
import com.college.SkillCheck.model.Evidence;
import com.college.SkillCheck.service.EvidenceService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/evidence")
@PreAuthorize("hasRole('ADMIN')")
public class EvidenceController {

    private final EvidenceService evidenceService;

    public EvidenceController(EvidenceService evidenceService) {
        this.evidenceService = evidenceService;
    }

    @PostMapping
    public ResponseEntity<Evidence> submitEvidence(@Valid @RequestBody EvidenceRequestDTO dto) {
        Evidence created = evidenceService.submitEvidence(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evidence> getEvidence(@PathVariable Long id) {
        return ResponseEntity.ok(evidenceService.getEvidenceById(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Evidence>> getEvidenceByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(evidenceService.getEvidenceByStudent(studentId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvidence(@PathVariable Long id) {
        evidenceService.deleteEvidence(id);
        return ResponseEntity.noContent().build();
    }
}
