package com.project.userService;

import com.project.userService.domain.Address;
import com.project.userService.domain.FavouritesCart;
import com.project.userService.domain.User;
import com.project.userService.repository.UserRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class UserRepoTest {
    @Autowired
    private UserRepo userRepo;
    private User user;
    private List<FavouritesCart> favouritesCart;

    @BeforeEach
    public void setUp(){

       FavouritesCart cartItem = new FavouritesCart(23,"Pasta",199f,5f,"abs.jpg");
       favouritesCart = new ArrayList<>();
       favouritesCart.add(cartItem);
        byte[] profileImg = new byte[]{0x00, 0x01, 0x02, 0x03};
       user = new User("abc@gmail.com","abc@2410","ABC",7685943456L,"user",new Address("123","abc block","11,back street","New Delhi",110041),"Delhi",profileImg,"abc.jpg",favouritesCart);
    }
    @AfterEach
    public void tearDown(){
          favouritesCart=null;
          user=null;
          userRepo.deleteAll();
    }

    @Test
    public void addUser(){
       User user1 = userRepo.save(user);
       assertEquals(user.getEmail(),user1.getEmail());
       assertEquals(user.getPhoneNo(),user1.getPhoneNo());
    }

    @Test
    public void addUserFailure(){
        User user1 = userRepo.save(user);
        assertNotEquals(user.getEmail(),"niit@gmail.com");
    }

    @Test
    public void addFavouritesInListSuccess(){
        User user1 = userRepo.save(user);
        List<User> users = userRepo.findAll();
        int size = users.size();
        assertEquals(1,size);
        assertNotEquals(2,size);
    }

    @Test
    public void deleteFavItem(){
        User user1 = userRepo.save(user);
        List<FavouritesCart> listOfFav = user1.getListFavouritesCart();
        Iterator<FavouritesCart>itr = listOfFav.iterator();
        while (itr.hasNext()){
            FavouritesCart item = itr.next();
            if(item.getItemName().equals("Pasta")){
                itr.remove();
            }
        }
        System.out.println(listOfFav.size());
      assertEquals(0,listOfFav.size());
    }

}
