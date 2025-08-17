package org.varayasolusi.saktiauth.infrastructure.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.varayasolusi.saktiauth.infrastructure.entity.AppUserAuthenticatedEntity;

@Repository
public interface AppUserAuthenticatedRepository extends JpaRepository<AppUserAuthenticatedEntity, UUID>{

	void deleteByAppUserId(UUID appUserId);
}
