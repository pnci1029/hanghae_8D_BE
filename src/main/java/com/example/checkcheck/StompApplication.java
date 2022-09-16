package com.example.checkcheck;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.persistence.EntityManager;

@EnableJpaAuditing
@SpringBootApplication
public class StompApplication {

	public static final String APPLICATION_LOCATIONS = "spring.config.location="
			+ "classpath:application.yml,"
			+ "/app/config/springboot-webservice/real-application.yml";

	public static void main(String[] args) {
		new SpringApplicationBuilder(StompApplication.class)
				.properties(APPLICATION_LOCATIONS)
				.run(args);

	}

//	public static void main(String[] args) {
//		new SpringApplicationBuilder(StompApplication.class)
//				.properties(APPLICATION_LOCATIONS)
//				.run(args);
//
//	}
	@Bean
	JPAQueryFactory jpaQueryFactory(EntityManager em) {
		return new JPAQueryFactory(em);
	}

}
