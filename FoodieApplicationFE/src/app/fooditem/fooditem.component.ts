import { Component,Input } from '@angular/core';
import { RestaurantServiceService } from '../service/restaurant-service.service';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginService } from '../service/login.service';
@Component({
  selector: 'app-fooditem',
  templateUrl: './fooditem.component.html',
  styleUrls: ['./fooditem.component.css']
})
export class FooditemComponent {
  constructor(private service:RestaurantServiceService,private route:Router,private activatedRoute:ActivatedRoute,
    private loginService:LoginService){
  }
  ngOnInit(): void {
    this.service.getAllRestaurant().subscribe(
      response=>{
        this.restaurants=response;
        // alert("");
      },
      error=>{
        alert("error!!!!");
      }
    )
  }

  getItems(id:number) {
    this.loginService.isLoggedIn;
    this.service.getId(id);
    this.route.navigateByUrl('/adminFoodItemView');
    }

    @Input('restaurants') restaurants:any = [];

}
