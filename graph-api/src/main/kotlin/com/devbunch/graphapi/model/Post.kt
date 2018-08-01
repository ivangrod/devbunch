package com.devbunch.graphapi.model

import org.neo4j.ogm.annotation.GeneratedValue
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship
import java.util.Date

@NodeEntity
data class Post(
		@Id @GeneratedValue val id: Long,

		val generatedId: String,

		val uri: String,

		val origin: String,

		val collectTimestamp: Date,

		val publicationTimestamp: Date,

		@Relationship(type = "TAG_WITH", direction = Relationship.OUTGOING)
		val tags: List<Tag>)