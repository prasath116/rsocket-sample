package prs.service.notification.controller;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import prs.schema.NotificationRequest;
import prs.schema.NotificationResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller responsible for generating sample notifications.
 */
@Controller
public class NotificationController {
	private static final Logger LOG = LoggerFactory.getLogger(NotificationController.class);

	@MessageMapping("randomNotification")
	public Mono<NotificationResponse> randomNotification() {
		return Mono.fromSupplier(() -> new NotificationResponse(1, false, "Generated Notification: " + 1));
	}

	@MessageMapping("randomNotifications")
	public Flux<NotificationResponse> randomNotifications(NotificationRequest request) {
		return Flux.range(1, request.getLimit())
				.delayElements(Duration.ofSeconds(request.getIntervalInSeconds())).map(i -> {
					LOG.info("Generated Notification : {}", i);
					return new NotificationResponse(i, false, "Generated Notification : " + i);
				});
	}
}
