package org.varayasolusi.saktiauth.context.v1.emailverification;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import org.varayasolusi.saktiauth.infrastructure.dto.ResponseDTO;

@Service
public interface EmailVerificationService {

    ResponseDTO verify(EmailVerificationReqModel emailVerificationReqModel) throws JsonProcessingException;
}
