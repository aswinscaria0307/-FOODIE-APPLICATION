import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';

import { Router } from '@angular/router';
import { RestaurantServiceService } from '../service/restaurant-service.service';
import { MatDialog } from '@angular/material/dialog';
import { AdminComponent } from '../admin/admin.component';

@Component({
  selector: 'app-add-restaurant',
  templateUrl: './add-restaurant.component.html',
  styleUrls: ['./add-restaurant.component.css']
})
export class AddRestaurantComponent {

  constructor(private fb:FormBuilder,private service:RestaurantServiceService,private route:Router){}

  role = localStorage.getItem("role");

  userLoggedIn?:string;
  restaurants:any=[];

  addform = this.fb.group({
    restaurantId: ['', Validators.required],
    restaurantName: ['', Validators.required],
    imageUrl: ['', Validators.required],
    location: ['', Validators.required],
    rating: ['', Validators.required]
  });
  

addRestaurant(){
  if(this.role === "adminRole"){
    if(this.addform.valid){
      this.service.addRestaurant(this.addform.value).subscribe(
        response=>{
          alert(`restaurant is added successfully`);
          this.addform.reset();
          this.route.navigateByUrl('/adminRestaurantView');
        },
        error=>{
          alert('add all the particulars');
        }
      )
    }
  }else{
    alert("You are not authorized to add or delete ");
  }
  
}

update(){
  if(this.addform.valid){
    const id = 1;
    this.service.updateRestaurant(this.addform.value,id).subscribe(
      response=>{
        alert(`restaurant has been updated`);
        this.addform.reset();
        this.route.navigateByUrl('/adminRestaurantView');
      },
      error=>{
        alert(`update is not possible`);
      }
    )
  }
}

view(){
  this.route.navigateByUrl('/restaurantView')
}

addItem(){
  const id=1;
  this.service.getId(id);
  this.addform.reset();
  this.route.navigateByUrl('/addItem');
}

}
