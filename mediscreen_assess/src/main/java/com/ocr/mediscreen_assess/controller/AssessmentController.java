package com.ocr.mediscreen_assess.controller;

import com.ocr.mediscreen_assess.service.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    @GetMapping("/assessment/{id}")
    public String getAssessmentById(@PathVariable Long id) {
        return assessmentService.getAssessmentById(id);
    }
}
