package com.devbunch.graphapi.model

import org.neo4j.ogm.annotation.EndNode
import org.neo4j.ogm.annotation.GeneratedValue
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.StartNode

data class Tag(
		@Id @GeneratedValue val id: Long,
		val weight: Double,
		@StartNode val post: Post,

		@EndNode val topic: Topic)