package com.project.authService;

import com.project.authService.domain.User;
import com.project.authService.repository.UserRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepoTest {
    private User user;
    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    public void setUp(){
        user=new User("abc@gmail.com","abc@123","Abc",8574829184L,"user","abc.jpg");
    }
    @AfterEach
    public void tearDown(){
        user=null;
        userRepo.deleteAll();
    }

    @Test
    public void insertSuccess(){
        User insertData = userRepo.save(user);
        assertEquals(user.getEmail(),insertData.getEmail());
    }

    @Test
    public void insertUserFailure(){
        User user1 = new User("xyz@gmail.com","xyz@123","Abc",8574829184L,"user","xyz.jpg");
        User insertData = userRepo.save(user1);
        assertNotEquals(user,insertData);
    }

    @Test
    public void findUserByEmailAndPassword(){
        User user1 = new User("abc@gmail.com","abc@123","Abc",8574829184L,"user","abc.jpg");
        userRepo.save(user1);
        User getUser = userRepo.findByEmailAndPassword("abc@gmail.com","abc@123");
        assertNotNull(getUser);
        assertEquals(user1.getName(),getUser.getName());
    }

    @Test
    public void findUserByEmailAndPasswordFailure(){
        User user1 = new User("abc@gmail.com","abc@123","Abc",8574829184L,"user","abc.jpg");
        User user2 = new User("xyz@gmail.com","xyz@123","Xyz",8574829123L,"user","xyz.jpg");
        userRepo.save(user1);
        User getUser = userRepo.findByEmailAndPassword("abc@gmail.com","abc@123");
        assertNotNull(getUser);
        assertNotEquals(user2.getName(),getUser.getName());
    }
}
