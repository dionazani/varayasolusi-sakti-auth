package org.varayasolusi.saktiauth.infrastructure.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.varayasolusi.saktiauth.infrastructure.entity.AppUserLoginEntity;
import java.util.UUID;

@Repository
public interface AppUserLoginRepository extends CrudRepository<AppUserLoginEntity, UUID>{

}
