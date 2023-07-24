package com.project.authService.service;

import com.project.authService.domain.User;

import java.util.Map;

public interface ITokenGenerator {
    Map<String,String> storeToken(User user);
}
