package com.example.aggregator;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

public class Fetcher
{
	private final static ObjectMapper MAPPER = new ObjectMapper();

	private final WebClient client;

	@Autowired
	public Fetcher(WebClient client)
	{
		this.client = client;
	}


	public Mono<JsonNode> callService1()
	{
		return client.get().uri("/service/1/demo")
			.retrieve().bodyToMono(JsonNode.class).onErrorResume(throwable -> {
				System.out.println("Error when calling service1!");
				try
				{
					return Mono.just(MAPPER.readTree("{}"));
				}
				catch (IOException e)
				{
					e.printStackTrace();
					return Mono.empty();
				}
			});
	}

	public Mono<JsonNode> callService2()
	{
		return client.get().uri("/service/2/demo2")
			.retrieve().bodyToMono(JsonNode.class);
	}

	public Mono<JsonNode> callService3()
	{
		return client.get().uri("/service/3/")
			.retrieve().bodyToMono(JsonNode.class);
	}
}
