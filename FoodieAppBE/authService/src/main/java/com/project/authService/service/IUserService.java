package com.project.authService.service;

import com.project.authService.domain.User;
import com.project.authService.exception.UserAlreadyPresentException;
import com.project.authService.exception.UserNotFoundException;

public interface IUserService {
    User login(User user)throws UserNotFoundException;
    User register (User user);
    User UpdateUser(String email, User user) throws UserNotFoundException;
    void removeUser(String email);
}
