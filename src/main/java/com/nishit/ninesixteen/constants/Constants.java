package com.nishit.ninesixteen.constants;

import org.springframework.web.client.RestTemplate;

public class Constants {

	public static final String SQS_QUEUE_NAME = "holding-queue";

	public static final String ACCESS_KEY = "x";
	public static final String SECRET_KEY = "x";

	public static final String END_POINT = "http://localhost:9324";
	public static final String REGION = "elasticmq";
	
	public static final int MAX_MESSAGES_TO_RECEIVE = 1;
	
	public static final int TOKEN_COUNT = 1;
	
	public static final String URL = "https://postman-echo.com/post";
	
	public static final int MIN_QUEUE_THREADS = 4;
	public static final int MAX_QUEUE_THREADS = 8;
	public static final int QUEUE_CAPACITY = 1000;
	
	//This is thread safe, so can be re-used across and everywhere.
	public static final RestTemplate REST_TEMPLATE = new RestTemplate();
}
