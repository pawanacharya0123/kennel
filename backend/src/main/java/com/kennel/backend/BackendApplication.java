package com.kennel.backend;

import com.kennel.backend.entity.enums.RoleName;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication
//		implements ApplicationRunner
{
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

//	@Override
//	public void run(ApplicationArguments args) throws Exception {
//		System.out.println("Hello World from Application Runner");
//	}
}
