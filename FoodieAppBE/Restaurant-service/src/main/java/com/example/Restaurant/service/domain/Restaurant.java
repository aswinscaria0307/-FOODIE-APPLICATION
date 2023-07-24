package com.example.Restaurant.service.domain;

//attributes - restaurantId,restaurantName,restaurantImageUrl,location,rating,List<Item>
//attributes - itemId,itemName,price,rating,imgUrl

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
//@NoArgsConstructor
@Data
@Document
public class Restaurant {
    @Id
    private int restaurantId;
    private String restaurantName;
    private String imageUrl;
    private String location;
    private double rating;
    private List<FoodItems> items;

   public Restaurant(){
       this.items = new ArrayList<>();
   }

    public List<FoodItems> getItems() {
        return items;
    }

    public void setItems(List<FoodItems> items) {
        this.items = items;
    }
}
