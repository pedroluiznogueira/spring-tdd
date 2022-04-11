package com.github.pedroluiznogueira.testingapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class TestingApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestingApiApplication.class, args);
		log.info("Running...");
	}

}
