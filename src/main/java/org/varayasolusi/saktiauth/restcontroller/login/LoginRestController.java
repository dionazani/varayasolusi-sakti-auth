package org.varayasolusi.saktiauth.restcontroller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.varayasolusi.saktiauth.context.login.LoginReqDTO;
import org.varayasolusi.saktiauth.context.login.LoginService;

@RestController
@RequestMapping("/v1/login")
public class LoginRestController {

    @Autowired
    private LoginService loginService;

    @PostMapping("")
    public ResponseEntity<Object> login(@RequestBody LoginReqModel loginReqModel) {
        ResponseEntity<Object> responseEntity = null;

        try {
        
            var responseModel = this.loginService.doLogin(loginReqModel);
            responseEntity = ResponseEntity.status(responseModel.getHttpStatusCode())
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(responseModel);

        }
        catch(Exception ex) {
            responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .contentType(MediaType.TEXT_PLAIN)
                                .body(ex.getMessage());
        }

        return responseEntity;
    }
}
