package prs.client;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Component;

import prs.schema.FeedRequest;
import prs.schema.FeedResponse;
import prs.schema.NotificationRequest;
import prs.schema.NotificationResponse;
import reactor.core.publisher.Flux;

/**
 * Runs the client application.
 */
@Component
public class ClientCommandLineRunner implements CommandLineRunner {
	private static final Logger LOG = LoggerFactory.getLogger(ClientCommandLineRunner.class);

	@Autowired
	private RSocketRequester feedRequester;

	@Autowired
	private RSocketRequester notificationRequester;

	@Override
	public void run(String... args) throws Exception {
		Flux<FeedResponse> sampleFeeds = feedRequester.route("sampleFeeds")
				.data(new FeedRequest("req1", 2, 20))
				.retrieveFlux(FeedResponse.class);

		Flux<NotificationResponse> sampleNotifications = notificationRequester.route("sampleNotifications")
				.data(new NotificationRequest("req2", 20, 4))
				.retrieveFlux(NotificationResponse.class);

		sampleFeeds
			.doOnComplete(() -> LOG.info("Feeds Done!"))
			.subscribe(feedConsumer, errorConsumer);
		
		sampleNotifications
			.doOnComplete(() -> LOG.info("Notifications Done!"))
			.subscribe(notificationConsumer,errorConsumer);

		feedRequester.route("sampleFeed")
			.retrieveMono(FeedResponse.class)
			.subscribe(feedConsumer);
		
		notificationRequester.route("sampleNotification")
			.retrieveMono(NotificationResponse.class)
			.subscribe(notificationConsumer);
	}

	Consumer<FeedResponse> feedConsumer = feedResponse -> LOG.info("Feed Received: {}", feedResponse.getContent());
	Consumer<NotificationResponse> notificationConsumer = notificationResponse -> LOG.info("Notification Received: {}",
			notificationResponse.getContent());
	Consumer<Throwable> errorConsumer = Throwable::printStackTrace;
}
