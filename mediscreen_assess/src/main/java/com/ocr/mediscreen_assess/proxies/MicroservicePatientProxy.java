package com.ocr.mediscreen_assess.proxies;

import com.ocr.mediscreen_assess.model.PatientBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "mediscreen", url = "${mediscreen.url}")
public interface MicroservicePatientProxy {
    @GetMapping(value = "/Patient/id/{id}")
    PatientBean getPatientById(@PathVariable Long id);

}
