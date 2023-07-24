package com.example.Restaurant.service;


import com.example.Restaurant.service.Repository.RestaurantRepository;
import com.example.Restaurant.service.domain.FoodItems;
import com.example.Restaurant.service.domain.Restaurant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class RestaurantRepositoryTest {

    @Autowired
    RestaurantRepository restaurantRepository;

    private Restaurant restaurant;
    private FoodItems foodItems;

    @BeforeEach
    public void setUp(){
        foodItems = new FoodItems(20, "pizza", 500,"", "https://img.freepik.com/free-photo/mixed-pizza-with-various-ingridients_140725-3790.jpg?w=2000", 5);
        List<FoodItems> foodItemsList = new ArrayList<>();
        foodItemsList.add(foodItems);
        restaurant = new Restaurant(501, "The Spice Factory", "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8cmVzdGF1cmFudHxlbnwwfHwwfHx8MA%3D%3D&w=1000&q=80", "Bangalore", 5.0, foodItemsList);
    }

    @AfterEach
    public void tearDown(){
        restaurant=null;
        foodItems=null;
        restaurantRepository.deleteAll();
    }

    @Test
    public void testSaveRestaurantIfSuccess(){
        Restaurant restaurant1 = restaurantRepository.save(restaurant);
        assertEquals(restaurant1,restaurant);
    }

    @Test
    public void testSaveRestaurantIfFailure() {
        restaurantRepository.insert(restaurant);
        assertThrows(DuplicateKeyException.class, () -> restaurantRepository.insert(restaurant));
    }

    @Test
    public void testFindAllRestaurant(){
        restaurantRepository.insert(restaurant);
        restaurant.setRestaurantId(502);
        restaurantRepository.insert(restaurant);
        restaurant.setRestaurantId(503);
        List<Restaurant> restaurants = restaurantRepository.findAll();
        assertEquals(2,restaurants.size());
    }

    @Test
    public void testFindByRestaurantIdIfSuccess() {
        restaurantRepository.save(restaurant);
        Restaurant foundRestaurant = restaurantRepository.findById(restaurant.getRestaurantId()).orElse(null);
        assertNotNull(foundRestaurant);
        assertEquals(restaurant.getRestaurantId(), foundRestaurant.getRestaurantId());
    }

    @Test
    public void testFindByRestaurantIdIfFailure() {
        Restaurant foundRestaurant = restaurantRepository.findById(501).orElse(null);
        assertNull(foundRestaurant);
    }

    @Test
    public void testFindByLocationIfSuccess() {
        restaurantRepository.save(restaurant);
        Restaurant foundRestaurant = restaurantRepository.findByLocation(restaurant.getLocation());
        assertNotNull(foundRestaurant);
        assertEquals(restaurant.getLocation(), foundRestaurant.getLocation());
    }
    @Test
    public void testFindByLocationIfFailure() {
        Restaurant foundRestaurant = restaurantRepository.findByLocation("New York");
        assertNull(foundRestaurant);
    }
    @Test
    public void testFindByRatingIfSuccess() {
        restaurantRepository.insert(restaurant);
        Restaurant foundRestaurant = restaurantRepository.findByRating(5.0);
        assertEquals(restaurant.getRating(), foundRestaurant.getRating());

    }
    @Test
    public void testFindByRatingIfNotFailure() {
        Restaurant foundRestaurant = restaurantRepository.findByRating(4.5);
        assertNull(foundRestaurant);
    }

    @Test
    public void testDeleteRestaurant(){
        restaurantRepository.insert(restaurant);
        restaurantRepository.deleteById(restaurant.getRestaurantId());
        List<Restaurant> restaurants = restaurantRepository.findAll();
        assertEquals(0,restaurants.size());
    }
    @Test
    public void testDeleteByIdIfNotExists() {
        assertDoesNotThrow(() -> restaurantRepository.deleteById(501));
    }

    @Test
    public void testGetFoodItems() {
        restaurantRepository.save(restaurant);
        Optional<Restaurant> restaurant1 = restaurantRepository.findById(restaurant.getRestaurantId());
        Restaurant retrievedRestaurant = restaurant1.get();
        FoodItems newFoodItem = new FoodItems(21, "burger", 300,"", "https://example.com/burger.jpg", 4);
        retrievedRestaurant.getItems().add(newFoodItem);
        restaurantRepository.save(retrievedRestaurant);
        Optional<Restaurant> restaurant2 = restaurantRepository.findById(restaurant.getRestaurantId());
        Restaurant updatedRestaurant = restaurant2.get();
        assertEquals(2, updatedRestaurant.getItems().size());
    }

    @Test
    public void testUpdateFoodItem() {
        restaurantRepository.save(restaurant);
        Optional<Restaurant> restaurant1 = restaurantRepository.findById(restaurant.getRestaurantId());
        Restaurant retrievedRestaurant = restaurant1.get();
        FoodItems updatedFoodItem = new FoodItems(20, "updated pizza", 600,"", "https://updated-url.com", 4);
        retrievedRestaurant.getItems().forEach(item -> {
            if (item.getItemId() == updatedFoodItem.getItemId()) {
                item.setItemName(updatedFoodItem.getItemName());
                item.setItemPrice(updatedFoodItem.getItemPrice());
                item.setImageUrl(updatedFoodItem.getImageUrl());
                item.setItemRating(updatedFoodItem.getItemRating());
            }
        });
        Restaurant updatedRestaurant = restaurantRepository.save(retrievedRestaurant);

        Optional<FoodItems> foodItems1 = updatedRestaurant.getItems()
                .stream()
                .filter(item -> item.getItemId() == updatedFoodItem.getItemId())
                .findFirst();
        FoodItems retrievedUpdatedFoodItem = foodItems1.get();
        assertEquals(updatedFoodItem.getItemName(), retrievedUpdatedFoodItem.getItemName());
        assertEquals(updatedFoodItem.getItemPrice(), retrievedUpdatedFoodItem.getItemPrice());
        assertEquals(updatedFoodItem.getImageUrl(), retrievedUpdatedFoodItem.getImageUrl());
        assertEquals(updatedFoodItem.getItemRating(), retrievedUpdatedFoodItem.getItemRating());
    }

    @Test
    public void testDeleteFoodItem() {
        restaurantRepository.save(restaurant);
        Optional<Restaurant> restaurant1 = restaurantRepository.findById(restaurant.getRestaurantId());
        Restaurant restaurant2 = restaurant1.get();
        Optional<FoodItems> optionalFoodItem = restaurant2.getItems()
                .stream()
                .filter(item -> item.getItemId() == foodItems.getItemId())
                .findFirst();
        FoodItems foodItems1 = optionalFoodItem.get();
        restaurant2.getItems().remove(foodItems1);
        Restaurant updatedRestaurant = restaurantRepository.save(restaurant2);
        assertEquals(0,updatedRestaurant.getItems().size());
    }

//    @Test
//    public void testDeleteFoodItemIfFailure() {
//        assertThrows(RestaurantNotFoundException.class, () -> restaurantRepository.deleteById(restaurant.getRestaurantId()));
//    }

}
