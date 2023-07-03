package com.ocr.mediscreen_assess.service;

import com.ocr.mediscreen_assess.model.PatientBean;
import com.ocr.mediscreen_assess.model.PatientHistoryBean;
import com.ocr.mediscreen_assess.model.TriggerWords;
import com.ocr.mediscreen_assess.proxies.MicroserviceNotesProxy;
import com.ocr.mediscreen_assess.proxies.MicroservicePatientProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class AssessmentService {


    private TriggerWords triggerWords = new TriggerWords();

    @Autowired
    private MicroserviceNotesProxy microserviceNotesProxy;
    @Autowired
    private MicroservicePatientProxy microservicePatientProxy;

    public String getAssessmentById(Long patId) {
        PatientBean patient = microservicePatientProxy.getPatientById(patId);
        List<PatientHistoryBean> patientHistoryList = microserviceNotesProxy.getListNotesByPatId(patId);

        if (patient == null || patId == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient  not found !");
        }
        String riskLevel = "Unknown";
        int nbTrigger = 0;

        for (PatientHistoryBean patientHistory : patientHistoryList) {
            long count = triggerWords.getTriggerList().stream()
                    .filter(trigger -> patientHistory.getNotes().toLowerCase().contains(trigger.toLowerCase()))
                    .count();
            nbTrigger += count;
        }

        Integer age = getAge(patient.getBirthdate());
        String gender = patient.getGender();

        if (noneMatch(nbTrigger)) {
            riskLevel = "None";
        } else if (borderline(nbTrigger, age, gender)) {
            riskLevel = "Borderline";
        } else if (danger(nbTrigger, age, gender)) {
            riskLevel = "In Danger";
        } else if (earlyOnset(nbTrigger, age, gender)) {
            riskLevel = "Early onset";
        }
        return diabetesAssessment(patient, riskLevel);

    }


    private String diabetesAssessment(PatientBean patientBean, String riskLevel) {
        int age = getAge(patientBean.getBirthdate());
        return patientBean.getFirstname() + " " + patientBean.getLastname() + " (age " + age +
                ") diabetes assessment is: " + riskLevel;
    }

    public Integer getAge(LocalDate birthdate) {
        LocalDate now = LocalDate.now();
        Period period = Period.between(birthdate, now);
        return period.getYears();
    }

    private boolean noneMatch(Integer nbTrigger) {
        return nbTrigger == 0;
    }

    private boolean borderline(Integer nbTrigger, Integer age, String gender) {
        return nbTrigger == 2 && gender.equals("F") || gender.equals("M") && age > 30;
    }

    private boolean danger(Integer nbTrigger, Integer age, String gender) {

        return (age < 30) && (gender.equals("M")) && (nbTrigger == 3)
                ||
                (gender.equals("F")) && (nbTrigger == 4)
                ||
                (age >= 30) && (nbTrigger == 6);
    }

    private boolean earlyOnset(Integer nbTrigger, Integer age, String gender) {
        return (age < 30) && (gender.equals("M")) && (nbTrigger == 5)
                ||
                (gender.equals("F")) && (nbTrigger == 7)
                ||
                (age <= 30) && (gender.equals("F")) || (gender.equals("M")) && (nbTrigger >= 8);
    }

}
