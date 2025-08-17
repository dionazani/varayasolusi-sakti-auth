package org.varayasolusi.saktiauth.infrastructure.model;

import lombok.Data;

@Data
public class SignUpDescModel {
	
	private String fullname;
	private String email;
	private String mobilePhone;
	private String gender;
	private String password;
}
