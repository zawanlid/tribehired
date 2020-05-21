package com.tribehired.postsapi.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.tribehired.postsapi.client.RestClient;

@EnableAutoConfiguration(exclude = { FlywayAutoConfiguration.class })
@Configuration
@Import({ RestClient.class })
@ComponentScan({ "com.tribehired.postsapi.client", "com.tribehired.postsapi.controller" })
@SpringBootApplication
public class PostsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostsApiApplication.class, args);
	}

}
