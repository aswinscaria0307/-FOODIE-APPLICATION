import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { AddRestaurantComponent } from '../add-restaurant/add-restaurant.component';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent {

  constructor(private route:Router){}

  role2:any = localStorage.getItem("role");

  add(){
    this.route.navigateByUrl('/adminRestaurantView');
  }

  addFoodItem(){
    this.route.navigateByUrl('/adminFoodItem');
  }  
}
