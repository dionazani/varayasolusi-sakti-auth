package org.varayasolusi.saktiauth.infrastructure.entityredis;

import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value="UserLogin")
public class UserLoginEntityRedis {

	@Id
	@Indexed
	private String appUserId;
	private String jwtToken;
	private String createdAt;
    private String updatedAt;
}
