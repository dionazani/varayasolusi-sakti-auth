package org.varayasolusi.saktiauth.context.v1.validation;

import org.springframework.stereotype.Service;
import org.varayasolusi.saktiauth.infrastructure.model.ResponseModel;

@Service
public interface ValidationService {

	ResponseModel authenticate(String token);
}
