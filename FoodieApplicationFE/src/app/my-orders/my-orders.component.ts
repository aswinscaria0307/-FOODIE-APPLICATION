import { Component, OnInit } from '@angular/core';
import { FoodieService } from '../service/foodie.service';
import { Item } from '../model/item';

@Component({
  selector: 'app-my-orders',
  templateUrl: './my-orders.component.html',
  styleUrls: ['./my-orders.component.css']
})
export class MyOrdersComponent implements OnInit{
  items:Item[]=[];
  itemsHistory:Item[]=[];
  constructor(private foodie:FoodieService){
    
  }
  ngOnInit(): void {
    this.foodie.getItems("ordered", localStorage.getItem('email')).subscribe(response => {
      console.log(response);
      this.items = this.items.concat(response);
    });

      this.foodie.getItems("packed", localStorage.getItem('email')).subscribe(response => {
        console.log(response);
        this.items = this.items.concat(response);
      });
      this.foodie.getItems("delivered", localStorage.getItem('email')).subscribe(response => {
        console.log(response);
        this.itemsHistory = this.itemsHistory.concat(response);
      });
  
        this.foodie.getItems("cancelled", localStorage.getItem('email')).subscribe(response => {
          console.log(response);
          this.itemsHistory = this.itemsHistory.concat(response);
        });
  }

  
  calculateTotalPrice(itemname: string | undefined): number {
    let itemTotal = 0;
    this.items.map((item) => {
      if (item.itemName == itemname && item.count && item.itemPrice) {
        itemTotal = item.count * item.itemPrice;
      }
    });
    return itemTotal;
  }
  calculateTotalPriceC(itemname: string | undefined): number {
    let itemTotal = 0;
    this.itemsHistory.map((item) => {
      if (item.itemName == itemname && item.count && item.itemPrice) {
        itemTotal = item.count * item.itemPrice;
      }
    });
    return itemTotal;
  }
  cancelOrder(itemname:string|undefined){
    alert("your order is cancelled")
    let items:Item[]=this.items.filter((item)=>item.itemName==itemname)
    this.foodie.cancelOrder(items,localStorage.getItem('email')).subscribe(response => {
      console.log(response);
      this.items=[];
      this.ngOnInit();
  }
);
  }
}
