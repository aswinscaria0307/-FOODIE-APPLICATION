package com.project.authService.controller;

import com.project.authService.domain.User;
import com.project.authService.domain.UserDto;
import com.project.authService.exception.UserAlreadyPresentException;
import com.project.authService.exception.UserNotFoundException;
import com.project.authService.service.ITokenGenerator;
import com.project.authService.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin(origins = "http://localhost:4200" )
@RequestMapping("/api/v1/auth")
public class UserController {
    private IUserService iUserService;
    private ITokenGenerator iTokenGenerator;

    @Autowired
    public UserController(IUserService iUserService, ITokenGenerator iTokenGenerator) {
        this.iUserService = iUserService;
        this.iTokenGenerator = iTokenGenerator;
    }

    // http://localhost:9090/api/v1/auth/register
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto){


        User user  = new User(userDto.getEmail(),
                              userDto.getPassword(),
                              userDto.getName(),
                              userDto.getPhoneNo(),
                              userDto.getRole(),
                              userDto.getImageName());
        System.out.println(user.getRole());

        return new ResponseEntity<>(iUserService.register(user), HttpStatus.CREATED);

    }

    // http://localhost:9090/api/v1/auth/upadteUser

    @PutMapping("/updateUser/{email}")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto, @PathVariable String email) throws UserNotFoundException {
        User user  = new User(userDto.getEmail(),
                userDto.getPassword(),
                userDto.getName(),
                userDto.getPhoneNo(),
                userDto.getRole(),
                userDto.getImageName());
        return new ResponseEntity<>(iUserService.UpdateUser(email,user), HttpStatus.CREATED);
    }

    // http://localhost:9090/api/v1/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) throws UserNotFoundException {
        User retrievedUser = iUserService.login(user);
        if(retrievedUser!=null){
            return new ResponseEntity<>(iTokenGenerator.storeToken(retrievedUser),HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Authorization failed....",HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/user")
    public ResponseEntity<?> removeUser(@RequestParam String email){
        iUserService.removeUser(email);
        return new ResponseEntity<>("User removed",HttpStatus.OK);
    }

}
