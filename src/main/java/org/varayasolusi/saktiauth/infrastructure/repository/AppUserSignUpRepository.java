package org.varayasolusi.saktiauth.infrastructure.repository;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.varayasolusi.saktiauth.infrastructure.entity.AppUserSignupEntity;

public interface AppUserSignUpRepository extends CrudRepository<AppUserSignupEntity, UUID>{

	AppUserSignupEntity findBySignUpId(UUID signUpId);
}
