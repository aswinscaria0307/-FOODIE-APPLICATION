package com.example.Restaurant.service.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND ,reason = "Food item of this id is not found")
public class FoodItemNotFoundException extends Exception{

}
