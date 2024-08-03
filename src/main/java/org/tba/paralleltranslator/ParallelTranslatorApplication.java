package org.tba.paralleltranslator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableAsync
public class ParallelTranslatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParallelTranslatorApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public ExecutorService executorService() {
		return Executors.newFixedThreadPool(10);
	}
}
