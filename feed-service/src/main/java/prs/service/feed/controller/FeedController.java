package prs.service.feed.controller;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import prs.schema.FeedRequest;
import prs.schema.FeedResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller responsible for generating sample feeds.
 */
@Controller
public class FeedController {
	private static final Logger LOG = LoggerFactory.getLogger(FeedController.class);

	@MessageMapping("sampleFeed")
	public Mono<FeedResponse> sampleFeed() {
		return Mono.just(new FeedResponse(1, "New Feed : " + 1));
	}

	@MessageMapping("sampleFeeds")
	public Flux<FeedResponse> sampleFeeds(FeedRequest request) {

		return Flux.range(1, request.getLimit())
				.delayElements(Duration.ofSeconds(request.getIntervalInSeconds())).map(i -> {
					LOG.info("New Feed : {}", i);
					return new FeedResponse(i, "New Feed : " + i);
				});
	
	}
}
