package com.ocr.mediscreen.service;

import com.ocr.mediscreen.exceptions.PatientNoCreateException;
import com.ocr.mediscreen.exceptions.PatientNotFoundException;
import com.ocr.mediscreen.model.Patient;
import com.ocr.mediscreen.repository.PatientDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PatientServiceTest {

    @Mock
    private PatientDAO patientDAO;

    @InjectMocks
    private PatientService patientService;

    private List<Patient> patientList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        patientList = new ArrayList<>();
        patientList.add(new Patient(1L, "First", "Last", LocalDate.of(1990, 04, 01), "Male", "Address 1", "123456789"));
        patientList.add(new Patient(2L, "First2", "Last2", LocalDate.of(1990, 04, 01), "Female", "Address 2", "987654321"));
    }


    @Test
    public void testFindAllPatients() {
        // Arrange
        when(patientDAO.findAll()).thenReturn(patientList);

        // Act
        List<Patient> actualPatients = patientService.findAll();

        // Assert
        assertNotNull(actualPatients);
        assertEquals(patientList.size(), actualPatients.size());
        assertEquals(patientList, actualPatients);
        verify(patientDAO, times(1)).findAll();
    }

    @Test
    public void testFindPatientById() {
        // Arrange
        Long id = 1L;
        Patient expectedPatient = new Patient();
        when(patientDAO.findById(id)).thenReturn(Optional.of(expectedPatient));

        // Act
        Patient actualPatient = patientService.findById(id);

        // Assert
        assertNotNull(actualPatient);
        assertEquals(expectedPatient, actualPatient);
        verify(patientDAO, times(1)).findById(id);
    }


    @Test
    public void testAddPatient() {
        // Arrange
        Patient patientToAdd = new Patient();
        when(patientDAO.save(patientToAdd)).thenReturn(patientToAdd);

        // Act
        Patient addedPatient = patientService.addPatient(patientToAdd);

        // Assert
        assertNotNull(addedPatient);
        assertEquals(patientToAdd, addedPatient);
        verify(patientDAO, times(1)).save(patientToAdd);
    }


    @Test
    public void testDeletePatientById() {
        Long id = 1L;
        when(patientDAO.findById(id)).thenReturn(Optional.of(new Patient()));

        assertDoesNotThrow(() -> patientService.deletePatientById(id));

        verify(patientDAO, times(1)).deleteById(id);
    }

    @Test
    void testUpdatePatientById() {
        Patient existingPatient = new Patient(1L, "First", "Last", LocalDate.of(1990, 04, 01), "Male", "Address 1", "123456789");

        when(patientDAO.findById(existingPatient.getId())).thenReturn(Optional.of(existingPatient));

        Patient updatedPatient = new Patient();
        updatedPatient.setId(1L);
        updatedPatient.setFirstname("OtherFirstname");

        when(patientDAO.save(updatedPatient)).thenReturn(updatedPatient);

        Patient result = patientService.updatePatientById(existingPatient.getId(), updatedPatient);

        assertEquals(existingPatient.getId(), result.getId());
        assertEquals("OtherFirstname", result.getFirstname());

        verify(patientDAO).findById(existingPatient.getId());

        verify(patientDAO).save(updatedPatient);
    }


    @Test
    void testUpdatePatientById_PatientNotFound() {
        Long nonExistentPatientId = 2L;
        Patient updatedPatient = new Patient();
        updatedPatient.setId(nonExistentPatientId);
        updatedPatient.setFirstname("OtherFirstname");

        when(patientDAO.findById(nonExistentPatientId)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> {
            patientService.updatePatientById(nonExistentPatientId, updatedPatient);
        });
    }


    @Test
    void testDeletePatientByIdPatientNotFound() {
        Long id = 1L;
        when(patientDAO.findById(id)).thenReturn(Optional.empty());

        PatientNotFoundException exception = assertThrows(PatientNotFoundException.class,
                () -> patientService.deletePatientById(id));
        assertEquals("The patient with id " + id + " doesn't exist", exception.getMessage());

        verify(patientDAO, never()).deleteById(id);
    }

    @Test
    void testConstructorAndGetMessage() {
        String message = "Patient creation failed";
        PatientNoCreateException exception = new PatientNoCreateException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testFillInStackTrace() {
        PatientNoCreateException exception = new PatientNoCreateException("Patient creation failed");

        assertNull(exception.fillInStackTrace());
    }

}

