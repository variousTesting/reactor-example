package com.example.web;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/service")
public class ServiceEndpoint
{
	private final static boolean      DELAY_ON = false;
	private final static ObjectMapper MAPPER   = new ObjectMapper();

	@RequestMapping(path = "/1/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Mono<String> service1(@PathVariable("id") String id) throws JsonProcessingException
	{

		final Map<String, Object> map = new HashMap<>();
		map.put("service1", id);

		sleep(1);
		//throw new RuntimeException();

		return Mono.just(MAPPER.writeValueAsString(map));
	}

	private Mono<String> delay(Mono<String> mono, int seconds)
	{
		return DELAY_ON
		       ? mono.delayElement(Duration.ofSeconds(seconds))
		       : mono;
	}

	@RequestMapping(path = "/2/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Mono<String> service2(@PathVariable("id") String id) throws JsonProcessingException
	{
		final Map<String, Object> map = new HashMap<>();
		map.put("service2", id);
		map.put("price", "34");
		map.put("subType", "E56");
		sleep(3);

		return Mono.just(MAPPER.writeValueAsString(map));
	}


	@GetMapping(path = "/3", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Mono<String> service3() throws JsonProcessingException
	{
		final Map<String, Object> map = new HashMap<>();
		map.put("service3", "somethingSmat");
		map.put("users", Collections.singleton("milan"));
		map.put("roles", "Developer");

		sleep(5);

		return delay(Mono.just(MAPPER.writeValueAsString(map)), 10);
	}


	private void sleep(int seconds)
	{
		try
		{
			if (DELAY_ON)
				Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}


}
