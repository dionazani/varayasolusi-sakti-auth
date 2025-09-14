package org.varayasolusi.saktiauth.context.v1.signup;

import org.springframework.stereotype.Service;
import org.varayasolusi.saktiauth.infrastructure.dto.ResponseDTO;

@Service
public interface SignUpService {

	ResponseDTO addNew(SignUpReqDTO signUpReqModel);
}
