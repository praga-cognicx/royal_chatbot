package com.royal.app.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.royal")
@EnableScheduling
public class ApplicationConfiguration extends SpringBootServletInitializer{

  public static void main(String[] args) {
    SpringApplication.run(ApplicationConfiguration.class, args);
  }
}
