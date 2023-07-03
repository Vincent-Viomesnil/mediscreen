package com.ocr.mediscreen_mdb.controller;

import com.ocr.mediscreen_mdb.model.PatientHistory;
import com.ocr.mediscreen_mdb.service.PatientHistoryService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class PatientHistoryControllerTest {

    @Mock
    private PatientHistoryService patientHistoryService;

    @InjectMocks
    private PatientHistoryController patientHistoryController;

    private List<PatientHistory> patientHistoryList;

    private PatientHistory patientHistory;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        patientHistoryList = new ArrayList<>();
        patientHistoryList.add(new PatientHistory(new ObjectId(), 10L, 1L, "Lastname1", "Note 1"));
        patientHistoryList.add(new PatientHistory(new ObjectId(), 20L, 2L, "Lastname2", "Note 2"));
    }

    @Test
    public void testGetPatientList() {
        // Act
        List<PatientHistory> patientHistoryList = patientHistoryController.patientList();

        // Assert
        verify(patientHistoryService, times(1)).findAll();
    }


    @Test
    public void testAddPatientHistory() {
        // Arrange
        PatientHistory patientHistoryToAdd = new PatientHistory(new ObjectId(), 10L, 1L, "Lastname1", "Note 1");
        when(patientHistoryService.addPatientHistory(patientHistoryToAdd)).thenReturn(patientHistoryToAdd);

        // Act
        PatientHistory result = patientHistoryController.addNote(patientHistoryToAdd);

        // Assert
        assertNotNull(result);
        assertEquals(patientHistoryToAdd, result);
        verify(patientHistoryService, times(1)).addPatientHistory(patientHistoryToAdd);
    }


    @Test
    void testGetNoteById() {
        // Arrange
        Long noteId = 1L;
        PatientHistory expectedNote = new PatientHistory();
        when(patientHistoryService.getNoteById(noteId)).thenReturn(expectedNote);

        // Act
        PatientHistory result = patientHistoryService.getNoteById(noteId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedNote, result);
        verify(patientHistoryService, times(1)).getNoteById(noteId);
        verifyNoMoreInteractions(patientHistoryService);
    }
}




