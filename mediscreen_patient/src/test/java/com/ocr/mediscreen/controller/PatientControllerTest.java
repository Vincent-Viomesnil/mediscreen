package com.ocr.mediscreen.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocr.mediscreen.controller.PatientController;
import com.ocr.mediscreen.exceptions.PatientNotFoundException;
import com.ocr.mediscreen.model.Patient;
import com.ocr.mediscreen.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PatientControllerTest {

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(patientController)
                .build();
    }

    @Test
    void testPatientList() throws Exception {
        Patient patient1 = new Patient(1L, "Firstname", "Lastname", LocalDate.of(1990, 1, 1), "M", "address", "010203040506");
        Patient patient2 = new Patient(2L, "Prenom", "Nom", LocalDate.of(1990, 1, 1), "F", "postalcode", "1234567890");
        List<Patient> patients = Arrays.asList(patient1, patient2);

        when(patientService.findAll()).thenReturn(patients);

        mockMvc.perform(get("/Patients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(patientService, times(1)).findAll();
        verifyNoMoreInteractions(patientService);
    }

    @Test
    void testGetPatientById() throws Exception {
        Patient patient = new Patient(1L, "Firstname", "Lastname", LocalDate.of(1990, 1, 1), "M", "address", "010203040506");

        when(patientService.findById(1L)).thenReturn(patient);

        mockMvc.perform(get("/Patient/id/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(patient.getId()))
                .andExpect(jsonPath("$.firstname").value(patient.getFirstname()))
                .andExpect(jsonPath("$.lastname").value(patient.getLastname()))
                .andExpect(jsonPath("$.gender").value(patient.getGender()))
                .andExpect(jsonPath("$.address").value(patient.getAddress()))
                .andExpect(jsonPath("$.phonenumber").value(patient.getPhonenumber()));

        verify(patientService, times(1)).findById(1L);
        verifyNoMoreInteractions(patientService);
    }

    @Test
    void testGetPatientByIdNotFoundException() throws Exception {
        when(patientService.findById(1L)).thenThrow(new PatientNotFoundException("Patient Not Found"));

        mockMvc.perform(get("/Patient/id/{id}", 1L))
                .andExpect(status().is4xxClientError());

        verify(patientService, times(1)).findById(1L);
        verifyNoMoreInteractions(patientService);
    }


}
