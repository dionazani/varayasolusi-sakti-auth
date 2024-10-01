package org.varayasolusi.saktiauth.context.login;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.varayasolusi.saktiauth.context.v1.login.LoginReqModel;
import org.varayasolusi.saktiauth.context.v1.login.LoginService;

@SpringBootTest
public class LoginUnitTest {

	@Autowired
	private LoginService loginService;

	@Test
	public void shouldDoLogin() {

		var loginReqModel = new LoginReqModel();
		loginReqModel.setUsername("dion.azani@gmail.com");
		loginReqModel.setPassword("1234");
		var responseModel = loginService.doLogin(loginReqModel);

		assertEquals(200, responseModel.getHttpStatusCode());
	}
}
