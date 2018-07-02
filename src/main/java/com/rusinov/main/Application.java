package com.rusinov.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(ApplicationConfig.class)
public class Application {

	public static final String USER = "admin";
	public static final String PASSWORD = "admin";
	public static final String ROOT = "downloads/";
	public static final String ZAMUNDA_DOWNLOAD_PATH = "http://zamunda.net/download.php/";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
