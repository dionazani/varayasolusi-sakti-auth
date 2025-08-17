package org.varayasolusi.saktiauth.infrastructure.repository;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.varayasolusi.saktiauth.infrastructure.entity.AppUserVerifiedEntity;

public interface AppUserVerifiedRepository extends CrudRepository<AppUserVerifiedEntity, UUID>{

	AppUserVerifiedEntity findByAppUserId(UUID appUserId);
}
