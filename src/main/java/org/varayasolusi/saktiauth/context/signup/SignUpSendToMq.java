package org.varayasolusi.saktiauth.context.signup;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.varayasolusi.saktiauth.infrastructure.model.EmailModel;

@Service
public class SignUpSendToMq {

	@Autowired
	private AmqpTemplate rabbitTemplate;
	
	@Value("${rabbitmq.saktiauth.exchange-direct}")
	String saktiAuthExchange;

	@Value("${rabbitmq.saktiauth.signup-email.routing-key}")
	private String saktiAuthQueueRoutingKey;
	
	public void send(EmailModel emailModel) {
		System.out.println("Send model = " + emailModel);
		rabbitTemplate.convertAndSend(saktiAuthExchange, saktiAuthQueueRoutingKey, emailModel);
	}
}
