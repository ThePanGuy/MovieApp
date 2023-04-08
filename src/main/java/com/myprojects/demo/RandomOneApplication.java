package com.myprojects.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RandomOneApplication {

	public static void main(String[] args) {
		SpringApplication.run(RandomOneApplication.class, args);
	}

}
