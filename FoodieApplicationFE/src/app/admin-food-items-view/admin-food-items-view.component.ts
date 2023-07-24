import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RestaurantServiceService } from '../service/restaurant-service.service';

@Component({
  selector: 'app-admin-food-items-view',
  templateUrl: './admin-food-items-view.component.html',
  styleUrls: ['./admin-food-items-view.component.css']
})
export class AdminFoodItemsViewComponent {

  constructor(private restaurant:RestaurantServiceService,private route:Router){}
  data:number | undefined;
  ngOnInit(): void {
    this.data = 51;
    this.restaurantId = this.restaurant.id;
    this.restaurant.getAllItems(+this.restaurantId).subscribe(data=>{
      console.log(data);
      
      this.restaurants1 = data;
      console.log(this.restaurants1);
          })
  }

  restaurants1:any=[];
  restaurantId!: number;
getItems() {
        this.restaurant.getAllItems(+this.restaurantId).subscribe(data=>{
          this.restaurants1 = data;
        })
      }

          
delete(item:any){
    this.restaurant.deleteItem(item,this.restaurantId).subscribe(
      data=>{
            alert("item is deleted");
            this.getItems();
    },
    error=>{
            alert("You are not authorized to delete the item>> ");
    }
    )
    let arr = this.restaurants1.filter((res:{restaurantId:any})=>{
      if(res.restaurantId==item){
        return false;
      }
      else{
        return true;
      }
    })
    this.restaurants1=arr;
}

update(){
  this.route.navigateByUrl("/addItem");
}
}
