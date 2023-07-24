package com.niit.NotificationService.controller;

import com.niit.NotificationService.domain.Email;
import com.niit.NotificationService.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notification")
//@CrossOrigin(origins = "http://localhost:4200")
public class EmailController {
    @Autowired
    EmailService emailService;

    @PostMapping("/email/{email}")
    public ResponseEntity sendMail(@PathVariable String email, @RequestBody Email content){
        return new ResponseEntity<>(emailService.sendEmail(email,content.subject,content.body),HttpStatus.OK);
    }

}
