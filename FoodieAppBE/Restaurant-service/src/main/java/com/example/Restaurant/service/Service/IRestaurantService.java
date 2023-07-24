package com.example.Restaurant.service.Service;

import com.example.Restaurant.service.domain.FoodItems;
import com.example.Restaurant.service.domain.Restaurant;
import com.example.Restaurant.service.exception.FoodItemNotFoundException;
import com.example.Restaurant.service.exception.RestaurantAlreadyExistsException;
import com.example.Restaurant.service.exception.RestaurantNotFoundException;

import java.util.List;

public interface IRestaurantService {
    public Restaurant addRestaurant(Restaurant restaurant) throws RestaurantAlreadyExistsException;   //save
    public List<Restaurant> getAllRestaurant();  //findAll
    public Restaurant getRestaurantByLocation(String location); //findByLocation
    public Restaurant getByRating(double rating); //findByRating
    public Restaurant updateRestaurant(Restaurant restaurant,int restaurantId) throws RestaurantNotFoundException;   //save -update
    public Restaurant getById(int restaurantId);     //findById
    public boolean deleteById(int restaurantId) throws RestaurantNotFoundException;
    public List<FoodItems> getItems(int id) throws RestaurantNotFoundException;
     public Restaurant updateFoodItem(int restaurantId, FoodItems newFoodItem) throws RestaurantNotFoundException, FoodItemNotFoundException ;
    public boolean deleteFoodItem(int restaurantId, FoodItems foodItems) throws RestaurantNotFoundException, FoodItemNotFoundException;
}
