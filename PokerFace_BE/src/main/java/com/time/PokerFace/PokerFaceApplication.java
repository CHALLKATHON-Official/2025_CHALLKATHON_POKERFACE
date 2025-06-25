package com.time.PokerFace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.time.PokerFace")
public class PokerFaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PokerFaceApplication.class, args);
	}

}
