package com.project.userService.service;

import com.project.userService.domain.Address;
import com.project.userService.domain.FavouritesCart;
import com.project.userService.domain.User;
import com.project.userService.exceptions.FavouriteItemNotFoundException;
import com.project.userService.exceptions.UserAlreadyExistException;
import com.project.userService.exceptions.UserNotFoundException;

import java.util.List;

public interface IUserService {
    public User addUser (User user);
    public User addFavouritesInList (String email, FavouritesCart favouritesCart);
    public User updateUser(String email,User updatedUser) throws UserNotFoundException;
    public List<FavouritesCart> getListOfFavouriteById(String email)throws FavouriteItemNotFoundException;
    public void removeFavouriteById(String email,int itemId)throws FavouriteItemNotFoundException;
    public boolean favItemExist(String email,int itemId);
    public byte[] getUserProfileImg(String email);
    public User getUserName(String email);
    public boolean addAddress(String email, Address address);
    public Address getAddress(String email);


}
