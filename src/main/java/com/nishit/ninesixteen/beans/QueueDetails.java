package com.nishit.ninesixteen.beans;

import com.nishit.ninesixteen.constants.FatalException;

public class QueueDetails {

	private static QueueDetails queueDetails;

	private String queueUrl;

	private QueueDetails(String queueUrl) {
		this.queueUrl = queueUrl;
	}
	
	public String getQueueUrl() {
		return this.queueUrl;
	}

	public static QueueDetails getQueueDetailsInstance() {
		if (queueDetails == null) {
			System.err.println(
					"QueueDetails singleton should be created at application launch. The object is null. Exiting the system");
			throw new FatalException(
					"QueueDetails singleton should be created at application launch. The object is null. Exiting the system");
		}
		return queueDetails;
	}

	public static void createQueueDetailsInstance(final String queueUrl) {
		if (queueDetails == null) {
			synchronized (QueueDetails.class) {
				if (queueDetails == null) {
					queueDetails = new QueueDetails(queueUrl);
				}
			}
		}
	}
}
