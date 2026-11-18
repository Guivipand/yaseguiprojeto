package com.ifsp.yas1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Yas1Application {

	public static void main(String[] args) {
		SpringApplication.run(Yas1Application.class, args);
	}

}
