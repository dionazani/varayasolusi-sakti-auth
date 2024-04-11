package org.varayasolusi.saktiauth.context.emailverification;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import org.varayasolusi.saktiauth.infrastructure.model.ResponseModel;
import org.varayasolusi.saktiauth.restcontroller.emailverification.EmailVerificationReqModel;

@Service
public interface EmailVerificationService {

    ResponseModel verify(EmailVerificationReqModel emailVerificationReqModel) throws JsonProcessingException;
}
