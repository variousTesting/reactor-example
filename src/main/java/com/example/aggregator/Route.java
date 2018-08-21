package com.example.aggregator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/demo")
public class Route
{

	private final Fetcher    fetcher;
	private final Aggregator aggregator;

	@Autowired
	public Route(Fetcher fetcher, Aggregator aggregator)
	{
		this.fetcher = fetcher;
		this.aggregator = aggregator;
	}


	@RequestMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Mono<String> aggregate()
	{
		return Mono.zip(fetcher.callService1(), fetcher.callService2(), fetcher.callService3())
			.flatMap(objects -> aggregator.aggregate(objects.getT1(), objects.getT2(), objects.getT3()));
	}


}
