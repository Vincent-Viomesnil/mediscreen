package com.ocr.mediscreen.controller;

import com.ocr.mediscreen.exceptions.PatientNotFoundException;
import com.ocr.mediscreen.model.Patient;
import com.ocr.mediscreen.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class PatientController {

    @Autowired
    private PatientService patientService;

    @RequestMapping(value = "/patients", method = RequestMethod.GET)
    public List<Patient> patientList() {
        return patientService.findAll();
    }

    @GetMapping("/patient/id/{id}")
    public Patient getPatientById(@PathVariable Long id) throws PatientNotFoundException {
        return patientService.findById(id);
    }

    @PostMapping("/patient/add")
    public Patient addPatient(@Valid @RequestBody Patient patient) {
        return patientService.addPatient(patient);
    }

    @PutMapping("/patient/update/{id}")
    public Patient updatePatientById(@PathVariable("id") Long id, @RequestBody Patient patient) {
        return patientService.updatePatientById(id, patient);
    }

    @DeleteMapping(value = "/patient/delete/{id}")
    public void deletePatientById(@PathVariable Long id) {
        patientService.deletePatientById(id);
    }

}
