package test.sunil.aws.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class ProduceMessage {
	
	@Autowired
	QueueMessagingTemplate sqsTemplate;
	
	@Value("${cloud.aws.end-point.uri}")
	private String sqsEndPoint;
	
	@GetMapping
	public String produceMessage(String message) {
		sqsTemplate.send(sqsEndPoint, MessageBuilder.withPayload("A Sunil Test message").build());
		return "Completed";
	}

}
