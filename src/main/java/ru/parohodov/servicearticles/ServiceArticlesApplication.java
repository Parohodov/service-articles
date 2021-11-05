package ru.parohodov.servicearticles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class ServiceArticlesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceArticlesApplication.class, args);
	}

}
