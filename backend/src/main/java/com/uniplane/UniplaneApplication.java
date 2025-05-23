package com.uniplane;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScan(basePackages = "com.uniplane")
@EnableAsync
public class UniplaneApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniplaneApplication.class, args);
	}

}
