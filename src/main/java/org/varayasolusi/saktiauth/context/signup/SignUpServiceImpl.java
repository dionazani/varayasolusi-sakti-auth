package org.varayasolusi.saktiauth.context.signup;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.varayasolusi.saktiauth.infrastructure.entity.SignUpEntity;
import org.varayasolusi.saktiauth.infrastructure.model.EmailModel;
import org.varayasolusi.saktiauth.infrastructure.model.ResponseModel;
import org.varayasolusi.saktiauth.infrastructure.model.SignUpDescModel;
import org.varayasolusi.saktiauth.infrastructure.repository.AppUserRepository;
import org.varayasolusi.saktiauth.infrastructure.repository.SignUpRepository;
import org.varayasolusi.saktiauth.infrastructure.utils.FormatUtils;
import org.varayasolusi.saktiauth.restcontroller.signup.SignUpReqModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

@Service
public class SignUpServiceImpl implements SignUpService {

	@Autowired
	private SignUpRepository signUpRepository;
	
	@Autowired
	private AppUserRepository appUserRepository;
	
	@Autowired
	private SignUpSendToMq signUpSendToMq;
	
	@Autowired
	private AmqpTemplate rabbitTemplate;

	@Value("${rabbitmq.saktiauth.exchange-direct}")
	String saktiAuthExchangeDirect;

	@Value("${rabbitmq.saktiauth.signup-email.routing-key}")
	private String saktiAuthSignUpEmailRoutingKey;

	private PasswordEncoder passwordEncoder;
	
	@Override
	public ResponseModel addNew(SignUpReqModel signUpReqModel) {

		// check weather SignUp is valid ??
		String email = signUpReqModel.getEmail();
		Boolean isUserExistByEmail = this.isUserExistByEmail(email);
		if (isUserExistByEmail) {
			var responseModel = new ResponseModel();
			responseModel.setHttpStatusCode(410);
			responseModel.setResponseMessage("user has been exist");
			responseModel.setTimeStamp(FormatUtils.getCurrentTimestamp());

			return responseModel;
		}

		this.passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = this.passwordEncoder.encode(signUpReqModel.getPassword());

		var signUpDescModel = new SignUpDescModel();
		signUpDescModel.setFullname(signUpReqModel.getFullname());
		signUpDescModel.setGender(signUpReqModel.getGender());
		signUpDescModel.setEmail(signUpReqModel.getEmail());
		signUpDescModel.setMobilePhone(signUpReqModel.getMobilePhone());
		signUpDescModel.setPassword(hashedPassword);

		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = "";
		try {
			jsonStr = mapper.writeValueAsString(signUpDescModel);
			System.out.println("Send jsonStr = " + jsonStr);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		var signUpEntity = new SignUpEntity();
		signUpEntity.setReqDesc(jsonStr);
		this.signUpRepository.save(signUpEntity);
		String signUpId = signUpEntity.getId().toString();

		var emailDTO = new EmailModel();
		emailDTO.setId(signUpId);
		emailDTO.setEmail(signUpReqModel.getEmail());
		emailDTO.setMobilePhone(signUpReqModel.getMobilePhone());

		String title = "Bapak";
		if (signUpReqModel.getGender().toUpperCase() == "W")
			title = "Ibu";
		emailDTO.setTitle(title);
		
		emailDTO.setFullname(signUpReqModel.getFullname());
		emailDTO.setCreatedAt(FormatUtils.getFromTimestamp(FormatUtils.getCurrentTimestamp()));
		
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			signUpSendToMq.send(emailDTO);
			map.put("isSendToMq", true);
		} catch (Exception exception) {
			map.put("isSendToMq", false);
			map.put("mqError", exception.toString());
		}

		var responseDTO = new ResponseModel();
		responseDTO.setHttpStatusCode(200);
		responseDTO.setResponseMessage("success");
		responseDTO.setTimeStamp(FormatUtils.getCurrentTimestamp());
		responseDTO.setData(map);

		return responseDTO;
	}
	
	private Boolean isUserExistByEmail(String email) {
		
		Boolean result = true;
		
		var appUserEntity =this.appUserRepository.findByEmail(email);
		if (appUserEntity == null)
			result = false;
		
		return result;
	}

}
