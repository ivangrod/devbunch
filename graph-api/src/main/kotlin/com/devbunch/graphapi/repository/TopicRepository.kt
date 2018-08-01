package com.devbunch.graphapi.repository

import com.devbunch.graphapi.model.Topic
import org.springframework.data.repository.CrudRepository

interface TopicRepository : CrudRepository<Topic, Long> {
}