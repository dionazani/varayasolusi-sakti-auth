package org.varayasolusi.saktiauth.infrastructure.repositoryredis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.varayasolusi.saktiauth.infrastructure.entityredis.UserLoginEntityRedis;

@Repository
public interface UserLoginRepositoryRedis extends CrudRepository<UserLoginEntityRedis, String>{

}
