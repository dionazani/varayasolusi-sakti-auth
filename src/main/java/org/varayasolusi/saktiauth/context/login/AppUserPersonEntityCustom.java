package org.varayasolusi.saktiauth.context.login;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name="login.AppUserPersonEntityCustom")
public class AppUserPersonEntityCustom {

	@Id
	private String appUserPersonId;
	private String appUserId;
	private String email;
	private String password;
}
