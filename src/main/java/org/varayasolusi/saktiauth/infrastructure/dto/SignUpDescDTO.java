package org.varayasolusi.saktiauth.infrastructure.dto;

import lombok.Data;

@Data
public class SignUpDescDTO {
	
	private String fullname;
	private String email;
	private String mobilePhone;
	private String gender;
	private String password;
}
