package org.tba.paralleltranslator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The main entry point for the Parallel Translator application.
 * This class is responsible for bootstrapping the Spring Boot application.
 */
@SpringBootApplication
@EnableAsync
public class ParallelTranslatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParallelTranslatorApplication.class, args);
	}

	/**
	 * Provides a RestTemplate bean for making HTTP requests.
	 *
	 * @return a RestTemplate instance
	 */
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	/**
	 * Provides an ExecutorService bean with a fixed thread pool for handling asynchronous tasks.
	 *
	 * @return an ExecutorService instance with a thread pool of size 10
	 */
	@Bean
	public ExecutorService executorService() {
		return Executors.newFixedThreadPool(10);
	}
}
