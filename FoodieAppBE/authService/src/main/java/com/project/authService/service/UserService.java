package com.project.authService.service;

import com.project.authService.domain.User;
import com.project.authService.exception.UserAlreadyPresentException;
import com.project.authService.exception.UserNotFoundException;
import com.project.authService.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService{

    @Autowired
    private UserRepo userRepo;
    @Override
    public User login(User user)throws UserNotFoundException{
        if(userRepo.findById(user.getEmail()).isPresent()){
            return userRepo.findByEmailAndPassword(user.getEmail(),user.getPassword());

        }
        else
            throw new UserNotFoundException();

    }

    @Override
    public User register(User user){
            return userRepo.save(user);

    }

    @Override
    public User UpdateUser(String email,User upadetedUser)throws UserNotFoundException {
        if(userRepo.findById(email).isEmpty()){
            throw new UserNotFoundException();
        }
        User existingUser = userRepo.findById(email).get();

        if(upadetedUser.getName() != null){
            existingUser.setName(upadetedUser.getName());
        }
        if(upadetedUser.getPhoneNo() != 0L){
            existingUser.setPhoneNo(upadetedUser.getPhoneNo());
        }
        if(upadetedUser.getImageName() != null){
            existingUser.setImageName(upadetedUser.getImageName());
        }
        return userRepo.save(existingUser);
    }

    @Override
    public void removeUser(String email) {
        User user = userRepo.findById(email).get();
        if(user.getEmail().equals(email)){
            userRepo.deleteById(email);
        }

    }
}
