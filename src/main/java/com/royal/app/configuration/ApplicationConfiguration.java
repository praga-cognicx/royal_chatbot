package com.royal.app.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.royal")
@EnableScheduling
//@EnableRedisHttpSession
public class ApplicationConfiguration extends SpringBootServletInitializer{

  public static void main(String[] args) {
    SpringApplication.run(ApplicationConfiguration.class, args);
  }
}
