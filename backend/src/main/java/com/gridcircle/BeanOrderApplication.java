package com.gridcircle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BeanOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeanOrderApplication.class, args);
	}

}
