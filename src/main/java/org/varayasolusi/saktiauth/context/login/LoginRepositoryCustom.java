package org.varayasolusi.saktiauth.context.login;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class LoginRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    public AppUserPersonEntityCustom getAppUserByEmail(String email) {

        String sql = "select app_user_person.id  as app_user_person_id,\n"
                + "		app_user.id  as app_user_id,\n"
                + "		app_user.email,\n"
                + "		app_user_person.\"password\"\n"
                + "from app_user_person inner join app_user on app_user.id = app_user_person.app_user_id\n"
                + "where app_user.email = :appPersonEmail";

        var appUserPersonEntityCustom = (AppUserPersonEntityCustom) this.em.createNativeQuery(sql, AppUserPersonEntityCustom.class)
                .setParameter("appPersonEmail", email)
                .getSingleResult();

        return appUserPersonEntityCustom;
    }
}
