package com.project.userService.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Address {
    private String houseNo;
    private String landmark;
    private String street;
    private String city;
    private int pin;

}