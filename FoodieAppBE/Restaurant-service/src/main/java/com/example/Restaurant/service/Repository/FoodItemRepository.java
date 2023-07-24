package com.example.Restaurant.service.Repository;

import com.example.Restaurant.service.domain.FoodItems;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodItemRepository extends MongoRepository<FoodItems,Integer> {
    public FoodItems findByItemName(String itemName);
}
