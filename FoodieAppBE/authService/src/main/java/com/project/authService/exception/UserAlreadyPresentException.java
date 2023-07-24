package com.project.authService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT ,reason = "User already present with the same id...")
public class UserAlreadyPresentException extends Exception{
}
