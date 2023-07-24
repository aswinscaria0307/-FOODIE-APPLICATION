package com.niit.NotificationService.controller;
import com.niit.NotificationService.domain.Sms;
import com.niit.NotificationService.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notification")
//@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class SmsController {
    @Autowired
    SmsService smsService;
    @PostMapping("/sms")
    public ResponseEntity processSMS(@RequestBody Sms sms){
        return new ResponseEntity<>(smsService.sendSms(sms.getDestinationNumber(),sms.getMessage()), HttpStatus.OK);
    }
}
