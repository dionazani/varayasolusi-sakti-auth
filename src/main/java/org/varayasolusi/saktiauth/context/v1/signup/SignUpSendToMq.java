package org.varayasolusi.saktiauth.context.v1.signup;

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
	String exchangeDirect;

	@Value("${rabbitmq.saktiauth.signup-email.routing-key}")
	private String signUpEmailRoutingKey;
	
	public void send(EmailModel emailModel) {
		System.out.println("Send model = " + emailModel);
		rabbitTemplate.convertAndSend(exchangeDirect, signUpEmailRoutingKey, emailModel);
	}
}
