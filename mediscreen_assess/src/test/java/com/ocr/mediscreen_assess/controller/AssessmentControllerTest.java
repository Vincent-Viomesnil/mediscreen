package com.ocr.mediscreen_assess.controller;

import com.ocr.mediscreen_assess.service.AssessmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AssessmentController.class)
public class AssessmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssessmentService assessmentService;

    @Test
    void testGetAssessmentById() throws Exception {
        // Arrange
        Long id = 1L;
        String expectedAssessment = "Some assessment";
        when(assessmentService.getAssessmentById(id)).thenReturn(expectedAssessment);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/assessment/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAssessment));

        verify(assessmentService, times(1)).getAssessmentById(id);
        verifyNoMoreInteractions(assessmentService);
    }
}
