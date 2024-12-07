package org.varayasolusi.saktiauth.context.v1.login;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name="login.AppUserPersonEntityCustom")
public class AppUserPersonEntityCustom {

	@Id
	private String appUserId;
	private String appUserPersonId;
	private String email;
	private String password;
}
