package com.ocr.mediscreen_ui.controller;

import com.ocr.mediscreen_ui.exceptions.PatientNotFoundException;
import com.ocr.mediscreen_ui.model.PatientBean;
import com.ocr.mediscreen_ui.model.PatientHistoryBean;
import com.ocr.mediscreen_ui.proxies.AssessmentProxy;
import com.ocr.mediscreen_ui.proxies.MicroserviceNotesProxy;
import com.ocr.mediscreen_ui.proxies.MicroservicePatientProxy;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;

@Controller
@Slf4j
public class FrontController {

    @Autowired
    private MicroserviceNotesProxy microserviceNotesProxy;
    @Autowired
    private AssessmentProxy assessmentProxy;
    @Autowired
    private MicroservicePatientProxy microservicePatientProxy;


    @GetMapping("/patientlist")
    public String home(Model model) {
        List<PatientBean> uniquePatientList = microservicePatientProxy.patientList();
        model.addAttribute("uniquePatientList", uniquePatientList);
        return "Home";
    }



    @GetMapping("/patientdetails")
    public String getPatientDetails(@RequestParam(name = "patId") Long patId, Model model, RedirectAttributes redir) {
        try {
            List<PatientHistoryBean> patientHistoryList = microserviceNotesProxy.getListNotesByPatId(patId);
            String assessmentResult = assessmentProxy.getAssessmentById(patId);
            String assessmentResultWithoutLastElement = assessmentResult.substring(0, assessmentResult.lastIndexOf(" "));
            PatientBean patient = microservicePatientProxy.getPatientById(patId);

            model.addAttribute("assessmentResultWithoutLastElement", assessmentResultWithoutLastElement);
            model.addAttribute("patientHistoryList", patientHistoryList);
            model.addAttribute("assessmentResult", assessmentResult);
            model.addAttribute("patient", patient);
            redir.addFlashAttribute("success", "Patient successfully added/updated");

            return "PatientDetails";
        } catch (FeignException.BadRequest e) {
            throw new PatientNotFoundException("Request Incorrect");
        } catch (FeignException e) {
            redir.addFlashAttribute("error", e.status() + " during operation");
            return "redirect:/";
        }
    }



    @GetMapping(value = "/pathistory/add")
    public String getPatientHistory(Model model) {
        PatientHistoryBean patientHistory = new PatientHistoryBean();

        List<PatientBean> patients = microservicePatientProxy.patientList();
        model.addAttribute("patientBean", new PatientBean());
        model.addAttribute("patients", patients);
        model.addAttribute("patientHistory", patientHistory);
        return "AddNote";
    }


    @GetMapping(value = "/patient/add")
    public String getPatient(Model model) {
        PatientBean patient = new PatientBean();
        model.addAttribute("patient", patient);

        return "AddPatient";
    }

    @PostMapping(value = "/patient/validate")
    public String addPatient(@ModelAttribute("patient") PatientBean patient, Model model, RedirectAttributes redir) {
        try {
            PatientBean patientExisting = new PatientBean();
            String firstname = patientExisting.getFirstname();
            String lastname = patientExisting.getLastname();
            LocalDate birthdate = patientExisting.getBirthdate();

            if (patient.getFirstname().equals(firstname)
                    && patient.getLastname().equals(lastname)
                    && patient.getBirthdate().equals(birthdate)) {

                redir.addFlashAttribute("error", "The patient " + firstname + " " + lastname + " already exists");
                System.out.println("FeignException");
                return "AddPatient";
            }

            PatientBean patientAdded = microservicePatientProxy.addPatient(patient);
            model.addAttribute("patientAdded", patientAdded);

            List<PatientBean> uniquePatientList = microservicePatientProxy.patientList();
            model.addAttribute("uniquePatientList", uniquePatientList);
            redir.addFlashAttribute("success", "Patient successfully added");

            return "redirect:/patientlist";

        } catch (FeignException e) {
            redir.addFlashAttribute("error", "The patient already exists");
            System.out.println("FE");

            List<PatientBean> uniquePatientList = microservicePatientProxy.patientList();
            model.addAttribute("uniquePatientList", uniquePatientList);

            return "redirect:/patientlist";

        }
    }


    @PostMapping(value = "/pathistory/validate")
    public String addPatientHistory(PatientHistoryBean patientHistory, Model model, RedirectAttributes redir) {

        try {
            PatientHistoryBean patientAdded = microserviceNotesProxy.addNote(patientHistory);
            PatientBean patientBean = microservicePatientProxy.getPatientById(patientHistory.getPatId());
            model.addAttribute("patientAdded", patientAdded);

            List<PatientHistoryBean> uniquePatientList = microserviceNotesProxy.patientList();
            model.addAttribute("patientBean", patientBean);
            model.addAttribute("uniquePatientList", uniquePatientList);
            Long patId = patientHistory.getPatId();

            redir.addFlashAttribute("success", "Patient successfully added");

            return "redirect:/patientdetails?patId=" +patId;
        } catch (FeignException e) {
            redir.addFlashAttribute("error", e.status() + " during operation");
            return "AddNote";
        }
    }

    @GetMapping("/patient/update/{id}")
    public String updatePatientForm(@PathVariable("id") Long id, Model model) {
        PatientBean patient = microservicePatientProxy.getPatientById(id);
        List<PatientBean> uniquePatientList = microservicePatientProxy.patientList();

        model.addAttribute("uniquePatientList", uniquePatientList);
        model.addAttribute("patient", patient);

        return "Update";
    }

    @PostMapping(value = "/patient/update/{id}")
    public String updatePatient(@PathVariable Long id, PatientBean patientToUpdate, Model model,
                                RedirectAttributes redir) {
        try {
            PatientBean patient = microservicePatientProxy.updatePatientById(id, patientToUpdate);
            model.addAttribute("patient", patient);

            List<PatientBean> uniquePatientList = microservicePatientProxy.patientList();
            Long patId = patientToUpdate.getId();


            model.addAttribute("uniquePatientList", uniquePatientList);
            redir.addFlashAttribute("success", "Patient successfully updated");

            return "redirect:/patientdetails?patId=" + patId;
        } catch (FeignException.BadRequest e) {
            redir.addFlashAttribute("error", "Bad request during operation");
            return "redirect:/patientlist";
        }
    }

    @GetMapping("/pathistory/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        PatientHistoryBean patientHistory = microserviceNotesProxy.getNoteById(id);
        List<PatientHistoryBean> patientHistoryList = microserviceNotesProxy.getListNotesByPatId(patientHistory.getPatId());

        model.addAttribute("patientHistoryList", patientHistoryList);
        model.addAttribute("patientHistory", patientHistory);

        return "UpdateNote";
    }

    @PostMapping(value = "/pathistory/update/{id}")
    public String updatePatientHistory(@PathVariable Long id, PatientHistoryBean patientToUpdate, Model model,
                                       RedirectAttributes redir) {
        try {
            PatientHistoryBean patientHistory = microserviceNotesProxy.updateNoteById(id, patientToUpdate);
            model.addAttribute("patientHistory", patientHistory);
            List<PatientHistoryBean> patientHistoryList = microserviceNotesProxy.getListNotesByPatId(patientHistory.getPatId());

            model.addAttribute("patientHistoryList", patientHistoryList);
            Long patId = patientHistory.getPatId();
            redir.addFlashAttribute("success", "Note successfully updated");

            return "redirect:/patientdetails?patId=" + patId;
        } catch (FeignException e) {
            redir.addFlashAttribute("error", e.status() + " during operation");
            return "Home";
        }
    }

    @PostMapping("/patient/delete/{id}")
    public String deletePatient(@PathVariable("id") Long id, RedirectAttributes redir, Model model) {
        microservicePatientProxy.deletePatientById(id);
        redir.addFlashAttribute("success", "Patient successfully deleted");
        return "redirect:/patientlist";
    }

    @PostMapping(value = "/pathistory/delete/{id}")
    public String deletePatientById(@PathVariable Long id, Model model, RedirectAttributes redir) {
        microserviceNotesProxy.deleteNoteById(id);
        List<PatientHistoryBean> uniquePatientList = microserviceNotesProxy.patientList();
        model.addAttribute("uniquePatientList", uniquePatientList);
        redir.addFlashAttribute("success", "Note successfully deleted");

        return "redirect:/patientlist";
    }
}
