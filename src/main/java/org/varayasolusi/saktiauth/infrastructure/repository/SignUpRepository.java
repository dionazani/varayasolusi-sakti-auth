package org.varayasolusi.saktiauth.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.varayasolusi.saktiauth.infrastructure.entity.SignUpEntity;

@Repository
public interface SignUpRepository extends CrudRepository<SignUpEntity, UUID>{

}
