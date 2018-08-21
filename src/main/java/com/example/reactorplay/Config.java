package com.example.reactorplay;

import com.example.aggregator.Aggregator;
import com.example.aggregator.Fetcher;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class Config
{

	@Bean
	public Aggregator aggregator()
	{
		return new Aggregator();
	}

	@Bean
	public WebClient client()
	{
		return WebClient.builder()
			.baseUrl("http://localhost:8080")
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.build();
	}

	@Bean
	public Fetcher fetcher(WebClient client)
	{
		return new Fetcher(client);
	}

}
