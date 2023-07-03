package com.ocr.mediscreen_mdb.controller;

import com.ocr.mediscreen_mdb.model.PatientHistory;
import com.ocr.mediscreen_mdb.service.PatientHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PatientHistoryController {

    private static final Logger logger = LoggerFactory.getLogger(PatientHistoryController.class);

    @Autowired
    private PatientHistoryService patientHistoryService;

    @GetMapping(value = "/PatHistoryList")
    public List<PatientHistory> patientList() {
        logger.info("Get All the Notes");
        return patientHistoryService.findAll();
    }

    @GetMapping(value = "/PatHistory/patid/{patId}")
    public List<PatientHistory> getListNotesByPatId(@PathVariable Long patId) {
        logger.info("get list of notes with patient id : " + patId);
        return patientHistoryService.getListNotesByPatId(patId);
    }

    @GetMapping(value = "/PatHistory/noteid/{noteId}")
    public PatientHistory getNoteById(@PathVariable Long noteId) {
        logger.info("Get Note Id: " + noteId);
        return patientHistoryService.getNoteById(noteId);
    }

    @PostMapping(value = "/PatHistory/add")
    public PatientHistory addNote(@RequestBody PatientHistory patientHistory) {
        logger.info("Note Added: ");
        return patientHistoryService.addPatientHistory(patientHistory);
    }

    @PutMapping("/PatHistory/update/{id}")
    public PatientHistory updateNoteById(@PathVariable Long id,
                                         @RequestBody PatientHistory patientNoteToUpdate) {
        logger.info("Note updated : " + id);
        PatientHistory noteUpdated = patientHistoryService.getNoteById(id);
        noteUpdated.setNotes(patientNoteToUpdate.getNotes());
        return patientHistoryService.updateNoteById(noteUpdated);
    }

    @DeleteMapping(value = "/PatHistory/delete/{noteId}")
    public void deleteNoteById(@PathVariable Long noteId) {
        logger.info("Note deleted : " + noteId);
        patientHistoryService.deleteNoteById(noteId);
    }
}
