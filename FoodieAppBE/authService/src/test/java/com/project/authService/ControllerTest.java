package com.project.authService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.authService.controller.UserController;
import com.project.authService.domain.User;
import com.project.authService.domain.UserDto;
import com.project.authService.exception.UserAlreadyPresentException;
import com.project.authService.exception.UserNotFoundException;
import com.project.authService.service.TokenGenerator;
import com.project.authService.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private TokenGenerator tokenGenerator;

    @InjectMocks
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;

    private User user;
    private UserDto userDto;

    @BeforeEach
    public void setUp(){
    userDto = new UserDto("xyz@gmail.com","Xyz@2410","XYZ","user",8767894535L,"xyz.jpg");
    mockMvc= MockMvcBuilders.standaloneSetup(userController).build();
    }
    @AfterEach
    public void tearDown(){
     userDto=null;
    }

    @Test
    public void registerUserSuccess() throws Exception {
        User user = new User(userDto.getEmail(),userDto.getPassword(),userDto.getName(),userDto.getPhoneNo(),userDto.getRole(),userDto.getImageName());
        when(userService.register(user)).thenReturn(user);
        mockMvc.perform(
                post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(user)))
                .andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
        verify(userService, times(1)).register(user);
    }

    @Test
    public void loginSuccess() throws Exception {
        User user = new User(userDto.getEmail(),userDto.getPassword(),userDto.getName(),userDto.getPhoneNo(),userDto.getRole(),userDto.getImageName());
        User retrieved=new User(userDto.getEmail(),userDto.getPassword(),userDto.getName(),userDto.getPhoneNo(),userDto.getRole(),userDto.getImageName());
        when(userService.login(user)).thenReturn(retrieved);

        Map<String,String>tokenGenerated=new HashMap<>();
        tokenGenerated.put("token","Bearer msklmkd");

        when(tokenGenerator.storeToken(retrieved)).thenReturn(tokenGenerated);
        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(convertToJson(user)))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(userService, times(1)).login(user);
    }

    @Test
    public void loginFailure() throws Exception {
        User user = new User(userDto.getEmail(),userDto.getPassword(),userDto.getName(),userDto.getPhoneNo(),userDto.getRole(),userDto.getImageName());

        when(userService.login(user)).thenReturn(null);

        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(convertToJson(user)))
                .andExpect(status().isNotFound()).andDo(MockMvcResultHandlers.print());
        verify(userService, times(1)).login(user);
    }

    @Test
    public void userUpdateSuccess() throws Exception {
        UserDto userDto = new UserDto("xyz@gmail.com","Xyz@2410","ABC","user",8767894535L,"abc.jpg");
        User userUpdated = new User(userDto.getEmail(),userDto.getPassword(),userDto.getName(),userDto.getPhoneNo(),userDto.getRole(),userDto.getImageName());
        when(userService.UpdateUser(userDto.getEmail(),userUpdated)).thenReturn(userUpdated);
        String email = userDto.getEmail();
        mockMvc.perform(put("/api/v1/auth/updateUser/{email}",email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(userDto)))
                .andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
        verify(userService, times(1)).UpdateUser(userDto.getEmail(),userUpdated);
    }


    @Test
    public void userUpdateFailure() throws Exception {
        UserDto userDto = new UserDto("xyz@gmail.com","Xyz@2410","ABC","user",8767894535L,"abc.jpg");
        User userUpdated = new User(userDto.getEmail(),userDto.getPassword(),userDto.getName(),userDto.getPhoneNo(),userDto.getRole(),userDto.getImageName());
       when(userService.UpdateUser(userDto.getEmail(),userUpdated)).thenThrow(UserNotFoundException.class);
        String email = userDto.getEmail();
       mockMvc.perform(put("/api/v1/auth/updateUser/{email}",email)
               .contentType(MediaType.APPLICATION_JSON)
               .content(convertToJson(userDto)))
               .andExpect(status().isNotFound()).andDo(MockMvcResultHandlers.print());
       verify(userService, times(1)).UpdateUser(userDto.getEmail(),userUpdated);
    }

    public  static  String convertToJson(final Object object){
        String result="";
        ObjectMapper mapper = new ObjectMapper();
        try {
            result = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
