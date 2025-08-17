package org.varayasolusi.saktiauth.context.v1.login;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.varayasolusi.saktiauth.infrastructure.entity.AppUserAuthenticatedEntity;
import org.varayasolusi.saktiauth.infrastructure.entity.AppUserEntity;
import org.varayasolusi.saktiauth.infrastructure.entity.ApplicationTypeEntity;
import org.varayasolusi.saktiauth.infrastructure.entityredis.UserAuthenticatedEntityRedis;
import org.varayasolusi.saktiauth.infrastructure.model.ResponseModel;
import org.varayasolusi.saktiauth.infrastructure.repository.AppUserAuthenticatedRepository;
import org.varayasolusi.saktiauth.infrastructure.repository.AppUserRepository;
import org.varayasolusi.saktiauth.infrastructure.repository.ApplicationTypeRepository;
import org.varayasolusi.saktiauth.infrastructure.repositoryredis.UserAuthenticatedRepositoryRedis;
import org.varayasolusi.saktiauth.utils.commons.FormatUtils;
import org.varayasolusi.saktiauth.utils.commons.JwtTokenManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private LoginRepositoryCustom loginRepositoryCustom;

	@Autowired
	private JwtTokenManager jwtTokenManager;

    @Autowired
	private AppUserAuthenticatedRepository appUserAuthenticatedRepository;
	
	@Autowired
	private ApplicationTypeRepository applicationTypeRepository;

	@Autowired
	private AppUserRepository appUserRepository;

	@Value("${id.table.applicationType.web}")
	private String applicationTypeWebId;

	@Value("${jwt.expiration.access-token}")
	private String expirationAccessToken;

	@Value("${jwt.expiration.refresh-token}")
	private String expirationRefreshToken;

	@Autowired
	private UserAuthenticatedRepositoryRedis userAuthenticatedRepositoryRedis;

	@Override
	@Transactional
	public ResponseModel doLogin(LoginReqModel loginReqModel) {
		
		ResponseModel responseModel = null;
		
		try {
			String email = loginReqModel.getUsername();
			var appUserPersonEntityCustom = this.loginRepositoryCustom.getAppUserByEmail(email);
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			
			if (passwordEncoder.matches(loginReqModel.getPassword(), appUserPersonEntityCustom.getPassword())) {

				// create access token
				String accessAuthenticatedId = UUID.randomUUID().toString();
				String accesstoken =  jwtTokenManager.createToken(appUserPersonEntityCustom.getAppUserId(), accessAuthenticatedId, Integer.parseInt(this.expirationAccessToken));

				//create refresh token
				String refreshAuthenticatedId = UUID.randomUUID().toString();
				String refreshToken =  jwtTokenManager.createToken(appUserPersonEntityCustom.getAppUserId(), refreshAuthenticatedId, Integer.parseInt(this.expirationRefreshToken));

				// delete first app_user_authenticated by app_user_id.
				this.appUserAuthenticatedRepository.deleteByAppUserId(UUID.fromString(appUserPersonEntityCustom.getAppUserId()));
				
				// get applicationTypeId
				ApplicationTypeEntity applicationTypeEntity = applicationTypeRepository.getReferenceById(UUID.fromString(applicationTypeWebId));
				
				// get appUserId
				String appUserId = appUserPersonEntityCustom.getAppUserId();
				AppUserEntity appUserEntity = appUserRepository.getReferenceById(UUID.fromString(appUserId));
				
				// insert into AppUserAuthenticatedEntity;
				var appUserAuthenticatedEntity = new AppUserAuthenticatedEntity();
				appUserAuthenticatedEntity.setId(UUID.fromString(accessAuthenticatedId));
				appUserAuthenticatedEntity.setApplicationType(applicationTypeEntity);
				appUserAuthenticatedEntity.setAppUser(appUserEntity);
				appUserAuthenticatedEntity.setAccessToken(accesstoken);
				appUserAuthenticatedEntity.setRefreshToken(refreshToken);
				appUserAuthenticatedEntity.setBehaviour("L");
				appUserAuthenticatedRepository.save(appUserAuthenticatedEntity);
				
				// add to redis, but it need to be deleted first.
				System.out.println(accessAuthenticatedId);
				this.userAuthenticatedRepositoryRedis.deleteById(appUserId);

				var userAuthenticatedEntityRedisEntityRedis = new UserAuthenticatedEntityRedis();
				userAuthenticatedEntityRedisEntityRedis.setId(appUserId);
				userAuthenticatedEntityRedisEntityRedis.setAuthenticatedId(accessAuthenticatedId);
				userAuthenticatedEntityRedisEntityRedis.setAppUserId(appUserId);
				userAuthenticatedEntityRedisEntityRedis.setAccessToken(accesstoken);
				userAuthenticatedEntityRedisEntityRedis.setRefreshToken(refreshToken);
				userAuthenticatedEntityRedisEntityRedis.setBehaviour("L");
				userAuthenticatedEntityRedisEntityRedis.setCreatedAt(String.valueOf(FormatUtils.getCurrentTimestamp()));
				userAuthenticatedEntityRedisEntityRedis.setUpdatedAt(String.valueOf(FormatUtils.getCurrentTimestamp()));
				this.userAuthenticatedRepositoryRedis.save(userAuthenticatedEntityRedisEntityRedis);

				// set Response by Map;
				Map<String, String> map = new HashMap<String, String>();
				map.put("behaviour", "L");
				map.put("accessToken", accesstoken);
				map.put("refreshToken", refreshToken);

				responseModel = new ResponseModel();
				responseModel.setHttpStatusCode(200);
				responseModel.setResponseCode("00000");
		        responseModel.setResponseMessage("succes");
		        responseModel.setTimeStamp(FormatUtils.getCurrentTimestamp());
		        responseModel.setData(map);
		        
			}
			else {
				responseModel = new ResponseModel();
				responseModel.setHttpStatusCode(401);
				responseModel.setResponseCode("00000");
				responseModel.setResponseMessage("unauthorized");
				responseModel.setTimeStamp(FormatUtils.getCurrentTimestamp());
				responseModel.setData(null);
			}
		}
		catch(NoResultException nre) {
			responseModel = new ResponseModel();
			responseModel.setHttpStatusCode(500);
			responseModel.setResponseCode("00000");
	        responseModel.setResponseMessage("error");
	        responseModel.setTimeStamp(FormatUtils.getCurrentTimestamp());
	        responseModel.setData(null);
		}
		
		return responseModel;
	}
}
