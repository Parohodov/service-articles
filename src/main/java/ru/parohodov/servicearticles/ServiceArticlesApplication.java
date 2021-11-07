package ru.parohodov.servicearticles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.parohodov.servicearticles.config.StorageProperties;

@SpringBootApplication
@EnableJpaRepositories
@EnableConfigurationProperties(StorageProperties.class)
public class ServiceArticlesApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServiceArticlesApplication.class, args);
	}
}
