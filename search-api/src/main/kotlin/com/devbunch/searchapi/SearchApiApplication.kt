package com.devbunch.searchapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.SpringApplication

@SpringBootApplication
open class SearchApiApplication

fun main(args: Array<String>) {
     SpringApplication.run(SearchApiApplication::class.java, *args)
}
