package org.varayasolusi.saktiauth.context.v1.emailverification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/email-verification")
public class EmailVerificationRestController {

    @Autowired
    private EmailVerificationService emailVerificationService;

    @PostMapping(path= "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> verify(@RequestBody EmailVerificationReqModel emailVerificationReqModel) {
    	
        ResponseEntity<Object> responseEntity = null;
        try {
        	
        	var responseModel = this.emailVerificationService.verify(emailVerificationReqModel);
            
            responseEntity = ResponseEntity
                    .status(responseModel.getHttpStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(responseModel);
        }
        catch(Exception ex) {
            responseEntity = ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(ex.getMessage());
        }

        return responseEntity;
    }
}
