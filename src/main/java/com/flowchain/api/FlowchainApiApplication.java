package com.flowchain.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
public class FlowchainApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlowchainApiApplication.class, args);
	}

}
