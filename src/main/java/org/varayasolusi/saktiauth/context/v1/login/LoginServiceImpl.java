package org.varayasolusi.saktiauth.context.v1.login;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.varayasolusi.saktiauth.infrastructure.entity.AppUserAuthenticatedEntity;
import org.varayasolusi.saktiauth.infrastructure.model.ResponseModel;
import org.varayasolusi.saktiauth.infrastructure.repository.AppUserAuthenticatedRepository;
import org.varayasolusi.saktiauth.infrastructure.repository.AppUserRepository;
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
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AppUserAuthenticatedRepository appUserAuthenticatedRepository;
	
	@Value("${id.table.applicationType.web}")
	private String applicationTypeWebId;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	private final String SAKTI_AUTH_JWT = "SAKTI_AUTH_JWT";
	
	@Override
	@Transactional
	public ResponseModel doLogin(LoginReqModel loginReqModel) {
		
		ResponseModel responseModel = null;
		
		try {
			String email = loginReqModel.getUsername();
			var appUserPersonEntityCustom = this.loginRepositoryCustom.getAppUserByEmail(email);
			this.passwordEncoder = new BCryptPasswordEncoder();
			
			if (passwordEncoder.matches(loginReqModel.getPassword(), appUserPersonEntityCustom.getPassword())) {
			
				String jwtId = UUID.randomUUID().toString();
				String jwtToken =  jwtTokenManager.createJwtToken(appUserPersonEntityCustom.getAppUserId(), jwtId);;
				String appUserId = appUserPersonEntityCustom.getAppUserId();
				
				// delete first app_user_authenticated by app_user_id.
				this.appUserAuthenticatedRepository.deleteByAppUserId(UUID.fromString(appUserPersonEntityCustom.getAppUserId()));
				
				// insert into redis.
				redisTemplate.opsForValue().set(SAKTI_AUTH_JWT, jwtToken);
				
				// insert into AppUserAuthenticatedEntity;
				var appUserAuthenticatedEntity = new AppUserAuthenticatedEntity();
				appUserAuthenticatedEntity.setId(UUID.fromString(jwtId));
				appUserAuthenticatedEntity.setApplicationTypeId(UUID.fromString(applicationTypeWebId));
				appUserAuthenticatedEntity.setAppUserId(UUID.fromString(appUserId));
				appUserAuthenticatedEntity.setTokenValue(jwtToken);
				appUserAuthenticatedRepository.save(appUserAuthenticatedEntity);
	
				// set Response by Map;

				Map<String, String> map = new HashMap<String, String>();
				map.put("token", jwtToken);
				
				responseModel = new ResponseModel();
				responseModel.setHttpStatusCode(200);
		        responseModel.setResponseMessage("succes");
		        responseModel.setTimeStamp(FormatUtils.getCurrentTimestamp());
		        responseModel.setData(map);
		        
			}
			else {
				responseModel = new ResponseModel();
				responseModel.setHttpStatusCode(401);
				responseModel.setResponseMessage("unauthorized");
				responseModel.setTimeStamp(FormatUtils.getCurrentTimestamp());
				responseModel.setData(null);
			}
		}
		catch(NoResultException nre) {
			responseModel = new ResponseModel();
			responseModel.setHttpStatusCode(500);
	        responseModel.setResponseMessage("error");
	        responseModel.setTimeStamp(FormatUtils.getCurrentTimestamp());
	        responseModel.setData(null);
		}
		
		return responseModel;
	}
}
