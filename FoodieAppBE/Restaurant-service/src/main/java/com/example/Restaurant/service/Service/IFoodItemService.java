package com.example.Restaurant.service.Service;

import com.example.Restaurant.service.domain.FoodItems;
import com.example.Restaurant.service.exception.RestaurantNotFoundException;

import java.util.List;

public interface IFoodItemService {
    public FoodItems addItem(FoodItems items,int restaurantId) throws RestaurantNotFoundException;
    public List<FoodItems> getAllItems();
    public FoodItems getById(int itemId);
    public FoodItems getByName(String itemName);
    public FoodItems updateFoodItem(FoodItems items,int itemId);
    public boolean deleteById(int itemId);
}
