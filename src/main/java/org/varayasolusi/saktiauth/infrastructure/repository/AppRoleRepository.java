package org.varayasolusi.saktiauth.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.varayasolusi.saktiauth.infrastructure.entity.AppRoleEntity;

import java.util.UUID;

public interface AppRoleRepository extends JpaRepository<AppRoleEntity, UUID> {
}
