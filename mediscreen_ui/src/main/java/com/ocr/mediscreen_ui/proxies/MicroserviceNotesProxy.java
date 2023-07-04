package com.ocr.mediscreen_ui.proxies;

import com.ocr.mediscreen_ui.model.PatientHistoryBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "mediscreen-mdb", url = "${mediscreen-mdb.url}")
public interface MicroserviceNotesProxy {

    @GetMapping(value = "/pathistorylist")
    List<PatientHistoryBean> patientList();

    @GetMapping(value = "/pathistory/patid/{patId}")
    List<PatientHistoryBean> getListNotesByPatId(@PathVariable Long patId);

    @GetMapping(value = "/pathistory/noteid/{noteId}")
    PatientHistoryBean getNoteById(@PathVariable Long noteId);

    @PostMapping(value = "/pathistory/add")
    PatientHistoryBean addNote(@RequestBody PatientHistoryBean patientHistory);

    @PutMapping("/pathistory/update/{id}")
    PatientHistoryBean updateNoteById(@PathVariable Long id, @RequestBody PatientHistoryBean patientNoteToUpdate);

    @DeleteMapping(value = "/pathistory/delete/{noteId}")
    void deleteNoteById(@PathVariable Long noteId);


}