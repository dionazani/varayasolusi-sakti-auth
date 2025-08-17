package org.varayasolusi.saktiauth.context.v1.emailverification;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailVerificationSendToMq {

    @Autowired
	private AmqpTemplate rabbitTemplate;

    @Value("${rabbitmq.saktiauth.exchange-direct}")
	String saktiAuthExchangeDirect;

	@Value("${rabbitmq.saktiauth.email-verification.routing-key}")
	private String saktiAuthEmailVerificationRoutingKey;
	
	public void send(EmailVerificationMqModel model) {
		System.out.println("Send EmailVerificationMqModel = " + model);
		rabbitTemplate.convertAndSend(saktiAuthExchangeDirect, saktiAuthEmailVerificationRoutingKey, model);
	}
}
