package org.varayasolusi.saktiauth.context.v1.login;

import org.springframework.stereotype.Service;
import org.varayasolusi.saktiauth.infrastructure.model.ResponseModel;

@Service
public interface LoginService {

	public ResponseModel doLogin(LoginReqModel loginReqModel);
}
