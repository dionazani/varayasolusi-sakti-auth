package org.varayasolusi.saktiauth.context.v1.signup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.varayasolusi.saktiauth.infrastructure.entity.SignUpEntity;
import org.varayasolusi.saktiauth.infrastructure.dto.EmailDTO;
import org.varayasolusi.saktiauth.infrastructure.dto.ResponseDTO;
import org.varayasolusi.saktiauth.infrastructure.dto.SignUpDescDTO;
import org.varayasolusi.saktiauth.infrastructure.repository.AppUserRepository;
import org.varayasolusi.saktiauth.infrastructure.repository.SignUpRepository;
import org.varayasolusi.saktiauth.utils.commons.FormatUtils;

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

	private PasswordEncoder passwordEncoder;
	
	@Override
	public ResponseDTO addNew(SignUpReqDTO signUpReqDTO) {

		// check weather SignUp is valid ??
		String email = signUpReqDTO.getEmail();
		Boolean isUserExistByEmail = this.isUserExistByEmail(email);
		if (isUserExistByEmail) {
			var responseDTO = new ResponseDTO();
			responseDTO.setHttpStatusCode(410);
			responseDTO.setResponseMessage("user has been exist");
			responseDTO.setTimeStamp(FormatUtils.getCurrentTimestamp());

			return responseDTO;
		}

		this.passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = this.passwordEncoder.encode(signUpReqDTO.getPassword());

		var signUpDescDTO = new SignUpDescDTO();
		signUpDescDTO.setFullname(signUpReqDTO.getFullname());
		signUpDescDTO.setGender(signUpReqDTO.getGender());
		signUpDescDTO.setEmail(signUpReqDTO.getEmail());
		signUpDescDTO.setMobilePhone(signUpReqDTO.getMobilePhone());
		signUpDescDTO.setPassword(hashedPassword);

		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = "";
		try {
			jsonStr = mapper.writeValueAsString(signUpDescDTO);
			System.out.println("Send jsonStr = " + jsonStr);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		var signUpEntity = new SignUpEntity();
		signUpEntity.setReqDesc(jsonStr);
		this.signUpRepository.save(signUpEntity);
		String signUpId = signUpEntity.getId().toString();

		var emailDTO = new EmailDTO();
		emailDTO.setId(signUpId);
		emailDTO.setEmail(signUpReqDTO.getEmail());
		emailDTO.setMobilePhone(signUpReqDTO.getMobilePhone());

		String title = "Bapak";
		if (signUpReqDTO.getGender().toUpperCase() == "W")
			title = "Ibu";
		emailDTO.setTitle(title);
		
		emailDTO.setFullname(signUpReqDTO.getFullname());
		emailDTO.setCreatedAt(FormatUtils.getFromTimestamp(FormatUtils.getCurrentTimestamp()));
		
		// send to MQ.
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			signUpSendToMq.send(emailDTO);
			map.put("isSendToMq", true);
		} catch (Exception exception) {
			map.put("isSendToMq", false);
			map.put("mqError", exception.toString());
		}

		var responseDTO = new ResponseDTO();
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
