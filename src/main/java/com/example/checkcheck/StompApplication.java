package com.example.checkcheck;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.persistence.EntityManager;

@EnableJpaAuditing
@SpringBootApplication
public class StompApplication {

	public static void main(String[] args) {
		SpringApplication.run(StompApplication.class, args);

	}
	@Bean
	JPAQueryFactory jpaQueryFactory(EntityManager em) {
		return new JPAQueryFactory(em);
	}

}
