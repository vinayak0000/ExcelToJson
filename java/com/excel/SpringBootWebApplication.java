package com.excel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;





@SpringBootApplication
@ComponentScan({ "com.excel"})
public class SpringBootWebApplication extends SpringBootServletInitializer {
 public static void main(String[] args) {
  SpringApplication.run(SpringBootWebApplication.class, args);
 }
 
 @Override
 protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
  return application.sources(SpringBootWebApplication.class);
 }
}