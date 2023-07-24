import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';

import { Router } from '@angular/router';
import { RestaurantServiceService } from '../service/restaurant-service.service';

@Component({
  selector: 'app-add-items',
  templateUrl: './add-items.component.html',
  styleUrls: ['./add-items.component.css']
})
export class AddItemsComponent {
  userLoggedIn?:string;

  restaurantId:any;

  items:any =[]

  constructor(private fb:FormBuilder,private service:RestaurantServiceService,private route:Router){}

  addform:any=this.fb.group({
    "itemId":['',Validators.required],
   "itemName" :['',Validators.required],
   "description":['',Validators.required],
   "imageUrl":['',Validators.required],
  "itemPrice":['',Validators.required],
  "itemRating":['',Validators.required]
})

addItem(){
  if(this.addform.valid){
    this.items.push(this.addform.value);
    this.service.getId(this.restaurantId);
    this.service.addItem(this.items,this.restaurantId).subscribe(
      response=>{
        alert("items are added successfully");
        this.items=[];
        this.addform.reset();
        this.route.navigateByUrl('/adminFoodItem');
      },
      error=>{
        alert("add all the details");
      }
    )
  }
 
}

view(){
  this.route.navigateByUrl("/viewAdminItem");
}
update(){
  if(this.addform.valid){
    this.service.getId(this.restaurantId);
    this.service.updateItem(this.addform.value,this.restaurantId).subscribe(
      response=>{
        alert("items have been updated");
        this.addform.reset();
        this.route.navigateByUrl('/adminFoodItem');
      },
      error=>{
        alert("add all the items");
      }
    )
  }
}
}
