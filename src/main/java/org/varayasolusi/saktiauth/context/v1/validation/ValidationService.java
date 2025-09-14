package org.varayasolusi.saktiauth.context.v1.validation;

import org.springframework.stereotype.Service;
import org.varayasolusi.saktiauth.infrastructure.dto.ResponseDTO;

@Service
public interface ValidationService {

	ResponseDTO authenticate(String token);
}
