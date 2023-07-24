package com.example.Restaurant.service.controller;

import com.example.Restaurant.service.Repository.FoodItemRepository;
import com.example.Restaurant.service.Repository.RestaurantRepository;
import com.example.Restaurant.service.Service.IFoodItemService;
import com.example.Restaurant.service.Service.IRestaurantService;
import com.example.Restaurant.service.domain.FoodItems;
import com.example.Restaurant.service.domain.Restaurant;
import com.example.Restaurant.service.exception.FoodItemNotFoundException;
import com.example.Restaurant.service.exception.RestaurantAlreadyExistsException;
import com.example.Restaurant.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//@CrossOrigin
@RestController
@RequestMapping("api/v1/restaurant")
public class RestaurantController {
    IRestaurantService iRestaurantService;
    RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantController(IRestaurantService iRestaurantService,RestaurantRepository restaurantRepository){
        this.iRestaurantService = iRestaurantService;
        this.restaurantRepository = restaurantRepository;
    }
//    http://localhost:25500/api/v1/restaurant/addRestaurant

    @PostMapping("/addRestaurant")
    public ResponseEntity<?> addNewRestaurant(HttpServletRequest httpServletRequest, @RequestBody Restaurant restaurant) throws RestaurantAlreadyExistsException {
        if (httpServletRequest.getAttribute("attr2").equals("adminRole")){
            return new ResponseEntity<>(iRestaurantService.addRestaurant(restaurant), HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>("You are not authorized to add new restaurant",HttpStatus.BAD_REQUEST);
        }
    }
    //    http://localhost:25500/api/v1/restaurant/addItem/{id}
    @PostMapping("/addItem/{id}")
    public ResponseEntity<?> addFoodItemsToSameRestaurant(HttpServletRequest httpServletRequest, @PathVariable int id, @RequestBody List<FoodItems> items) throws RestaurantNotFoundException {
        if (httpServletRequest.getAttribute("attr2").equals("adminRole")) {
            Restaurant restaurant = restaurantRepository.findById(id).get();
            restaurant.getItems().addAll(items);
            Restaurant savedRestaurant = restaurantRepository.save(restaurant);
            return ResponseEntity.ok(savedRestaurant);
        }
        else {
            return new ResponseEntity<>("Restaurant not found",HttpStatus.BAD_REQUEST);
        }
    }
    //    http://localhost:25500/api/v1/restaurant/getItems/{id}
    @GetMapping("/getItems/{id}")
    public ResponseEntity<?> getAllFoodItems(@PathVariable int id) throws RestaurantNotFoundException {
        return new ResponseEntity<>(iRestaurantService.getItems(id),HttpStatus.OK);
    }

    //     http://localhost:25500/api/v1/restaurant/getRestaurant
    @GetMapping("/getRestaurant")
    public ResponseEntity<?> getAllRestaurants(){
        return new ResponseEntity<>(iRestaurantService.getAllRestaurant(),HttpStatus.OK);
    }

    //    http://localhost:25500/api/v1/restaurant/getLocation/{location}
    @GetMapping("/getLocation/{location}")
    public ResponseEntity<?> getRestaurantByLocation(@PathVariable String location){
        return new ResponseEntity<>(iRestaurantService.getRestaurantByLocation(location),HttpStatus.OK);
    }

    //     http://localhost:25500/api/v1/restaurant/getRating/{rating}
    @GetMapping("/getRating/{rating}")
    public ResponseEntity<?> getByRating(@PathVariable double rating){
        return new ResponseEntity<>(iRestaurantService.getByRating(rating),HttpStatus.OK);
    }

    //    http://localhost:25500/api/v1/restaurant/update/{id}
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRestaurant(HttpServletRequest httpServletRequest,@RequestBody Restaurant restaurant,@PathVariable int id) throws RestaurantNotFoundException {
        if (httpServletRequest.getAttribute("attr2").equals("adminRole")){
            return new ResponseEntity<>(iRestaurantService.updateRestaurant(restaurant,id),HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>("You are not authorized to update information",HttpStatus.BAD_REQUEST);
        }
    }
    //  http://localhost:25500/api/v1/restaurant/updateItem/{restaurantId}
    @PutMapping("/updateItem/{restaurantId}")
    public ResponseEntity<?> updateFoodItems(HttpServletRequest httpServletRequest, @PathVariable int restaurantId, @RequestBody FoodItems foodItems) throws RestaurantNotFoundException, FoodItemNotFoundException {
        System.out.println(httpServletRequest.getAttribute("attr2"));
        if (httpServletRequest.getAttribute("attr2") != null && httpServletRequest.getAttribute("attr2").equals("adminRole")) {
            return new ResponseEntity<>(iRestaurantService.updateFoodItem(restaurantId,foodItems), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Not authorized to update food items", HttpStatus.BAD_REQUEST);
        }
    }

    //    http://localhost:25500/api/v1/restaurant/delete/{id}
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRestaurant(HttpServletRequest httpServletRequest,@PathVariable int id) throws RestaurantNotFoundException {
        if (httpServletRequest.getAttribute("attr2").equals("adminRole")){
            return new ResponseEntity<>(iRestaurantService.deleteById(id),HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("You are not authorized to delete",HttpStatus.BAD_REQUEST);
        }
    }
    //    http://localhost:25500/api/v1/restaurant/deleteItem/{restaurantId}
    @PostMapping("/deleteItem/{id}")
    public ResponseEntity<?> deleteFoodItem(@PathVariable int id,@RequestBody FoodItems foodItems,HttpServletRequest httpServletRequest) throws RestaurantNotFoundException, FoodItemNotFoundException {
        System.out.println("Reached d");
        if (httpServletRequest.getAttribute("attr2")!=null && httpServletRequest.getAttribute("attr2").equals("adminRole")){
            return new ResponseEntity<>(iRestaurantService.deleteFoodItem(id,foodItems),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("You are not authorized to delete food item",HttpStatus.BAD_REQUEST);
        }
    }
}
