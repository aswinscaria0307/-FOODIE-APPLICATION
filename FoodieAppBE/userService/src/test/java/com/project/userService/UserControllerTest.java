package com.project.userService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.userService.controller.UserController;
import com.project.userService.domain.Address;
import com.project.userService.domain.FavouritesCart;
import com.project.userService.domain.User;
import com.project.userService.exceptions.FavouriteItemNotFoundException;
import com.project.userService.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;



import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;
    @Autowired
    private MockMvc mockMvc;
    private Address address;

    private User user;
    private List<FavouritesCart> favouritesCart;

    @BeforeEach
    public void setUp(){
        FavouritesCart cartItem = new FavouritesCart(23,"Pasta",199f,5f,"abs.jpg");
        favouritesCart = new ArrayList<>();
        favouritesCart.add(cartItem);
        address=new Address("102","iritty","madathil","kannur",670703);
        byte[] profileImg = new byte[]{0x00, 0x01, 0x02, 0x03};
        user = new User("abc@gmail.com","abc@2410","ABC",7685943456L,"user",address,"Delhi",profileImg,"abc.jpg",favouritesCart);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }
    @AfterEach
    public void tearDown(){
        favouritesCart=null;
        user=null;
    }

    @Test
    public void addUser() throws Exception {
        when(userService.addUser(Mockito.any(User.class))).thenReturn(user);
        MockMultipartFile file= new MockMultipartFile("file","abc.jpg","image/jpeg","content".getBytes());

        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/api/v1/userService/register/user")
                                        .file(file)
                                        .param("userData",convertToJson(user)))
                                .andExpect(MockMvcResultMatchers.status().isCreated()).andDo(MockMvcResultHandlers.print());
        verify(userService, times(1)).addUser(Mockito.any(User.class));

    }

    @Test
    public void addItemSuccess() throws Exception {
        FavouritesCart favItem = new FavouritesCart(23,"Pasta",199f,5f,"abs.jpg");
        when(userService.addFavouritesInList(user.getEmail(),favItem)).thenReturn(user);
        mockMvc.perform(
                post("/api/v1/userService/add/item").requestAttr("attr1","abc@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(favItem))
        ).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(userService, times(1)).addFavouritesInList(user.getEmail(),favItem);
    }

    @Test
    public void addItemFailure() throws Exception {
        FavouritesCart favItem = new FavouritesCart(23,"Pasta",199f,5f,"abs.jpg");

        mockMvc.perform(
                post("/api/v1/userService/add/item").requestAttr("attr1","")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(favItem))
        ).andExpect(status().isBadRequest()).andDo(MockMvcResultHandlers.print());
        verify(userService, times(0)).addFavouritesInList(user.getEmail(),favItem);
    }

    @Test
    public void getUserFav() throws Exception {
       when(userService.getListOfFavouriteById(user.getEmail())).thenReturn(favouritesCart);
       mockMvc.perform(
               get("/api/v1/userService/get/user/favourite").requestAttr("attr1","abc@gmail.com"))
               .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
       verify(userService,times(1)).getListOfFavouriteById(user.getEmail());
    }

    @Test
    public void getUserFavFailure() throws Exception {
        when(userService.getListOfFavouriteById("aman@gmail.com")).thenThrow(FavouriteItemNotFoundException.class);
        mockMvc.perform(
                get("/api/v1/userService/get/user/favourite")
                        .requestAttr("attr1","aman@gmail.com"))
                .andExpect(status().isNotFound()).andDo(MockMvcResultHandlers.print());
                verify(userService, times(1)).getListOfFavouriteById("aman@gmail.com");

    }

    @Test
    public void removeFavItem() throws Exception {
        String email = "abc@gmail.com";
       mockMvc.perform(delete("/api/v1/userService/remove/favourite")
                       .param("itemId",String.valueOf(23))
                       .requestAttr("attr1",email))
               .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
       verify(userService, times(1)).removeFavouriteById(email,23);
    }

    @Test
    public void getUserName() throws Exception {
        when(userService.getUserName("abc@gmail.com")).thenReturn(user);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("attr1","abc@gmail.com");
        mockMvc.perform(
                get("/api/v1/userService/getName").requestAttr("attr1","abc@gmail.com"))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(userService, times(1)).getUserName(user.getEmail());
    }

    @Test
    public void getUserNameFailure() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("attr1","");
        mockMvc.perform(
                        get("/api/v1/userService/getName").requestAttr("attr1",""))
                .andExpect(status().isBadRequest()).andDo(MockMvcResultHandlers.print());
        verify(userService, times(0)).getUserName(user.getEmail());
    }
    @Test
    public void addAddress() throws Exception {
        when(userService.addAddress(user.getEmail(),address)).thenReturn(true);
        mockMvc.perform(post("/api/v1/userService/add/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(address))
                        .requestAttr("attr1", user.getEmail()))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(userService, times(1)).addAddress(user.getEmail(),address);
    }

    @Test
    public void addAddressFailure() throws Exception {
        when(userService.addAddress(user.getEmail(),address)).thenReturn(false);
        mockMvc.perform(
                        post("/api/v1/userService/add/address")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertToJson(address))
                                .requestAttr("attr1", user.getEmail()))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(userService, times(1)).addAddress(user.getEmail(),address);
    }
    @Test
    public void getAddress() throws Exception {
        when(userService.getAddress(user.getEmail())).thenReturn(address);
        mockMvc.perform(get("/api/v1/userService/get/address")
                        .requestAttr("attr1", user.getEmail()))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(userService, times(1)).getAddress(user.getEmail());
    }

    @Test
    public void getAddressFailure() throws Exception {
        when(userService.getAddress(user.getEmail())).thenReturn(null);
        mockMvc.perform(
                        get("/api/v1/userService/get/address")
                                .requestAttr("attr1", user.getEmail()))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(userService, times(1)).getAddress(user.getEmail());
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
