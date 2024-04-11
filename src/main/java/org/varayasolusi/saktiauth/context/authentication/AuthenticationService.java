package org.varayasolusi.saktiauth.context.authentication;

import org.springframework.stereotype.Service;
import org.varayasolusi.saktiauth.infrastructure.model.ResponseModel;
import org.varayasolusi.saktiauth.restcontroller.authentication.AuthenticatonReqModel;

@Service
public interface AuthenticationService {

	ResponseModel authenticate(AuthenticatonReqModel authenticatonReqModel);
}
