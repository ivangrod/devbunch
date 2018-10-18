package com.devbunch.searchapi.model

import io.gsonfire.annotations.ExposeMethodResult
import java.io.Serializable
import com.google.gson.annotations.Expose

data class FeedItem(val generatedId: String, val title: String, val uri: String, val creator: String, val origin: String, val topics: Set<String>) : Serializable 
