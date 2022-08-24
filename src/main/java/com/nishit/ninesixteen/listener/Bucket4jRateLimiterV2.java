package com.nishit.ninesixteen.listener;

import static com.nishit.ninesixteen.constants.Constants.TOKEN_COUNT;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

@Component
public class Bucket4jRateLimiterV2 {

	@Value("${service.ratelimit:100}")
	private int throttleRate=5;

	@Value("${service.thrrotleDuration:1}")
	private int refillDuration=1;

	private final Refill refill = Refill.intervally(throttleRate, Duration.ofMinutes(refillDuration));

	private final Bandwidth limit = Bandwidth.classic(5, refill);

	private Bucket BUCKET = Bucket4j.builder().addLimit(limit).build();

	public boolean isAPICallAllowed() {
		System.out.println("Available tokens are  : " + BUCKET.getAvailableTokens() + " called from thread : "
				+ Thread.currentThread().getName());
		return BUCKET.tryConsume(TOKEN_COUNT);
	}
}
