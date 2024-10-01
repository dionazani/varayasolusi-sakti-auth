package org.varayasolusi.saktiauth.context.emailverification;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.varayasolusi.saktiauth.context.v1.emailverification.EmailVerificationReqModel;
import org.varayasolusi.saktiauth.context.v1.emailverification.EmailVerificationService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EmailVerificationUnitTest {

    @Autowired
    EmailVerificationService emailVerificationService;

    @Test
    public void shouldDoVerification() throws JsonProcessingException {
    	
        var emailVerificationReqModel = new EmailVerificationReqModel();
        emailVerificationReqModel.setSignUpId("c56fed87-6b82-4f97-b503-59e7e7f7d90f");
        
        /*
        var responseModel = this.emailVerificationService.verify(emailVerificationReqModel);
        assertEquals(200, responseModel.getHttpStatusCode());
        */
    }
}
