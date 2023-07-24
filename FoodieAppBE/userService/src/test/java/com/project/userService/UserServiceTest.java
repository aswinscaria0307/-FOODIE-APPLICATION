package com.project.userService;

import com.project.userService.domain.Address;
import com.project.userService.domain.FavouritesCart;
import com.project.userService.domain.User;
import com.project.userService.domain.UserDto;
import com.project.userService.exceptions.FavouriteItemNotFoundException;
import com.project.userService.exceptions.UserAlreadyExistException;
import com.project.userService.exceptions.UserNotFoundException;
import com.project.userService.proxy.UserProxy;
import com.project.userService.repository.UserRepo;
import com.project.userService.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.internal.connection.tlschannel.util.Util.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {
   @Mock
    private UserRepo userRepo;
   @Mock
   private UserProxy userProxy;

   @InjectMocks
    private UserService userService;
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
    }
    @AfterEach
    public void tearDown(){
        favouritesCart=null;
        user=null;
        userRepo.deleteAll();
    }

   @Test
    public void addUserSuccess(){
        UserDto userDto = new UserDto(user.getEmail(),user.getPassword(),user.getName(),user.getRole(),user.getPhoneNo(),user.getImageName());
        when(userRepo.findById(user.getEmail())).thenReturn(Optional.ofNullable(null));
        when(userRepo.save(user)).thenReturn(user);
        when(userProxy.registerUser(userDto)).thenReturn(null);
        User addedUser = userService.addUser(user);
        assertEquals(user,addedUser);
        verify(userRepo, times(1)).save(user);
   }

   @Test
   public void updateUser() throws UserNotFoundException {
       byte[] newProfileImg = new byte[]{0x00, 0x01, 0x02, 0x03};
        User updatedUser = new User(user.getEmail(),user.getPassword(),"Aman",8799326731L,user.getRole(),user.getAddress(),user.getCity(),newProfileImg,"newImg",user.getListFavouritesCart());
        UserDto userDto = new UserDto(user.getEmail(),user.getPassword(),updatedUser.getName(),user.getRole(),updatedUser.getPhoneNo(),updatedUser.getImageName());
        when(userRepo.findById(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepo.save(updatedUser)).thenReturn(updatedUser);
        when(userProxy.updateUser(userDto,user.getEmail())).thenReturn(null);
        User update = userService.updateUser(user.getEmail(),updatedUser);
        verify(userRepo, times(2)).findById(user.getEmail());
        verify(userRepo, times(1)).save(update);
   }

   @Test
   public void updateUserFailure(){
        byte[] newProfileImg = new byte[]{0x00, 0x01, 0x02, 0x03};
       User updatedUser = new User(user.getEmail(),user.getPassword(),"Aman",8799326731L,user.getRole(),user.getAddress(),user.getCity(),newProfileImg,"newImg",user.getListFavouritesCart());
       UserDto userDto = new UserDto(user.getEmail(),user.getPassword(),updatedUser.getName(),user.getRole(),updatedUser.getPhoneNo(),updatedUser.getImageName());
       when(userRepo.findById(user.getEmail())).thenReturn(Optional.empty());
       assertThrows(UserNotFoundException.class,()->userService.updateUser(user.getEmail(),updatedUser));
       verify(userRepo, times(1)).findById(user.getEmail());
   }

   @Test
   public void getUserNameSuccess() throws UserNotFoundException {
        when(userRepo.findById(user.getEmail())).thenReturn(Optional.of(user));
        User getUserName = userService.getUserName(user.getEmail());
        assertEquals(getUserName.getName(),user.getName());
        verify(userRepo, times(1)).findById(user.getEmail());
   }

   @Test
    public void addFavouriteInListSuccess() throws UserNotFoundException {
        when(userRepo.findById(user.getEmail())).thenReturn(Optional.ofNullable(user));
        when(userRepo.save(user)).thenReturn(user);
        FavouritesCart favItem = new FavouritesCart(23,"Pasta",199f,5f,"abs.jpg");
        User updatedUser = userService.addFavouritesInList(user.getEmail(),favItem);
        List<FavouritesCart> listOfItem = updatedUser.getListFavouritesCart();
        assertTrue(listOfItem.contains(favItem));
   }


   @Test
    public void getListOfItems() throws FavouriteItemNotFoundException {
        when(userRepo.findById(user.getEmail())).thenReturn(Optional.ofNullable(user));
        List<FavouritesCart> listOfCart = userService.getListOfFavouriteById(user.getEmail());
        assertEquals(1,listOfCart.size());
        verify(userRepo, times(2)).findById(user.getEmail());
   }

   @Test
    public void getListOfItemsFailure(){
        when(userRepo.findById(user.getEmail())).thenReturn(Optional.ofNullable(null));
        assertThrows(FavouriteItemNotFoundException.class,()->userService.getListOfFavouriteById(user.getEmail()));
        verify(userRepo, times(1)).findById(user.getEmail());
   }

   @Test
    public void removeFavouriteByIdSuccess() throws FavouriteItemNotFoundException {
        FavouritesCart listToRemove = new FavouritesCart(23,"Pasta",199f,5f,"abs.jpg");
        when(userRepo.findById(user.getEmail())).thenReturn(Optional.of(user));
        userService.removeFavouriteById(user.getEmail(),listToRemove.getItemId());
        verify(userRepo, times(2)).findById(user.getEmail());
        verify(userRepo, times(1)).save(user);
   }
   @Test
    public void removeFavouriteByIdFailure(){
       FavouritesCart listToRemove = new FavouritesCart(23,"Pasta",199f,5f,"abs.jpg");
       when(userRepo.findById(user.getEmail())).thenReturn(Optional.empty());
       assertThrows(FavouriteItemNotFoundException.class,()->userService.removeFavouriteById(user.getEmail(),23));
       verify(userRepo, times(1)).findById(user.getEmail());
   }
    @Test
    public void setAddressSuccess(){
        Address address = new Address("123", "Central Park", "Main Street", "New York", 10001);
        user.setAddress(address);
        when(userRepo.findById(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepo.save(user)).thenReturn(user);
        userService.addAddress(user.getEmail(),address);
        assertEquals(address,user.getAddress());
        verify(userRepo,times(1)).findById(user.getEmail());
        verify(userRepo,times(1)).save(user);
    }
    @Test
    public void setAddressFailure(){
        Address address = new Address("123", "Central Park", "Main Street", "New York", 10001);
        user.setAddress(address);
        when(userRepo.findById(user.getEmail())).thenReturn(Optional.ofNullable(null));
        userService.addAddress(user.getEmail(),address);
        assertEquals(address,user.getAddress());
        verify(userRepo,times(1)).findById(user.getEmail());
        verify(userRepo,times(0)).save(user);
    }
    @Test
    public void getAddressSuccess(){
        when(userRepo.findById(user.getEmail())).thenReturn(Optional.of(user));
        userService.getAddress(user.getEmail());
        assertEquals(address,user.getAddress());
        verify(userRepo,times(1)).findById(user.getEmail());
    }
    @Test
    public void getAddressFailure(){
        byte[] profileImg = new byte[]{0x00, 0x01, 0x02, 0x03};
        User user1 = new User("abc@gmail.com","abc@2410","ABC",7685943456L,"user",null,"New Delhi",profileImg,"abc.jpg",favouritesCart);

        when(userRepo.findById(user1.getEmail())).thenReturn(Optional.ofNullable(null));
        userService.getAddress(user1.getEmail());
        assertEquals(null,user1.getAddress());
        verify(userRepo,times(1)).findById(user1.getEmail());
    }
}
