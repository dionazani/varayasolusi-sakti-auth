package org.varayasolusi.saktiauth.context.emailverification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.varayasolusi.saktiauth.infrastructure.entity.AppUserEntity;
import org.varayasolusi.saktiauth.infrastructure.entity.AppUserPersonEntity;
import org.varayasolusi.saktiauth.infrastructure.model.ResponseModel;
import org.varayasolusi.saktiauth.infrastructure.model.SignUpDescModel;
import org.varayasolusi.saktiauth.infrastructure.repository.AppUserPersonRepository;
import org.varayasolusi.saktiauth.infrastructure.repository.AppUserRepository;
import org.varayasolusi.saktiauth.infrastructure.repository.SignUpRepository;
import org.varayasolusi.saktiauth.infrastructure.utils.FormatUtils;
import org.varayasolusi.saktiauth.restcontroller.emailverification.EmailVerificationReqModel;
import java.util.Calendar;
import java.util.Date;
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

    private PasswordEncoder passwordEncoder;
    
    @Transactional
    @Override
    public ResponseModel verify(EmailVerificationReqModel emailVerificationReqModel) throws JsonProcessingException {

        String signUpId = emailVerificationReqModel.getSignUpId();
        System.out.println(signUpId);
        var signUpEntity = signUpRepository.findById(UUID.fromString(signUpId));

        //extract JSON
        String json = signUpEntity.get().getReqDesc();
        ObjectMapper objectMapper = new ObjectMapper();
        var signUpDescModel = objectMapper.readValue(json, SignUpDescModel.class);

        // insert into app_user
        var appUserEntity = new AppUserEntity();
        appUserEntity.setAppUserType(1);
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

        //insert into app_user_person
        
        this.passwordEncoder = new BCryptPasswordEncoder();
        String passwordHashed = this.passwordEncoder.encode(signUpDescModel.getPassword());
        
        var appUserPersonEntity = new AppUserPersonEntity();
        appUserPersonEntity.setAppUserId(UUID.fromString(appUserId));
        appUserPersonEntity.setAppRoleId(UUID.fromString(appRoleIdDebtor));
        appUserPersonEntity.setPassword(passwordHashed);
        appUserPersonEntity.setMustChangePassword(mustChangePasswordShortValue);
        appUserPersonEntity.setChangePasswordNextDate(dt);
        this.appUserPersonRepository.save(appUserPersonEntity);

        var responseModel = new ResponseModel();
        responseModel.setHttpStatusCode(200);
        responseModel.setResponseMessage("success");
        responseModel.setTimeStamp(FormatUtils.getCurrentTimestamp());

        return responseModel;
    }
}

