package com.andriiyan.springlearning.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

//	@Bean
//	CommandLineRunner demo(BookingFacade bookingFacade, PasswordEncoder passwordEncoder) {
//		return (args) -> {
//			logger.info("Demo has started");
//			bookingFacade.createUser(new UserEntity("Andrii", "ayan@gmail.com", passwordEncoder.encode("pass"), User.SCOPE_USER));
//			logger.info("Find first user is {}", bookingFacade.getUserById(1));
//		};
//	}

}
