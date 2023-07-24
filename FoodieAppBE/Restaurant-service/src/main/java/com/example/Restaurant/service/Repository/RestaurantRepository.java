package com.example.Restaurant.service.Repository;

import com.example.Restaurant.service.domain.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends MongoRepository<Restaurant,Integer> {

    public Restaurant findByLocation(String location);
    public Restaurant findByRating(double rating);
}
