package com.nishit.ninesixteen.constants;

import static com.nishit.ninesixteen.constants.Constants.ACCESS_KEY;
import static com.nishit.ninesixteen.constants.Constants.END_POINT;
import static com.nishit.ninesixteen.constants.Constants.MAX_MESSAGES_TO_RECEIVE;
import static com.nishit.ninesixteen.constants.Constants.REGION;
import static com.nishit.ninesixteen.constants.Constants.SECRET_KEY;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.nishit.ninesixteen.beans.QueueDetails;

public class Utils {

	private static int MESSAGE_VISIBILITY_TIMEOUT = 43200; // 12 hours

	public static ReceiveMessageRequest receiveMessageRequest() {
		String queueUrl = QueueDetails.getQueueDetailsInstance().getQueueUrl();
		ReceiveMessageRequest request = new ReceiveMessageRequest(queueUrl);
		request.setMaxNumberOfMessages(MAX_MESSAGES_TO_RECEIVE);
		return request;
	}

	public static DeleteMessageRequest deleteMessageRequest(Message message) {
		String queueUrl = QueueDetails.getQueueDetailsInstance().getQueueUrl();
		DeleteMessageRequest request = new DeleteMessageRequest(queueUrl, message.getReceiptHandle());
		return request;
	}

	public static ChangeMessageVisibilityRequest changeMessageVisibilityRequest(Message message) {

		return new ChangeMessageVisibilityRequest(QueueDetails.getQueueDetailsInstance().getQueueUrl(),
				message.getReceiptHandle(), MESSAGE_VISIBILITY_TIMEOUT);
	}

	public static AmazonSQS getAmazonSQSClient() {
		return AmazonSQSClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY)))
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(END_POINT, REGION)).build();
	}

}
