package org.varayasolusi.saktiauth.context.v1.authentication;

import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.varayasolusi.saktiauth.infrastructure.entity.AppUserAuthenticatedEntity;
import org.varayasolusi.saktiauth.infrastructure.model.ResponseModel;
import org.varayasolusi.saktiauth.infrastructure.repository.AppUserAuthenticatedRepository;
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
	
	@Override
	public ResponseModel authenticate(AuthenticatonReqModel authenticatonReqModel) {
		
		String jwtToken = authenticatonReqModel.getJwtToken();
		var responseModel = new ResponseModel();
		
		try {
			
			// check jwtToken.
			
			Jws<Claims> jwtResult = jwtTokenManager.parseJwt(jwtToken);
			String jwtId = jwtResult.getBody().getId();
			
			// check in AppUserAuthenticated;
			Optional<AppUserAuthenticatedEntity> appUserAuthenticatedEntity = this.appUserAuthenticatedRepository.findById(UUID.fromString(jwtId));
			if (appUserAuthenticatedEntity.isPresent()) {
		        responseModel.setHttpStatusCode(200);
		        responseModel.setResponseMessage("success");
		        responseModel.setTimeStamp(FormatUtils.getCurrentTimestamp());
		        responseModel.setData(jwtId);
			} else {
		        responseModel.setHttpStatusCode(401);
		        responseModel.setResponseMessage("unauthorized");
		        responseModel.setTimeStamp(FormatUtils.getCurrentTimestamp());
		        responseModel.setData(jwtId);
			}
	        
		} catch(SignatureException sgx) {
			responseModel.setHttpStatusCode(401);
	        responseModel.setResponseMessage("unauthorized");
	        responseModel.setTimeStamp(FormatUtils.getCurrentTimestamp());
			
		} catch(ExpiredJwtException eje) {
			responseModel.setHttpStatusCode(401);
	        responseModel.setResponseMessage("unauthorized");
	        responseModel.setTimeStamp(FormatUtils.getCurrentTimestamp());
		}
		
		return responseModel;

	}

}
