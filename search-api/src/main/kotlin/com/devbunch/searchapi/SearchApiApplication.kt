package com.devbunch.searchapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.SpringApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.core.env.StandardEnvironment

@SpringBootApplication
class SearchApiApplication

	val DEFAULT_ENVIRONMENT = "local"

	fun main(args: Array<String>) {
	     SpringApplicationBuilder(SearchApiApplication::class.java).environment(object : StandardEnvironment() {

			public override fun getActiveProfiles() : Array<String> {
				var systemEnvironmentVar: String? = System.getenv("ENV")
				val environmentStr = if ( systemEnvironmentVar == null ) DEFAULT_ENVIRONMENT else systemEnvironmentVar
				return arrayOf(environmentStr)
			}
		}).run(*args)
	}
