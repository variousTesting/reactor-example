package com.example.aggregator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

public class Aggregator
{
	private final static ObjectMapper MAPPER = new ObjectMapper();

	public Mono<? extends String> aggregate(JsonNode service1, JsonNode service2, JsonNode service3)
	{
		try
		{
			final JsonNode json12   = MAPPER.updateValue(service1, service2);
			final JsonNode solution = MAPPER.updateValue(json12, service3);
			return Mono.just(MAPPER.writeValueAsString(solution));
		}
		catch (JsonProcessingException ex)
		{
			throw new RuntimeException(ex);
		}
	}

}
