package com.hpzzz.smsfilter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SmsFilterApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmsFilterApplication.class, args);
	}

}
