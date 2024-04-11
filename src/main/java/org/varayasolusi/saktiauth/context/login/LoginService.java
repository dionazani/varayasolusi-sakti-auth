package org.varayasolusi.saktiauth.context.login;

import org.springframework.stereotype.Service;
import org.varayasolusi.saktiauth.infrastructure.model.ResponseModel;
import org.varayasolusi.saktiauth.restcontroller.login.LoginReqModel;

@Service
public interface LoginService {

	public ResponseModel doLogin(LoginReqModel loginReqModel);
}
