package com.nishit.ninesixteen.service;

import static com.nishit.ninesixteen.constants.Constants.REST_TEMPLATE;
import static com.nishit.ninesixteen.constants.Constants.URL;
import static com.nishit.ninesixteen.constants.Utils.changeMessageVisibilityRequest;
import static com.nishit.ninesixteen.constants.Utils.deleteMessageRequest;
import static com.nishit.ninesixteen.constants.Utils.getAmazonSQSClient;
import static com.nishit.ninesixteen.constants.Utils.receiveMessageRequest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.nishit.ninesixteen.listener.Bucket4jRateLimiter;
import com.nishit.ninesixteen.listener.QueueListener;

/**
 * Long running queue listener which will exit only after processing the message
 * PS : Not the ideal way, but wanted to wrap this assignment up within 4 hours
 * 
 * @author npatira
 *
 */
@Component
public class ElasticMQListenerImpl implements QueueListener, Runnable {

	private static final int THREAD_SLEEP = 10000;

	public Optional<Message> getMessageFromQueue() {
		ReceiveMessageResult result = getAmazonSQSClient().receiveMessage(receiveMessageRequest());

		for (Message message : result.getMessages()) {
			System.out.println(message);
		}
		if (result.getMessages().size() != 0) {
			getAmazonSQSClient().changeMessageVisibility(changeMessageVisibilityRequest(result.getMessages().get(0)));
			return Optional.of(result.getMessages().get(0));
		} else {
			return Optional.empty();
		}
	}

	@Override
	//@Async("listenerBean")
	public void processMessage() {
		Optional<Message> message = getMessageFromQueue();
		if (message.isPresent()) {
			System.out.println("Message in the queue is :" + message.get().getBody());
			while (!Thread.currentThread().isInterrupted()) {
				if (isAPICallAllowed()) {
					call3rdParty(message.get().getBody());
				} else {
					try {
						Thread.sleep(THREAD_SLEEP);
					} catch (InterruptedException ie) {
						System.err.println("Thread was interrupted" + ie);
					}
				}
			}
		}
	}
	
	private void call3rdParty(String message) {
		REST_TEMPLATE.postForEntity(URL, message, Void.class);
	}

	private boolean isAPICallAllowed() {
		return Bucket4jRateLimiter.isAPICallAllowed();
	}

	@Override
	public void run() {
		System.out.println("Queue Listener threads are up");
		while (true) {
			processMessage();
		}

	}
}
