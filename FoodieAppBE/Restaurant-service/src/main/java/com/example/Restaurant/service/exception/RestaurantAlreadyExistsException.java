package com.example.Restaurant.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT,reason = "Restaurant of this Id already exists")
public class RestaurantAlreadyExistsException extends Exception{
}
