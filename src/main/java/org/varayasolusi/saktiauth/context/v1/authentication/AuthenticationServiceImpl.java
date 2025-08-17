package org.varayasolusi.saktiauth.context.v1.authentication;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.varayasolusi.saktiauth.infrastructure.entity.AppUserAuthenticatedEntity;
import org.varayasolusi.saktiauth.infrastructure.entityredis.UserAuthenticatedEntityRedis;
import org.varayasolusi.saktiauth.infrastructure.model.ResponseModel;
import org.varayasolusi.saktiauth.infrastructure.repository.AppUserAuthenticatedRepository;
import org.varayasolusi.saktiauth.infrastructure.repositoryredis.UserAuthenticatedRepositoryRedis;
import org.varayasolusi.saktiauth.utils.commons.FormatUtils;
import org.varayasolusi.saktiauth.utils.commons.JwtTokenManager;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.security.SignatureException;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private AppUserAuthenticatedRepository appUserAuthenticatedRepository;
	
	@Autowired
	private JwtTokenManager jwtTokenManager;
	
	@Autowired
	private UserAuthenticatedRepositoryRedis userLoginRepositoryRedis;
	
	@Override
	public ResponseModel authenticate(String token) {
		
		var responseModel = new ResponseModel();
		
		try {

			// check jwtToken.
			Jws<Claims> jwtResult = jwtTokenManager.parseJwt(token);
			String authenticatedId = jwtResult.getBody().getId();
			String appUserId = jwtResult.getBody().getIssuer();

			if (jwtTokenManager.isTokenExpired(token)) {
				responseModel.setHttpStatusCode(401);
				responseModel.setResponseCode("00000");
		        responseModel.setResponseMessage("invalid token");
		        responseModel.setTimeStamp(FormatUtils.getCurrentTimestamp());
			}
			
			// set response API.
			Map<String, String> map = new HashMap<String, String>();

			responseModel.setHttpStatusCode(200);
			responseModel.setResponseCode("00000");
	        responseModel.setResponseMessage("success");
	        responseModel.setTimeStamp(FormatUtils.getCurrentTimestamp());
	        
			// check first in REDIS.
			boolean isAuthenticatedInRedisExist = this.isAuthenticatedInRedisExist(appUserId, authenticatedId);
			if (isAuthenticatedInRedisExist) {
				map.put("authenticationCache", "true");
		        responseModel.setData(map);
			} else {
				// check in DB.
				boolean isAuthenticatedInDBExist = this.isAuthenticatedInDBExist(appUserId, authenticatedId);
				if (isAuthenticatedInDBExist) {
					map.put("authenticationCache", "false");
			        responseModel.setData(map);
				}
				else {
					responseModel.setHttpStatusCode(401);
					responseModel.setResponseCode("00000");
			        responseModel.setResponseMessage("unauthorized");
			        responseModel.setTimeStamp(FormatUtils.getCurrentTimestamp());
			        responseModel.setData(null);
				}
			}
		} catch(SignatureException | ExpiredJwtException sgx) {
			responseModel.setHttpStatusCode(500);
			responseModel.setResponseCode("00000");
	        responseModel.setResponseMessage("internal server error");
	        responseModel.setTimeStamp(FormatUtils.getCurrentTimestamp());
			
	        sgx.printStackTrace();
		}

        return responseModel;

	}
	
	private boolean isAuthenticatedInRedisExist(String appUserId, String authenticatedId) {
		boolean isExist = true;

		Optional<UserAuthenticatedEntityRedis> userAuthenticatedEntityRedis = this.userLoginRepositoryRedis.findById(appUserId);
		if (userAuthenticatedEntityRedis.isPresent()) {
			if (!userAuthenticatedEntityRedis.get().getAuthenticatedId().equals(authenticatedId))
				isExist = false;
		} else {
			isExist = false;
		}

		return isExist;
	}
	
	private boolean isAuthenticatedInDBExist(String appUserId, String authenticatedId) {
		boolean isExist = true;

		Optional<AppUserAuthenticatedEntity> appUserAuthenticatedEntity = this.appUserAuthenticatedRepository.findById(UUID.fromString(authenticatedId));
		if (appUserAuthenticatedEntity.isEmpty())
			isExist = false;

		return isExist;
	}

}
