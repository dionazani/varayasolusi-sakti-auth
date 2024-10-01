package org.varayasolusi.saktiauth.infrastructure.repository;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.varayasolusi.saktiauth.infrastructure.entity.AppUserSignUpEntity;

public interface AppUserSignUpRepository extends CrudRepository<AppUserSignUpEntity, UUID>{

	AppUserSignUpEntity findBySignUpId(UUID signUpId);
}
