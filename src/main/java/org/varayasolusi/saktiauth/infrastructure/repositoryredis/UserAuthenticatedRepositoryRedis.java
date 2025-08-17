package org.varayasolusi.saktiauth.infrastructure.repositoryredis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.varayasolusi.saktiauth.infrastructure.entityredis.UserAuthenticatedEntityRedis;

import java.util.Optional;

@Repository
public interface UserAuthenticatedRepositoryRedis extends CrudRepository<UserAuthenticatedEntityRedis, String>{

    // Spring Data Redis will generate this automatically
    Optional<UserAuthenticatedEntityRedis> findByAuthenticatedId(String authenticatedId);
}
