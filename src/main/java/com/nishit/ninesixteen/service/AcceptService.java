package com.nishit.ninesixteen.service;

import static com.nishit.ninesixteen.constants.Constants.URL;
import static com.nishit.ninesixteen.constants.Utils.getAmazonSQSClient;
import static com.nishit.ninesixteen.constants.Constants.REST_TEMPLATE;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.nishit.ninesixteen.beans.QueueDetails;
import com.nishit.ninesixteen.listener.Bucket4jRateLimiter;

@Service
public class AcceptService {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Async("serviceBean")
	public void processMessage(String message) {
		if (isAPICallAllowed()) {
			call3rdParty(message);
		} else {
			System.out.println("Call to 3rd party API is throttled. Writing message to Queue");
			writeToQueue(message);
		}

	}

	void call3rdParty(String message) {
		REST_TEMPLATE.postForEntity(URL, message, Void.class);
	}

	private void writeToQueue(String messageBody) {
		String queueUrl = QueueDetails.getQueueDetailsInstance().getQueueUrl();
		getAmazonSQSClient().sendMessage(new SendMessageRequest(queueUrl, messageBody));

	}

	private boolean isAPICallAllowed() {
		return Bucket4jRateLimiter.isAPICallAllowed();
	}
}
