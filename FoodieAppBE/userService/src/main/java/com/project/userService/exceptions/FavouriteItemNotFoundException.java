package com.project.userService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.NOT_FOUND,reason = "Not a valid Id Provided...")
public class FavouriteItemNotFoundException extends Exception{

}
