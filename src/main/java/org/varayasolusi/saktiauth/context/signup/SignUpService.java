package org.varayasolusi.saktiauth.context.signup;

import org.springframework.stereotype.Service;
import org.varayasolusi.saktiauth.infrastructure.model.ResponseModel;
import org.varayasolusi.saktiauth.restcontroller.signup.SignUpReqModel;

@Service
public interface SignUpService {

	ResponseModel addNew(SignUpReqModel signUpReqModel);
}
