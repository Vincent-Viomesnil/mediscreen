package com.ocr.mediscreen_assess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableFeignClients("com.ocr.mediscreen_assess")
@EnableSwagger2
public class MediscreenAssessApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediscreenAssessApplication.class, args);
    }

}
