package org.varayasolusi.saktiauth.infrastructure.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.varayasolusi.saktiauth.infrastructure.entity.AppUserEntity;

@Repository
public interface AppUserRepository extends JpaRepository<AppUserEntity, UUID>{

	@Query(value="SELECT au FROM AppUserEntity au WHERE au.email = :email")
	AppUserEntity findByEmail(@Param("email") String email);

}
