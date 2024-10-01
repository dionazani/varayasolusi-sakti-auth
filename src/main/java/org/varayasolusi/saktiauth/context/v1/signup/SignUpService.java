package org.varayasolusi.saktiauth.context.v1.signup;

import org.springframework.stereotype.Service;
import org.varayasolusi.saktiauth.infrastructure.model.ResponseModel;

@Service
public interface SignUpService {

	ResponseModel addNew(SignUpReqModel signUpReqModel);
}
