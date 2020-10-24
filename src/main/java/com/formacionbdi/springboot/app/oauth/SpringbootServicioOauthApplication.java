package com.formacionbdi.springboot.app.oauth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
public class SpringbootServicioOauthApplication implements CommandLineRunner {

	public BCryptPasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringbootServicioOauthApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String password = "12345";
		
		for (int i = 0; i < 4; i++) {
			System.out.println(passwordEncoder.encode(password));
		}
		
	}

}
