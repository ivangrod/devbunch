package com.devbunch.feedcollector;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.env.StandardEnvironment;

@SpringBootApplication
public class FeedCollectorApplication {
	
	private static final String DEFAULT_ENVIRONMENT = "local";
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(FeedCollectorApplication.class).environment( new StandardEnvironment() {
	        
			@Override
	        public String[] getActiveProfiles() {
	            final String systemEnvironmentVar = System.getenv("ENV");
	            final String environmentStr = systemEnvironmentVar == null ? DEFAULT_ENVIRONMENT : systemEnvironmentVar;
	            return new String[] { environmentStr };
	        }
	    }).run(args);
	}
}
