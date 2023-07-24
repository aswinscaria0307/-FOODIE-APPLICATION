package com.niit.NotificationService.domain;

import lombok.Data;

@Data
public class Sms {
    private String message;
    private String destinationNumber;
}
