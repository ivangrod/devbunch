package com.devbunch.graphapi.model

import org.neo4j.ogm.annotation.GeneratedValue
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship

@NodeEntity
data class Topic(

		@Id @GeneratedValue val id: Long,
		val name: String,
		@Relationship(type = "TAG_WITH", direction = Relationship.INCOMING)
		val tags: List<Tag>
)