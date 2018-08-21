package com.example.aggregator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.mockito.*;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


public class RouteTest
{
	private final static ObjectMapper MAPPER = new ObjectMapper();


	@Test
	public void test()
	{
		final Fetcher    fetcher    = mock(Fetcher.class);
		final Aggregator aggregator = mock(Aggregator.class);
		final Route      route      = new Route(fetcher, aggregator);


		final Map<String, Object> json1 = new HashMap<>();
		json1.put("test1", "test1");

		doReturn(Mono.just(toJson(json1))).when(fetcher).callService1();
		doReturn(Mono.just(emptyJson())).when(fetcher).callService2();
		doReturn(Mono.just(emptyJson())).when(fetcher).callService3();


		final String             result = "bla";
		ArgumentCaptor<JsonNode> arg1   = ArgumentCaptor.forClass(JsonNode.class);
		doReturn(Mono.just(result)).when(aggregator).aggregate(arg1.capture(), any(), any());

		StepVerifier.withVirtualTime(route::aggregate)
			.expectNext(result)
			.verifyComplete();

		final JsonNode value = arg1.getValue();
		assertThat(value).isEqualTo(toJson(json1));

	}

	private JsonNode toJson(Map<String, Object> json1)
	{

		return MAPPER.convertValue(json1, JsonNode.class);


	}

	public JsonNode emptyJson()
	{
		try
		{
			return MAPPER.readTree("{}");
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}


}
