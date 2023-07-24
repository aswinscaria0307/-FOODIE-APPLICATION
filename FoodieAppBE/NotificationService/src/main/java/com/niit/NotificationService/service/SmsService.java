package com.niit.NotificationService.service;

import com.niit.NotificationService.domain.Sms;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class SmsService {
    @Value("${TWILIO_ACCOUNT_SID}")
     String SID;
    @Value("${TWILIO_AUTH_TOKEN}")
    private String authToken;
    @Value("${TWILIO_PHONE_NUMBER}")
    private String fromPhoneNumber;
    @PostConstruct
    public void setUp(){
        Twilio.init(SID,authToken);
    }
    public Boolean  sendSms(String smsNumber,String smsMessage){
        System.out.println(smsNumber);
        Message message=Message.creator(new PhoneNumber(smsNumber),new PhoneNumber(fromPhoneNumber),smsMessage).create();
        log.info("Sms Triggered successfully and the sid:",message.getSid());
        return true;
    }
}
