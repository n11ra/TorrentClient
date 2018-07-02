package com.rusinov.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(ApplicationConfig.class)
public class Application {

	public static final String DOWNLOAD_DIR = "./downloads/";
	public static final String ZAMUNDA_DOWNLOAD_PATH = "http://zamunda.net/magnetlink/download_go.php?m=x&id=";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
