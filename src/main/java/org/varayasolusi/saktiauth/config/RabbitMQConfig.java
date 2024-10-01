package org.varayasolusi.saktiauth.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	@Value("${rabbitmq.saktiauth.exchange-direct}")
	String exchangeDirect;

	@Value("${rabbitmq.saktiauth.signup-email.routing-key}")
	private String signUpEmailRoutingKey;
	
	@Value("${rabbitmq.saktiauth.signup-email.queue}")
	String signUpEmailQueue;

	@Bean
	Queue signUpEmailQueue() {
		return new Queue(signUpEmailQueue, false);
	}

	@Bean
	DirectExchange exchangeDirect() {
		return new DirectExchange(exchangeDirect);
	}

	@Bean
	MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
	@Bean
	Binding bindingSignUpEmailRoutingKey() {
		return BindingBuilder.bind(signUpEmailQueue()).to(exchangeDirect()).with(signUpEmailRoutingKey);
	}
	
	@Bean
	AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;

	}

}
