package com.devbunch.feedcollector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FeedCollectorApplication {

  public static void main(String[] args) {
    System
        .exit(SpringApplication.exit(SpringApplication.run(FeedCollectorApplication.class, args)));
  }
}
