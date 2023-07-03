package com.ocr.mediscreen_mdb.service;

import com.ocr.mediscreen_mdb.exceptions.NoteNotFoundException;
import com.ocr.mediscreen_mdb.model.PatientHistory;
import com.ocr.mediscreen_mdb.repository.PatientHistoryDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PatientHistoryService {

    @Autowired
    private PatientHistoryDAO patientHistoryDAO;
    public List<PatientHistory> findAll()  {
        return patientHistoryDAO.findAll();
    }

    public PatientHistory addPatientHistory(PatientHistory patientHistory) {
        PatientHistory patientHistory1 = new PatientHistory();

            PatientHistory latestNote = patientHistoryDAO.findFirstByOrderByNoteIdDesc();

            Long newNoteId = latestNote.getNoteId() + 1;
            patientHistory1.setPatId(patientHistory.getPatId());
            patientHistory1.setLastname(patientHistory.getLastname());
            patientHistory1.setNotes(patientHistory.getNotes());
            patientHistory1.setNoteId(newNoteId);

        return patientHistoryDAO.save(patientHistory1);
    }



    public void deleteNoteById(Long noteId) {
        Optional<PatientHistory> note =  patientHistoryDAO.findByNoteId(noteId);
        if (note.isEmpty()) throw new NoteNotFoundException("Note with id " + noteId + " doesn't exist");
            patientHistoryDAO.deleteByNoteId(noteId);
    }

    public PatientHistory getNoteById(Long noteId) {
        Optional<PatientHistory> note =  patientHistoryDAO.findByNoteId(noteId);
        if (note.isEmpty()) throw new NoteNotFoundException("Note with id " + noteId + " doesn't exist");
        return note.get();
    }

    public List<PatientHistory> getListNotesByPatId(Long patId) {
        return patientHistoryDAO.findByPatId(patId);
    }

    public PatientHistory updateNoteById(PatientHistory noteUpdated) {
        return patientHistoryDAO.save(noteUpdated);
    }
}
