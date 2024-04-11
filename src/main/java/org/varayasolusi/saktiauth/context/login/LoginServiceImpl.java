package org.varayasolusi.saktiauth.context.login;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.varayasolusi.saktiauth.infrastructure.entity.AppUserAuthenticatedEntity;
import org.varayasolusi.saktiauth.infrastructure.model.ResponseModel;
import org.varayasolusi.saktiauth.infrastructure.repository.AppUserAuthenticatedRepository;
import org.varayasolusi.saktiauth.infrastructure.utils.FormatUtils;
import org.varayasolusi.saktiauth.infrastructure.utils.JwtTokenManager;
import org.varayasolusi.saktiauth.restcontroller.login.LoginReqModel;

import jakarta.persistence.NoResultException;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private LoginRepositoryCustom loginRepositoryCustom;

	@Autowired
	private JwtTokenManager jwtTokenManager;
	
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AppUserAuthenticatedRepository appUserAuthenticatedRepository;
	
	@Value("${id.table.applicationType.web}")
	private String applicationTypeWebId;
	
	@Override
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
				
				// insert into AppUserAuthenticatedEntity;

				var appUserAuthenticatedEntity = new AppUserAuthenticatedEntity();
				appUserAuthenticatedEntity.setId(UUID.fromString(jwtId));
				appUserAuthenticatedEntity.setApplicationTypeId(UUID.fromString(applicationTypeWebId));
				appUserAuthenticatedEntity.setAppUserId(UUID.fromString(appUserId));
				appUserAuthenticatedEntity.setTokenValue(jwtToken);
				appUserAuthenticatedEntity.setLoginAt(FormatUtils.getCurrentTimestamp());
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
