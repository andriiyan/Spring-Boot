package com.andriiyan.springlearning.springboot;

import com.andriiyan.springlearning.springboot.api.facade.BookingFacade;
import com.andriiyan.springlearning.springboot.impl.model.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = "com.andriiyan.springlearning.springboot")
public class Application {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner demo(BookingFacade bookingFacade) {
		return (args) -> {
			logger.info("Demo has started");
			bookingFacade.createUser(new UserEntity("Andrii", "ayan@gmail.com"));
			logger.info("Find first user is {}", bookingFacade.getUserById(1));
		};
	}

}