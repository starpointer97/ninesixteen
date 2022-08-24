package com.nishit.ninesixteen.main;

import static com.nishit.ninesixteen.constants.Constants.MAX_QUEUE_THREADS;
import static com.nishit.ninesixteen.constants.Constants.MIN_QUEUE_THREADS;
import static com.nishit.ninesixteen.constants.Constants.QUEUE_CAPACITY;
import static com.nishit.ninesixteen.constants.Constants.SQS_QUEUE_NAME;
import static com.nishit.ninesixteen.constants.Utils.getAmazonSQSClient;

import java.util.concurrent.Executor;

import org.elasticmq.rest.sqs.SQSRestServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.nishit.ninesixteen.beans.QueueDetails;
import com.nishit.ninesixteen.service.ElasticMQListenerImpl;

@EnableAsync
@SpringBootApplication
@ComponentScan(basePackages = "com.nishit")
@EnableCaching(proxyTargetClass = true)
public class Launcher {

	public static void main(String[] args) {
		if (createQueue()) {
			SpringApplication.run(Launcher.class, args);
		}
	}

	private static boolean createQueue() {
		SQSRestServerBuilder.start();

		try {
			CreateQueueResult result = getAmazonSQSClient().createQueue(SQS_QUEUE_NAME);
			System.out.println("Created the queue : " + SQS_QUEUE_NAME + " with the url : " + result.getQueueUrl());
			QueueDetails.createQueueDetailsInstance(result.getQueueUrl());
			return true;
		} catch (Exception exception) {
			System.err.println("Failed to create the queue, exiting the application" + exception);
		}
		return false;
	}

	@Bean(value = "listenerBean")
	public Executor listenerTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(MIN_QUEUE_THREADS);
		executor.setMaxPoolSize(MAX_QUEUE_THREADS);
		executor.setQueueCapacity(QUEUE_CAPACITY);
		executor.setThreadNamePrefix("nishit-ninesixteen-listenerBean");
		executor.setBeanName("listenerBean");
		executor.setDaemon(true);
		executor.initialize();
		
		for (int i = 0; i < MIN_QUEUE_THREADS; i++) {
			executor.execute(new ElasticMQListenerImpl());
		}
		return executor;
	}

	@Bean(value = "serviceBean")
	public Executor serviceTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(MIN_QUEUE_THREADS);
		executor.setQueueCapacity(QUEUE_CAPACITY);
		executor.setThreadNamePrefix("nishit-ninesixteen-serviceBean");
		executor.setBeanName("serviceBean");
		executor.setDaemon(true);
		executor.initialize();

		return executor;
	}
}
