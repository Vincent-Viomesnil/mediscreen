package com.ocr.mediscreen_mdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class Mediscreen_mdbApplication {

	public static void main(String[] args) {
		SpringApplication.run(Mediscreen_mdbApplication.class, args);
	}

}
