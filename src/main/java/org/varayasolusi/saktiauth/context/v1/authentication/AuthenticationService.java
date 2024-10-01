package org.varayasolusi.saktiauth.context.v1.authentication;

import org.springframework.stereotype.Service;
import org.varayasolusi.saktiauth.infrastructure.model.ResponseModel;

@Service
public interface AuthenticationService {

	ResponseModel authenticate(AuthenticatonReqModel authenticatonReqModel);
}
