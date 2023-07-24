import { Component, OnInit } from '@angular/core';
import { UserService } from '../service/user.service';
import { Router } from '@angular/router';
import { LoginService } from '../service/login.service';
import { FoodieService } from '../service/foodie.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Order } from '../model/order';
@Component({
  selector: 'app-favourites',
  templateUrl: './favourites.component.html',
  styleUrls: ['./favourites.component.css']
})
export class FavouritesComponent implements OnInit  {
  favList:any;
  addtocart!:Order;

  constructor(private userSer:UserService,private router:Router,private login:LoginService,private foodie:FoodieService,private userService:UserService,private _sanckBar:MatSnackBar){}

  ngOnInit(): void {
    this.userSer.getListOfFavourites().subscribe({
      next:data=>{
        console.log(data);
         this.favList = data;
      } 
    });
    
  }
  removeItem(data:any){
   this.userSer.removeFavourite(data).subscribe({
    next:data=>{
      console.log("item removed");
      this.ngOnInit();
      this.router.navigateByUrl("/favourite")
    }
   })
  }

  addToOrder(theItem:any){
    if(this.login.isLoggedIn){
      this.addtocart = new Order();
      this.addtocart.customerId = localStorage.getItem('email') ?? '';
      this.addtocart.billingAddress=localStorage.getItem("address")?? '';
      if (theItem) {
        theItem.status="incart";
        theItem.count=1;
        if (!this.addtocart.items) {
          this.addtocart.items = [];
        }
        this.addtocart.items = this.addtocart.items.concat(theItem);
        this.foodie.insertOrder(this.addtocart).subscribe(data=>{
          this._sanckBar.open(`Item Added to Cart`, 'success', {
            duration: 3000,
            panelClass: ['mat-toolbar', 'mat-primary']
          });
        });
        this.router.navigateByUrl("/cart")
        }
  }
  
  }

}
