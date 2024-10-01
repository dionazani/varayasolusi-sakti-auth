package org.varayasolusi.saktiauth.context.v1.emailverification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.varayasolusi.saktiauth.infrastructure.entity.AppUserEntity;
import org.varayasolusi.saktiauth.infrastructure.entity.AppUserPersonEntity;
import org.varayasolusi.saktiauth.infrastructure.entity.AppUserSignUpEntity;
import org.varayasolusi.saktiauth.infrastructure.entity.AppUserVerifiedEntity;
import org.varayasolusi.saktiauth.infrastructure.model.ResponseModel;
import org.varayasolusi.saktiauth.infrastructure.model.SignUpDescModel;
import org.varayasolusi.saktiauth.infrastructure.repository.AppUserPersonRepository;
import org.varayasolusi.saktiauth.infrastructure.repository.AppUserRepository;
import org.varayasolusi.saktiauth.infrastructure.repository.AppUserSignUpRepository;
import org.varayasolusi.saktiauth.infrastructure.repository.AppUserVerifiedRepository;
import org.varayasolusi.saktiauth.infrastructure.repository.SignUpRepository;
import org.varayasolusi.saktiauth.utils.commons.FormatUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {

    @Autowired
    private SignUpRepository signUpRepository;

    @Value("${id.table.appRoleId.debtor}")
    private String appRoleIdDebtor;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserPersonRepository appUserPersonRepository;
    
    @Autowired
    private AppUserVerifiedRepository appUserVerifiedRepository;
    
    @Autowired
    private AppUserSignUpRepository appUserSignUpRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailVerificationSendToMq emailVerificationSendToMq;
    
    private final String APP_USER_PERSON_STATUS = "ACTIVE";
 
    @Transactional
    @Override
    public ResponseModel verify(EmailVerificationReqModel emailVerificationReqModel) throws JsonProcessingException {

    	String signUpId = emailVerificationReqModel.getSignUpId();
    	
    	if (this.isAppUserExist(signUpId)) {
    		var responseModel = new ResponseModel();
            responseModel.setHttpStatusCode(400);
            responseModel.setResponseMessage("object has been exist");
            responseModel.setTimeStamp(FormatUtils.getCurrentTimestamp());
            
            return responseModel;
    	} 
    	
        System.out.println(signUpId);
        var signUpEntity = signUpRepository.findById(UUID.fromString(signUpId));

        //extract JSON
        String json = signUpEntity.get().getReqDesc();
        ObjectMapper objectMapper = new ObjectMapper();
        var signUpDescModel = objectMapper.readValue(json, SignUpDescModel.class);

        // insert into app_user
        var appUserEntity = new AppUserEntity();
        appUserEntity.setAppUserType(1);
        appUserEntity.setAppUserName(signUpDescModel.getFullname());
        appUserEntity.setEmail(signUpDescModel.getEmail());
        appUserEntity.setMobilePhone(signUpDescModel.getMobilePhone());
        appUserRepository.save(appUserEntity);
        String appUserId = String.valueOf(appUserEntity.getId());

        // set changePassword
        short mustChangePasswordShortValue = 0;
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 90);
        dt = c.getTime();

        this.passwordEncoder = new BCryptPasswordEncoder();
        String passwordHashed = this.passwordEncoder.encode(signUpDescModel.getPassword());
        
        //insert into app_user_person
        var appUserPersonEntity = new AppUserPersonEntity();
        appUserPersonEntity.setAppUserId(UUID.fromString(appUserId));
        appUserPersonEntity.setAppRoleId(UUID.fromString(appRoleIdDebtor));
        appUserPersonEntity.setPassword(passwordHashed);
        appUserPersonEntity.setMustChangePassword(mustChangePasswordShortValue);
        appUserPersonEntity.setChangePasswordNextDate(dt);
        appUserPersonEntity.setUserPersonStatus(APP_USER_PERSON_STATUS);
        this.appUserPersonRepository.save(appUserPersonEntity);

        // insert into app_user_signup
        var appUserSignUpEntity = new AppUserSignUpEntity();
        appUserSignUpEntity.setAppUserId(UUID.fromString(appUserId));
        appUserSignUpEntity.setSignUpId(UUID.fromString(signUpId));
        this.appUserSignUpRepository.save(appUserSignUpEntity);
        
        // insert into app_user_verified
        var appUserVerifiedEntity = new AppUserVerifiedEntity();
        appUserVerifiedEntity.setAppUserId(UUID.fromString(appUserId));
        appUserVerifiedEntity.setVerifiedFor("EMA");
        appUserVerifiedEntity.setVerifiedDate(FormatUtils.getCurrentTimestamp());
        appUserVerifiedRepository.save(appUserVerifiedEntity);
        
        // send to MQ.
        var emailVerificationMqModel = new EmailVerificationMqModel();
        emailVerificationMqModel.setId(appUserId);
        emailVerificationMqModel.setUserPersonStatus(APP_USER_PERSON_STATUS);
        emailVerificationMqModel.setSendAt(String.valueOf(FormatUtils.getCurrentTimestamp()));
        
 		Map<String, Object> map = new HashMap<String, Object>();
 		try {
 			emailVerificationSendToMq.send(emailVerificationMqModel);
 			map.put("isSendToMq", true);
 		} catch (Exception exception) {
 			map.put("isSendToMq", false);
 			map.put("mqError", exception.toString());
 		}
 		
        var responseModel = new ResponseModel();
        responseModel.setHttpStatusCode(200);
        responseModel.setResponseMessage("success");
        responseModel.setTimeStamp(FormatUtils.getCurrentTimestamp());
        responseModel.setData(map);

        return responseModel;
    }
    
    private boolean isAppUserExist(String signUpId) {
    	
    	boolean isExist = true;
    	
    	Optional<AppUserSignUpEntity> appUserSignUpEntity = Optional.ofNullable(this.appUserSignUpRepository.findBySignUpId(UUID.fromString(signUpId)));
    	isExist = appUserSignUpEntity.isPresent();
    	
    	return isExist;
    }
}
