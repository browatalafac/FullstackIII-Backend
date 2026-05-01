package com.fullstack3.bff_emergencia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class BffEmergenciaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BffEmergenciaApplication.class, args);
	}

}
