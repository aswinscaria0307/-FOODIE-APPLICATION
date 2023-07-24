import { Component, Input } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RestaurantServiceService } from '../service/restaurant-service.service';

@Component({
  selector: 'app-admin-restaurant-view',
  templateUrl: './admin-restaurant-view.component.html',
  styleUrls: ['./admin-restaurant-view.component.css']
})
export class AdminRestaurantViewComponent {
  constructor(private service:RestaurantServiceService,private route:Router,private activatedRoute:ActivatedRoute){
  }
  ngOnInit(): void {
    this.service.getAllRestaurant().subscribe(
      response=>{
        this.restaurants=response;
      },
      error=>{
        alert("error!!!!");
      }
    )
  }

  getItems(id:number) {
    this.service.getId(id);
    this.route.navigateByUrl('/viewAdminItem');
    }

    @Input('restaurants') restaurants:any = [];

    getLocation(location:any){
      this.getLocation(location);
      this.service.getRestaurantByLocation(location).subscribe(
        (response: Object) => {
          const restaurantArray = response as any[];
          if(location && location !== ''){
            this.restaurants = restaurantArray.filter((restaurant:any) => restaurant.location === location);
          } else {
            this.restaurants = restaurantArray;
            console.log(restaurantArray);
            
          }
        },
        error=>{
          alert("error!!!!");
        }
      )
    }

    add(){
      this.route.navigateByUrl('/addRestaurant');
    }

    delete(id:any){
      this.service.deleteRestaurant(id).subscribe(
        response=>{
          alert("restaurant is deleted");
        },
        error=>{
          alert("delete is not possible !! You are not admin");
        }
      )
      let arr = this.restaurants.filter((res:{restaurantId:any})=>{
        if(res.restaurantId==id){
          return false;
        }
        else{
          return true;
        }
      })
      this.restaurants=arr;
    }
    update(){}
}
