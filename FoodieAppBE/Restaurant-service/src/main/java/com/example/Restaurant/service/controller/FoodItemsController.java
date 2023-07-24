package com.example.Restaurant.service.controller;

import com.example.Restaurant.service.Service.IFoodItemService;
import com.example.Restaurant.service.domain.FoodItems;
import com.example.Restaurant.service.exception.RestaurantNotFoundException;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v1/cuisine")
public class FoodItemsController {
    @Autowired
    IFoodItemService iFoodItemService;

//    http://localhost:25500/api/v1/cuisine/addNewCuisine/*

    @PostMapping("/addNewCuisine/{restaurantId}")
    public ResponseEntity<?> addCuisine(HttpServletRequest httpServletRequest, @RequestBody FoodItems foodItems,@PathVariable int restaurantId) throws RestaurantNotFoundException {
        if (httpServletRequest.getAttribute("attr2").equals("adminRole")){
            return new ResponseEntity<>(iFoodItemService.addItem(foodItems,restaurantId), HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>("You are not authorized to add new items", HttpStatus.CREATED);
        }
    }

//    http://localhost:25500/api/v1/cuisine/getAllItems
    @GetMapping("/getAllItems")
    public ResponseEntity<?> getAllItems(){
        return new ResponseEntity<>(iFoodItemService.getAllItems(),HttpStatus.OK);
    }

//    http://localhost:25500/api/v1/cuisine/getByName/{itemName}
    @GetMapping("/getByName/{itemName}")
    public ResponseEntity<?> getByName(@PathVariable String itemName){
        return new ResponseEntity<>(iFoodItemService.getByName(itemName),HttpStatus.OK);
    }

//    http://localhost:25500/api/v1/cuisine/getById/{id}

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getById(@PathVariable int id){
        return new ResponseEntity<>(iFoodItemService.getById(id),HttpStatus.OK);
    }

//    http://localhost:25500/api/v1/cuisine/update/{id}
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(HttpServletRequest httpServletRequest,@RequestBody FoodItems foodItems,@PathVariable int id){
        if (httpServletRequest.getAttribute("attr2").equals("adminRole")){
            return new ResponseEntity<>(iFoodItemService.updateFoodItem(foodItems,id),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("You are not authorized to update",HttpStatus.OK);
        }
    }

//    http://localhost:25500/api/v1/cuisine/delete/{id}
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(HttpServletRequest httpServletRequest,@PathVariable int id){
        if (httpServletRequest.getAttribute("attr2").equals("adminRole")){
            return new ResponseEntity<>(iFoodItemService.deleteById(id),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("You are not authorized to delete",HttpStatus.OK);
        }
    }
}
