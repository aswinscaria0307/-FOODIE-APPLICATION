package com.project.authService;

import com.project.authService.domain.User;
import com.project.authService.exception.UserAlreadyPresentException;
import com.project.authService.exception.UserNotFoundException;
import com.project.authService.repository.UserRepo;
import com.project.authService.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ServiceTesting {
    @Mock
    private UserRepo userRepo;
    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp(){
        user = new User("xyz@gmail.com","Xyz@2410","XYZ",8767894535L,"user","xyz.jpg");
    }
    @AfterEach
    public void tearDown(){
        user =null;
        userRepo.deleteAll();
    }

    @Test
    public void loginSuccess() throws UserNotFoundException {
        when(userRepo.findById(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepo.findByEmailAndPassword(user.getEmail(),user.getPassword())).thenReturn(user);

        User loginUser = userService.login(user);
        assertEquals(user,loginUser);
        verify(userRepo, times(1)).findById(user.getEmail());
        verify(userRepo, times(1)).findByEmailAndPassword(user.getEmail(),user.getPassword());
    }

    @Test
    public void loginFailure(){
        when(userRepo.findById(user.getEmail())).thenReturn(Optional.ofNullable(null));
        assertThrows(UserNotFoundException.class,()->userService.login(user));
        verify(userRepo, times(0)).findByEmailAndPassword(user.getEmail(),user.getPassword());

    }

    @Test
    public void registerUser() throws UserAlreadyPresentException {
        when(userRepo.save(user)).thenReturn(user);
        User addUser = userService.register(user);
        assertEquals(user,addUser);
        verify(userRepo, times(1)).save(addUser);
    }

    @Test
    public void updateUserSuccess() throws UserNotFoundException {
        User updatedUser = new User(user.getEmail(),user.getPassword(),"ABC",9456784538L,user.getRole(),"abcd.jpg");
        when(userRepo.findById(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepo.save(updatedUser)).thenReturn(user);

        User result = userService.UpdateUser(user.getEmail(),updatedUser);
        assertEquals(updatedUser.getName(),result.getName());
        assertEquals(updatedUser.getPhoneNo(),result.getPhoneNo());
        verify(userRepo, times(2)).findById(user.getEmail());
    }

    @Test
    public void updateUserFailure(){
        User updatedUser = new User(user.getEmail(),user.getPassword(),"ABC",9456784538L,user.getRole(),"abcd.jpg");
        when(userRepo.findById(user.getEmail())).thenReturn(Optional.ofNullable(null));
        when(userRepo.save(updatedUser)).thenReturn(user);

        assertThrows(UserNotFoundException.class,()->userService.UpdateUser(user.getEmail(),updatedUser));

        verify(userRepo, times(1)).findById(user.getEmail());
    }

}
