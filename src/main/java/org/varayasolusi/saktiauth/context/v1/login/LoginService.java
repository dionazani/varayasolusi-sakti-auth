package org.varayasolusi.saktiauth.context.v1.login;

import org.springframework.stereotype.Service;
import org.varayasolusi.saktiauth.infrastructure.dto.ResponseDTO;

@Service
public interface LoginService {

	public ResponseDTO doLogin(LoginReqDTO loginReqModel);
}
