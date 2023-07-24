package com.project.userService.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.userService.domain.Address;
import com.project.userService.domain.FavouritesCart;
import com.project.userService.domain.User;
import com.project.userService.exceptions.FavouriteItemNotFoundException;
import com.project.userService.exceptions.UserAlreadyExistException;
import com.project.userService.exceptions.UserNotFoundException;
import com.project.userService.service.IUserService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/userService")
public class UserController {
    private IUserService iUserService;

    @Autowired
    public UserController(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @Autowired
    ServletContext context;

    //http://localhost:8090/api/v1/userService/register/user
    @PostMapping("/register/user")
    public ResponseEntity<?>addUser(@RequestParam("file")MultipartFile file,@RequestParam("userData") String user) throws IOException {

        User user1 = new ObjectMapper().readValue(user, User.class);
        user1.setProfileImg(file.getBytes());
        String filename  = file.getOriginalFilename();

        String newFileName = FilenameUtils.getBaseName(filename)+"_"+System.currentTimeMillis()+"."+FilenameUtils.getExtension(filename);

        user1.setImageName(newFileName);
            return new ResponseEntity<>(iUserService.addUser(user1), HttpStatus.CREATED);

    }

    //http://localhost:8090/api/v1/userService/update/user

    @PutMapping("/update/user")
    public ResponseEntity<?> updateCurrentUser(HttpServletRequest request,@RequestParam(value="file",required = false)MultipartFile file,@RequestParam("userInfo") String user) throws IOException, UserNotFoundException {

        String email = (String)request.getAttribute("attr1");
        User user1 = new ObjectMapper().readValue(user, User.class);
        if(file!=null && !file.isEmpty()){
            user1.setProfileImg(file.getBytes());
            String filename  = file.getOriginalFilename();
            String newFileName = FilenameUtils.getBaseName(filename)+"_"+System.currentTimeMillis()+"."+FilenameUtils.getExtension(filename);
            System.out.println(newFileName);
            user1.setImageName(newFileName);
        }
       return new ResponseEntity<>(iUserService.updateUser(email,user1),HttpStatus.OK);

    }

    // http://localhost:8090/api/v1/userService/get/profile
    @GetMapping("/get/profile")
    public ResponseEntity<?> getUserImg(HttpServletRequest request){

        String email = (String)request.getAttribute("attr1");
        byte[] imageData = iUserService.getUserProfileImg(email);
        if(imageData==null){
            return new ResponseEntity<>("Not found",HttpStatus.NOT_FOUND);
        }
        String base64Image = Base64.getEncoder().encodeToString(imageData);
        Map<String,Object>  response = new HashMap<>();
        response.put("imageData",base64Image);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    //http://localhost:8090/api/v1/userService/getName
    @GetMapping("/getName")
    public ResponseEntity<?>getUserName(HttpServletRequest request){

            String email = (String)request.getAttribute("attr1");
            if(email.isEmpty()){
                System.out.println("if part get name");
                return new ResponseEntity<>("Not a valid Email",HttpStatus.BAD_REQUEST);
            }
            else {
                System.out.println("getName else part");
                return new ResponseEntity<>(iUserService.getUserName(email), HttpStatus.OK);
            }
    }

    //http://localhost:8090/api/v1/userService/add/item
    @PostMapping("/add/item")
    public ResponseEntity<?> addItem(HttpServletRequest request,@RequestBody FavouritesCart favouritesCart){
        String email = (String)request.getAttribute("attr1");
        if(email.isEmpty()){
            return new ResponseEntity<>("No value is Present for email",HttpStatus.BAD_REQUEST);
        }
        else
            return new ResponseEntity<>(iUserService.addFavouritesInList(email, favouritesCart),HttpStatus.OK);
    }


    //http://localhost:8090/api/v1/userService/get/user/favourite

    @GetMapping("/get/user/favourite")
    public ResponseEntity<?> getUserFavourite(HttpServletRequest request) throws FavouriteItemNotFoundException {

        String email = (String)request.getAttribute("attr1");

        return new ResponseEntity<>(iUserService.getListOfFavouriteById(email),HttpStatus.OK);

    }

    //http://localhost:8090/api/v1/userService/remove/favourite

    @DeleteMapping("/remove/favourite")
    public ResponseEntity<?> removeFavouritesFromList(HttpServletRequest request,@RequestParam int itemId) throws FavouriteItemNotFoundException {

       String email = (String)request.getAttribute("attr1");
        System.out.println("deleted");
        iUserService.removeFavouriteById(email,itemId);
       return new ResponseEntity<>(HttpStatus.OK);

    }

    //http://localhost:8090/api/v1/userService/check/list
    @GetMapping("/check/list")
    public ResponseEntity<?>checkList(HttpServletRequest request, @RequestParam int itemId){
        String email = (String)request.getAttribute("attr1");
        return new ResponseEntity<>(iUserService.favItemExist(email,itemId),HttpStatus.OK);
    }
    //http://localhost:8090/api/v1/userService/add/address
    @PostMapping("/add/address")
    public ResponseEntity<?> addAddress(HttpServletRequest request,@RequestBody Address address){
        String email = (String)request.getAttribute("attr1");
        return new ResponseEntity<>(iUserService.addAddress(email,address),HttpStatus.OK);
    }
    @GetMapping("/get/address")
    public ResponseEntity<?> getAddress(HttpServletRequest request){
        String email = (String)request.getAttribute("attr1");
        return new ResponseEntity<>(iUserService.getAddress(email),HttpStatus.OK);
    }
}
