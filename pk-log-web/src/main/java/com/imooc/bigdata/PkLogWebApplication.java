package com.imooc.bigdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class PkLogWebApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(PkLogWebApplication.class);
		app.run(args);
	}

}
