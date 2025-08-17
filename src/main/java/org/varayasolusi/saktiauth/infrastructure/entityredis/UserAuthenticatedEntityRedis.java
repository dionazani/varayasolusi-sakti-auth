package org.varayasolusi.saktiauth.infrastructure.entityredis;


import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.index.Indexed;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value="UserAuthenticated")
public class UserAuthenticatedEntityRedis {

	@Id
	private String id;
	private String appUserId;
	private String authenticatedId;
	private String accessToken;
	private String refreshToken;
	private String behaviour;
	private String createdAt;
    private String updatedAt;
}
