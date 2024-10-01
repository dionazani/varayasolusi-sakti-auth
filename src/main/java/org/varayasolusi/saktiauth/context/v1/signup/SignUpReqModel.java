package org.varayasolusi.saktiauth.context.v1.signup;

import lombok.Data;

@Data
public class SignUpReqModel {

	private String id;
	private String fullname;
	private String email;
	private String mobilePhone;
	private String gender;
	private String password;
	private String createdAt;
}
