package org.varayasolusi.saktiauth.infrastructure.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.varayasolusi.saktiauth.infrastructure.entity.ApplicationTypeEntity;

public interface ApplicationTypeRepository extends JpaRepository<ApplicationTypeEntity, UUID>{

}
