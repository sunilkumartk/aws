package test.sunil.aws.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleResult;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;

@Configuration
public class SqsConfig {
	@Value("${cloud.aws.credentials.accessKey}")
	String accessKey;
	
	@Value("${cloud.aws.credentials.secretKey}")
	String secretKey;
	
	@Value("${cloud.aws.credentials.role}")
	String roleName;
	
	@Value("${cloud.aws.credentials.profile}")
	String profileName;
	
	@Value("${cloud.aws.credentials.session}")
	String sessionName;
	
	@Bean
	public QueueMessagingTemplate queueMessaginTemplate() {
		return new QueueMessagingTemplate(amazonSqsAsync());
	}

	private AmazonSQSAsync amazonSqsAsync() {
		
		//return AmazonSQSAsyncClientBuilder.standard().withRegion(Regions.US_EAST_1)
		//		.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey))).build();
		
		final AWSSecurityTokenServiceClient stsClient = new AWSSecurityTokenServiceClient(new ProfileCredentialsProvider(profileName));
		
		final AssumeRoleRequest assumeRequest = new AssumeRoleRequest()
												.withRoleArn(roleName)
									            .withDurationSeconds(3600)
									            .withRoleSessionName(sessionName);

	    final AssumeRoleResult assumeResult = stsClient.assumeRole(assumeRequest);

	    final BasicSessionCredentials temporaryCredentials = new BasicSessionCredentials(
                assumeResult.getCredentials()
                            .getAccessKeyId(), assumeResult.getCredentials().getSecretAccessKey(),
                assumeResult.getCredentials().getSessionToken());
	    
		return AmazonSQSAsyncClientBuilder.standard().withRegion(Regions.US_EAST_1)
				.withCredentials(new AWSStaticCredentialsProvider(temporaryCredentials)).build();
	}

}
