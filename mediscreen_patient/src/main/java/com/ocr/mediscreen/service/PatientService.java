package com.ocr.mediscreen.service;

import com.ocr.mediscreen.exceptions.PatientNoCreateException;
import com.ocr.mediscreen.exceptions.PatientNotFoundException;
import com.ocr.mediscreen.model.Patient;
import com.ocr.mediscreen.repository.PatientDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PatientService {

    @Autowired
    private PatientDAO patientDAO;

    public List<Patient> findAll() {
        List<Patient> patientList = patientDAO.findAll();
        return patientList;
    }


    public Patient findById(Long id) {
        Optional<Patient> patient = patientDAO.findById(id);
        if (patient.isEmpty()) throw new PatientNotFoundException("Patient with id " + id + " doesn't exist");
        return patient.get();
    }


    public void searchExistingPatient(String firstname, String lastname, LocalDate birthdate) {
        Patient existingPatient = patientDAO.findByFirstnameAndLastnameAndBirthdate(firstname, lastname, birthdate);
        if (existingPatient != null) {
            throw new PatientNoCreateException("Patient already exists");
        }
    }

    public Patient addPatient(Patient patient) {
        String firstname = patient.getFirstname();
        String lastname = patient.getLastname();
        LocalDate birthdate = patient.getBirthdate();

        searchExistingPatient(firstname, lastname, birthdate);

        Patient patientToAdd = patientDAO.save(patient);
        return patientToAdd;
    }


    public Patient updatePatientById(Long id, Patient patient) {
        if (patientDAO.findById(id).isPresent()) {
            patient.setId(id);
            return patientDAO.save(patient);
        } else throw new PatientNotFoundException("The patient with id " + id + " doesn't exist");
    }

    public void deletePatientById(Long id) {
        if (patientDAO.findById(id).isPresent()) {
            patientDAO.deleteById(id);
        } else throw new PatientNotFoundException("The patient with id " + id + " doesn't exist");
    }


}

