package com.devbunch.graphmodel;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.env.StandardEnvironment;

@SpringBootApplication
public class GraphModelApplication {

	private static final String DEFAULT_ENVIRONMENT = "local";

	public static void main(String[] args) {
		new SpringApplicationBuilder(GraphModelApplication.class).environment(new StandardEnvironment() {

			@Override
			public String[] getActiveProfiles() {
				final String systemEnvironmentVar = System.getenv("ENV");
				final String environmentStr = systemEnvironmentVar == null ? DEFAULT_ENVIRONMENT : systemEnvironmentVar;
				return new String[] { environmentStr };
			}
		}).run(args);
	}
}
