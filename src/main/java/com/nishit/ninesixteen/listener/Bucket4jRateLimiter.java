package com.nishit.ninesixteen.listener;

import static com.nishit.ninesixteen.constants.Constants.TOKEN_COUNT;

import java.time.Duration;

import org.springframework.stereotype.Component;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

@Component
public class Bucket4jRateLimiter {

	// @Value("${service.ratelimit:100}")
	private static int throttleRate = 5;

	// @Value("${service.thrrotleDuration:1")
	private static int refillDuration = 1;

	private static final Refill refill = Refill.intervally(throttleRate, Duration.ofMinutes(refillDuration));

	private static final Bandwidth limit = Bandwidth.classic(5, refill);

	private static Bucket BUCKET = Bucket4j.builder().addLimit(limit).build();

	public static boolean isAPICallAllowed() {
		System.out.println("Available tokens are  : " + BUCKET.getAvailableTokens() + " called from thread : "
				+ Thread.currentThread().getName());
		return BUCKET.tryConsume(TOKEN_COUNT);
	}
}