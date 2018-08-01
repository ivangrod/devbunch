package com.devbunch.graphapi.controller

import com.devbunch.graphapi.repository.TopicRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/topic")
class TopicController(private val repository: TopicRepository) {
	

	@GetMapping("/")
	fun findAll() = repository.findAll()
		
}