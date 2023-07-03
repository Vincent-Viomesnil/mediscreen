package com.ocr.mediscreen_assess.proxies;

import com.ocr.mediscreen_assess.model.PatientHistoryBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "mediscreen-mdb", url = "${mediscreen-mdb.url}")
public interface MicroserviceNotesProxy {

    @GetMapping(value = "/PatHistoryList")
    List<PatientHistoryBean> patientList();

    @GetMapping(value = "/PatHistory/patid/{patId}")
    List<PatientHistoryBean> getListNotesByPatId(@PathVariable Long patId);
}
