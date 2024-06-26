package org.varayasolusi.saktiauth.restcontroller.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.varayasolusi.saktiauth.context.authentication.AuthenticationService;

@RestController
@RequestMapping("/v1/authentication")
public class AuthenticationRestController {
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@PostMapping("")
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticatonReqModel authenticatonReqModel) {

		ResponseEntity<Object> responseEntity = null;
        try {
        	

            var responseModel = this.authenticationService.authenticate(authenticatonReqModel);
            
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
