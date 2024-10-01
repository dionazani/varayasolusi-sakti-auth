package org.varayasolusi.saktiauth.context.v1.emailverification;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import org.varayasolusi.saktiauth.infrastructure.model.ResponseModel;

@Service
public interface EmailVerificationService {

    ResponseModel verify(EmailVerificationReqModel emailVerificationReqModel) throws JsonProcessingException;
}
