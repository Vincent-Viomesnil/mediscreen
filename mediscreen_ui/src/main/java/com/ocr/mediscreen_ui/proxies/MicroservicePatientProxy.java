package com.ocr.mediscreen_ui.proxies;

import com.ocr.mediscreen_ui.model.PatientBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "mediscreen", url = "${mediscreen.url}")
public interface MicroservicePatientProxy {


    @GetMapping(value = "/patients")
    List<PatientBean> patientList();

    @GetMapping(value = "/patient/id/{id}")
    PatientBean getPatientById(@PathVariable Long id);

    @PostMapping(value = "/patient/add")
    PatientBean addPatient(PatientBean patient);

    @PutMapping(value = "/patient/update/{id}")
    PatientBean updatePatientById(@PathVariable Long id, PatientBean patientToUpdate);

    @DeleteMapping(value = "/patient/delete/{id}")
    void deletePatientById(@PathVariable Long id);

}