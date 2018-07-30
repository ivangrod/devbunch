package com.devbunch.searchapi.config

import org.springframework.context.annotation.Configuration
import io.searchbox.client.JestClientFactory
import org.springframework.beans.factory.annotation.Value
import io.gsonfire.GsonFireBuilder
import com.google.gson.Gson
import io.searchbox.client.config.HttpClientConfig
import io.searchbox.client.JestClient
import io.searchbox.client.AbstractJestClient
import org.springframework.context.annotation.Bean

@Configuration
open class SearchApiConfig {

	@Value("\${jest.elasticsearch.url}")
	private var urlConnection: String? = "localhost:9200"

	@Bean("jestClient")
	open fun jestClient(): JestClient {

		val fireBuilder = GsonFireBuilder()
		val builder = fireBuilder/*.enableExposeMethodResult()*/.createGsonBuilder()/*.excludeFieldsWithoutExposeAnnotation()*/

		val gson: Gson = builder.setDateFormat(AbstractJestClient.ELASTIC_SEARCH_DATE_FORMAT).create()
		System.out.println("Establishing JEST Connection to Elasticsearch over HTTP : [$urlConnection]")
		val factory = JestClientFactory()
		factory.setHttpClientConfig(HttpClientConfig.Builder(urlConnection)
				.multiThreaded(true)
				.readTimeout(20000)
				.gson(gson)
				.build())
		return factory.getObject()
	}
}