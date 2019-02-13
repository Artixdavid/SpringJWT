package com.test.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.test.app.models.service.UploadFileServiceImpl;

@SpringBootApplication
public class SpringBootJwtApplication implements CommandLineRunner {

	@Autowired
	UploadFileServiceImpl uploadSerice;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootJwtApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		uploadSerice.deleteAll();
		uploadSerice.init();
		String encodePass = passwordEncoder.encode("123");
		System.out.println("Encriptado: " +encodePass );
		
	}
}
