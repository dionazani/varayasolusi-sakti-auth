package org.varayasolusi.saktiauth.context.v1.emailverification;

import lombok.Data;

@Data
public class EmailVerificationMqModel {

	private String id;
	private String userPersonStatus;
	private String sendAt;
	
}
