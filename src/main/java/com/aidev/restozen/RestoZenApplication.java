package com.aidev.restozen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RestoZenApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestoZenApplication.class, args);
	}

}
