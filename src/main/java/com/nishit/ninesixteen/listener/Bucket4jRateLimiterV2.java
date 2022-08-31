package com.nishit.ninesixteen.listener;

import static com.nishit.ninesixteen.constants.Constants.TOKEN_COUNT;

import java.time.Duration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

@Component
public class Bucket4jRateLimiterV2 {

	@Value("${ratelimit:100}")
	private int throttleRate;

	@Value("${thrrotleDuration:1}")
	private int refillDuration;

	private Refill refill;

	private Bandwidth limit;

	private Bucket BUCKET;
	
	@PostConstruct
	private void init() {
		refill = Refill.intervally(throttleRate, Duration.ofMinutes(refillDuration));
		limit = Bandwidth.classic(5, refill);
		BUCKET = Bucket4j.builder().addLimit(limit).build();
	}

	public boolean isAPICallAllowed() {
		System.out.println("Available tokens are  : " + BUCKET.getAvailableTokens() + " called from thread : "
				+ Thread.currentThread().getName());
		return BUCKET.tryConsume(TOKEN_COUNT);
	}
}
