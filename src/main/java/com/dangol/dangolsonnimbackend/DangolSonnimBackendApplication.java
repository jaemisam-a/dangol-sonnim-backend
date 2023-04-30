package com.dangol.dangolsonnimbackend;

import com.dangol.dangolsonnimbackend.oauth.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
@EnableWebSecurity
@ComponentScan({ "com.dangol.dangolsonnimbackend.*"})
public class DangolSonnimBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DangolSonnimBackendApplication.class, args);
	}

}
