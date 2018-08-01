package com.devbunch.graphapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GraphApiApplication

fun main(args: Array<String>) {
    runApplication<GraphApiApplication>(*args)
}
