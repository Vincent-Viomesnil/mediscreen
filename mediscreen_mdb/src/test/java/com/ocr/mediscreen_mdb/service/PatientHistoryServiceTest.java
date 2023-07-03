package com.ocr.mediscreen_mdb.service;

import com.ocr.mediscreen_mdb.exceptions.NoteNotFoundException;
import com.ocr.mediscreen_mdb.model.PatientHistory;
import com.ocr.mediscreen_mdb.repository.PatientHistoryDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PatientHistoryServiceTest {

    @Mock
    private PatientHistoryDAO patientHistoryDAO;

    @InjectMocks
    private PatientHistoryService patientHistoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        // Arrange
        List<PatientHistory> patientHistoryList = new ArrayList<>();
        when(patientHistoryDAO.findAll()).thenReturn(patientHistoryList);

        // Act
        List<PatientHistory> result = patientHistoryService.findAll();

        // Assert
        assertEquals(patientHistoryList, result);
        verify(patientHistoryDAO, times(1)).findAll();
        verifyNoMoreInteractions(patientHistoryDAO);
    }

    @Test
    void testAddPatientHistory() {
        // Arrange
        PatientHistory patientHistory = new PatientHistory();
        PatientHistory latestNote = new PatientHistory();
        latestNote.setNoteId(1L);
        when(patientHistoryDAO.findFirstByOrderByNoteIdDesc()).thenReturn(latestNote);
        when(patientHistoryDAO.save(any(PatientHistory.class))).thenReturn(patientHistory);

        // Act
        PatientHistory result = patientHistoryService.addPatientHistory(patientHistory);

        // Assert
        assertNotNull(result);
        assertEquals(patientHistory.getPatId(), result.getPatId());
        assertEquals(patientHistory.getLastname(), result.getLastname());
        assertEquals(patientHistory.getNotes(), result.getNotes());
        verify(patientHistoryDAO, times(1)).findFirstByOrderByNoteIdDesc();
        verify(patientHistoryDAO, times(1)).save(any(PatientHistory.class));
        verifyNoMoreInteractions(patientHistoryDAO);
    }


    @Test
    void testDeleteNoteById() {
        // Arrange
        Long noteId = 1L;
        Optional<PatientHistory> noteOptional = Optional.of(new PatientHistory());
        when(patientHistoryDAO.findByNoteId(noteId)).thenReturn(noteOptional);

        // Act
        patientHistoryService.deleteNoteById(noteId);

        // Assert
        verify(patientHistoryDAO, times(1)).findByNoteId(noteId);
        verify(patientHistoryDAO, times(1)).deleteByNoteId(noteId);
        verifyNoMoreInteractions(patientHistoryDAO);
    }

    @Test
    void testDeleteNoteById_NoteNotFound() {
        // Arrange
        Long noteId = 1L;
        when(patientHistoryDAO.findByNoteId(noteId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoteNotFoundException.class, () -> patientHistoryService.deleteNoteById(noteId));
        verify(patientHistoryDAO, times(1)).findByNoteId(noteId);
        verifyNoMoreInteractions(patientHistoryDAO);
    }

    @Test
    void testGetNoteById() {
        // Arrange
        Long noteId = 1L;
        PatientHistory patientHistory = new PatientHistory();
        Optional<PatientHistory> noteOptional = Optional.of(patientHistory);
        when(patientHistoryDAO.findByNoteId(noteId)).thenReturn(noteOptional);

        // Act
        PatientHistory result = patientHistoryService.getNoteById(noteId);

        // Assert
        assertNotNull(result);
        assertEquals(patientHistory, result);
        verify(patientHistoryDAO, times(1)).findByNoteId(noteId);
        verifyNoMoreInteractions(patientHistoryDAO);
    }

    @Test
    void testGetNoteById_NoteNotFound() {
        // Arrange
        Long noteId = 1L;
        when(patientHistoryDAO.findByNoteId(noteId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoteNotFoundException.class, () -> patientHistoryService.getNoteById(noteId));
        verify(patientHistoryDAO, times(1)).findByNoteId(noteId);
        verifyNoMoreInteractions(patientHistoryDAO);
    }

    @Test
    void testGetListNotesByPatId() {
        // Arrange
        Long patId = 1L;
        List<PatientHistory> patientHistoryList = new ArrayList<>();
        when(patientHistoryDAO.findByPatId(patId)).thenReturn(patientHistoryList);

        // Act
        List<PatientHistory> result = patientHistoryService.getListNotesByPatId(patId);

        // Assert
        assertEquals(patientHistoryList, result);
        verify(patientHistoryDAO, times(1)).findByPatId(patId);
        verifyNoMoreInteractions(patientHistoryDAO);
    }

    @Test
    void testUpdateNoteById() {
        // Arrange
        PatientHistory noteToUpdate = new PatientHistory();
        when(patientHistoryDAO.save(noteToUpdate)).thenReturn(noteToUpdate);

        // Act
        PatientHistory result = patientHistoryService.updateNoteById(noteToUpdate);

        // Assert
        assertNotNull(result);
        assertEquals(noteToUpdate, result);
        verify(patientHistoryDAO, times(1)).save(noteToUpdate);
        verifyNoMoreInteractions(patientHistoryDAO);
    }
}
