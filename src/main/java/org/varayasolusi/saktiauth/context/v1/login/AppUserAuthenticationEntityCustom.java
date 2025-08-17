package org.varayasolusi.saktiauth.context.v1.login;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name="login.AppUserAuthenticationEntityCustom")
public class AppUserAuthenticationEntityCustom {
    @Id
    private String appUserId;
    private String appUserLoginId;
    private String email;
    private String password;
}
