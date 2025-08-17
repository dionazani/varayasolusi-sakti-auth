package org.varayasolusi.saktiauth.context.v1.login;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class LoginRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    public AppUserAuthenticationEntityCustom getAppUserByEmail(String email) {

        String sql = "select app_user.id  as app_user_id, \n"
    		+ "			app_user_login.id  as app_user_login_id,\n"
                + "		app_user.email,\n"
                + "		app_user_login.\"password\"\n"
                + "from app_user_login inner join app_user on app_user.id = app_user_login.app_user_id\n"
                + "where app_user.email = :appPersonEmail";

        var appUserPersonEntityCustom = (AppUserAuthenticationEntityCustom) this.em.createNativeQuery(sql, AppUserAuthenticationEntityCustom.class)
                .setParameter("appPersonEmail", email)
                .getSingleResult();

        return appUserPersonEntityCustom;
    }
}
