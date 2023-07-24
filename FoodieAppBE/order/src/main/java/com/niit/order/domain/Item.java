package com.niit.order.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Item {
    private int itemId;
    private String itemName;
    private int itemPrice;
    private float itemRating;
    private String imageUrl;
    private int count;
    private String status;
}
