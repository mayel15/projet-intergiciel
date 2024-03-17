package com.pr1.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Producteur1Application {

	public static void main(String[] args) {
		SpringApplication.run(Producteur1Application.class, args);
	}

}
