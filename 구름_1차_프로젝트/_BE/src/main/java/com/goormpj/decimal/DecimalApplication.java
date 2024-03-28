package com.goormpj.decimal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DecimalApplication {

	public static void main(String[] args) {
		SpringApplication.run(DecimalApplication.class, args);
	}

}
