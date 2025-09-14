package org.varayasolusi.saktiauth.context.v1.validation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.varayasolusi.saktiauth.infrastructure.entity.AppUserAuthenticatedEntity;
import org.varayasolusi.saktiauth.infrastructure.entityredis.UserAuthenticatedEntityRedis;
import org.varayasolusi.saktiauth.infrastructure.dto.ResponseDTO;
import org.varayasolusi.saktiauth.infrastructure.repository.AppUserAuthenticatedRepository;
import org.varayasolusi.saktiauth.infrastructure.repositoryredis.UserAuthenticatedRepositoryRedis;
import org.varayasolusi.saktiauth.utils.commons.FormatUtils;
import org.varayasolusi.saktiauth.utils.commons.JwtTokenManager;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.security.SignatureException;

@Service
public class ValidationServiceImpl implements ValidationService {

	@Autowired
	private AppUserAuthenticatedRepository appUserAuthenticatedRepository;
	
	@Autowired
	private JwtTokenManager jwtTokenManager;
	
	@Autowired
	private UserAuthenticatedRepositoryRedis userLoginRepositoryRedis;
	
	@Override
	public ResponseDTO authenticate(String token) {
		
		var responseDTO = new ResponseDTO();
		
		try {

			// check jwtToken.
            Jws<Claims> jwtResult = null;
            try {
                jwtResult = jwtTokenManager.parseJwt(token);
            }
			catch(ExpiredJwtException eje) {
                responseDTO.setHttpStatusCode(401);
                responseDTO.setResponseCode("0006.003");
                responseDTO.setResponseMessage("invalid token");
                responseDTO.setTimeStamp(FormatUtils.getCurrentTimestamp());

                return responseDTO;
            }

			String authenticatedId = jwtResult.getBody().getId();
			String appUserId = jwtResult.getBody().getIssuer();
			
			// set response API.
			Map<String, String> map = new HashMap<String, String>();

			responseDTO.setHttpStatusCode(200);
			responseDTO.setResponseCode("0011.000");
	        responseDTO.setResponseMessage("success");
	        responseDTO.setTimeStamp(FormatUtils.getCurrentTimestamp());
	        
			// check first in REDIS.
			boolean isAuthenticatedInRedisExist = this.isAuthenticatedInRedisExist(appUserId, authenticatedId);
			if (isAuthenticatedInRedisExist) {
				map.put("authenticationCache", "true");
		        responseDTO.setData(map);
			} else {
				// check in DB.
				boolean isAuthenticatedInDBExist = this.isAuthenticatedInDBExist(appUserId, authenticatedId);
				if (isAuthenticatedInDBExist) {
					map.put("authenticationCache", "false");
			        responseDTO.setData(map);
				}
				else {
					responseDTO.setHttpStatusCode(401);
					responseDTO.setResponseCode("0006.003");
			        responseDTO.setResponseMessage("invalid token");
			        responseDTO.setTimeStamp(FormatUtils.getCurrentTimestamp());
			        responseDTO.setData(null);

                    return responseDTO;
				}
			}
		} catch(SignatureException | ExpiredJwtException sgx) {
			responseDTO.setHttpStatusCode(500);
			responseDTO.setResponseCode("0016.003");
	        responseDTO.setResponseMessage("internal server error");
	        responseDTO.setTimeStamp(FormatUtils.getCurrentTimestamp());

            return responseDTO;
		}

        return responseDTO;

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
