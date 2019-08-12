package test.sunil.aws.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;

import com.amazonaws.services.sqs.AmazonSQSAsync;

public class SqsQueueSender {

	private final QueueMessagingTemplate queueMessagingTemplate;

	@Autowired
	public SqsQueueSender(AmazonSQSAsync amazonSqs) {
		this.queueMessagingTemplate = new QueueMessagingTemplate(amazonSqs);
	}

	public void send(String message) {
		this.queueMessagingTemplate.send("https://sqs.us-east-1.amazonaws.com/555064264705/dh-minions-job-dev", MessageBuilder.withPayload(message).build());
	}
}
