package com.example.Restaurant.service;

import com.example.Restaurant.service.Repository.FoodItemRepository;
import com.example.Restaurant.service.Repository.RestaurantRepository;
import com.example.Restaurant.service.Service.RestaurantService;
import com.example.Restaurant.service.domain.FoodItems;
import com.example.Restaurant.service.domain.Restaurant;
import com.example.Restaurant.service.exception.FoodItemNotFoundException;
import com.example.Restaurant.service.exception.RestaurantAlreadyExistsException;
import com.example.Restaurant.service.exception.RestaurantNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTest {

    @Mock
    RestaurantRepository restaurantRepository;

    @Mock
    FoodItemRepository foodItemRepository;

    @InjectMocks
    RestaurantService restaurantService;

    private Restaurant restaurant;
    private FoodItems foodItems;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        foodItems = new FoodItems(20, "pizza", 500,"pizza is very tasty", "https://img.freepik.com/free-photo/mixed-pizza-with-various-ingridients_140725-3790.jpg?w=2000", 5);
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
    public void testSaveRestaurantIfSuccess() throws RestaurantAlreadyExistsException {
        when(restaurantRepository.findById(restaurant.getRestaurantId())).thenReturn(Optional.empty());
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
        Restaurant restaurant1 = restaurantService.addRestaurant(restaurant);
        assertEquals(restaurant1, restaurant);
        verify(restaurantRepository, times(1)).findById(restaurant.getRestaurantId());
        verify(restaurantRepository, times(1)).save(restaurant);
    }
    @Test
    public void testSaveRestaurantIfFailure(){
        when(restaurantRepository.findById(restaurant.getRestaurantId())).thenReturn(Optional.of(restaurant));
        assertThrows(RestaurantAlreadyExistsException.class,()->restaurantService.addRestaurant(restaurant));
        verify(restaurantRepository,times(1)).findById(restaurant.getRestaurantId());
        verify(restaurantRepository,times(0)).save(restaurant);
    }

    @Test
    public void testGetAllItemsIfSuccess() throws RestaurantNotFoundException {
        when(restaurantRepository.findById(restaurant.getRestaurantId())).thenReturn(Optional.of(restaurant));
        List<FoodItems> retrievedFoodItems = restaurantService.getItems(restaurant.getRestaurantId());
//        assertNotNull(retrievedFoodItems);
        assertEquals(1, retrievedFoodItems.size());
        verify(restaurantRepository, times(1)).findById(restaurant.getRestaurantId());
    }

    @Test
    public void testGetAllItemsIfFailure(){

        when(restaurantRepository.findById(restaurant.getRestaurantId())).thenReturn(Optional.empty());
        assertThrows(RestaurantNotFoundException.class, () -> restaurantService.getItems(restaurant.getRestaurantId()));
        verify(restaurantRepository, times(1)).findById(restaurant.getRestaurantId());
    }
    @Test
    public void testGetAllRestaurantsIfSuccess(){
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant));
        List<Restaurant> restaurants = restaurantService.getAllRestaurant();
        assertEquals(1,restaurants.size());
        verify(restaurantRepository,times(1)).findAll();
    }
    @Test
    public void testGetLocation(){
        when(restaurantRepository.findByLocation("Bangalore")).thenReturn(restaurant);
        Restaurant restaurant1 = restaurantService.getRestaurantByLocation("Bangalore");
        assertEquals("Bangalore",restaurant1.getLocation());
        verify(restaurantRepository,times(1)).findByLocation("Bangalore");
    }

    @Test
    public void testGetRating(){
        when(restaurantRepository.findByRating(5.0)).thenReturn(restaurant);
        Restaurant restaurant1 = restaurantService.getByRating(5.0);
        assertEquals(5.0,restaurant1.getRating());
        verify(restaurantRepository,times(1)).findByRating(5.0);
    }
    @Test
    public void updateRestaurantIfSuccess() throws RestaurantNotFoundException {
        when(restaurantRepository.findById(restaurant.getRestaurantId())).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
        Restaurant result = restaurantService.updateRestaurant(restaurant, restaurant.getRestaurantId());
        assertEquals(restaurant.getRestaurantName(), result.getRestaurantName());
        assertEquals(restaurant.getLocation(), result.getLocation());
        verify(restaurantRepository, times(1)).findById(restaurant.getRestaurantId());
        verify(restaurantRepository, times(1)).save(restaurant);
    }

    @Test
    public void updateRestaurantIfFailure(){
        when(restaurantRepository.findById(restaurant.getRestaurantId())).thenReturn(Optional.empty());
        assertThrows(RestaurantNotFoundException.class,()->restaurantService.updateRestaurant(restaurant,restaurant.getRestaurantId()));
        verify(restaurantRepository,times(1)).findById(restaurant.getRestaurantId());
        verify(restaurantRepository,times(0)).save(restaurant);
    }
    @Test
    public void updateFoodItemIfSuccess() throws RestaurantNotFoundException, FoodItemNotFoundException {

        when(restaurantRepository.findById(restaurant.getRestaurantId())).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
        Restaurant result = restaurantService.updateFoodItem(restaurant.getRestaurantId(), foodItems);
        assertNotNull(result);
        assertEquals(foodItems.getItemName(), result.getItems().get(0).getItemName());
        assertEquals(foodItems.getItemPrice(), result.getItems().get(0).getItemPrice());
        verify(restaurantRepository, times(1)).findById(restaurant.getRestaurantId());
        verify(restaurantRepository, times(1)).save(restaurant);
    }

    @Test
    public void updateFoodItemIfFailure(){
        when(restaurantRepository.findById(restaurant.getRestaurantId())).thenReturn(Optional.empty());
        assertThrows(RestaurantNotFoundException.class,()->restaurantService.updateFoodItem(restaurant.getRestaurantId(),foodItems));
        verify(restaurantRepository,times(1)).findById(restaurant.getRestaurantId());
        verify(restaurantRepository,times(0)).save(restaurant);
    }


    @Test
    public void deleteRestaurantIfSuccess() throws RestaurantNotFoundException {
        when(restaurantRepository.findById(restaurant.getRestaurantId())).thenReturn(Optional.of(restaurant));
        restaurantService.deleteById(restaurant.getRestaurantId());
        verify(restaurantRepository,times(1)).findById(restaurant.getRestaurantId());
        verify(restaurantRepository,times(1)).deleteById(restaurant.getRestaurantId());

    }

    @Test
    public void deleteRestaurantIfFailure(){
        when(restaurantRepository.findById(restaurant.getRestaurantId())).thenReturn(Optional.empty());
        assertThrows(RestaurantNotFoundException.class,()->restaurantService.deleteById(restaurant.getRestaurantId()));
        verify(restaurantRepository,times(1)).findById(restaurant.getRestaurantId());
        verify(restaurantRepository,times(0)).deleteById(restaurant.getRestaurantId());
    }

    @Test
    public void deleteFoodItemIfSuccess() throws RestaurantNotFoundException, FoodItemNotFoundException {
        when(restaurantRepository.findById(restaurant.getRestaurantId())).thenReturn(Optional.of(restaurant));
        restaurantService.deleteFoodItem(restaurant.getRestaurantId(),foodItems);
        assertEquals(0,restaurant.getItems().size());
        verify(restaurantRepository,times(1)).findById(restaurant.getRestaurantId());
        verify(restaurantRepository,times(1)).save(restaurant);
    }

    @Test
    public void deleteFoodItemIfFailure(){
        when(restaurantRepository.findById(restaurant.getRestaurantId())).thenReturn(Optional.empty());
        assertThrows(RestaurantNotFoundException.class,()->restaurantService.deleteFoodItem(restaurant.getRestaurantId(),foodItems));
        verify(restaurantRepository,times(1)).findById(restaurant.getRestaurantId());
        verify(restaurantRepository,times(0)).save(restaurant);
    }



}
