import { EventEmitter, Injectable } from '@angular/core';
import { FoodieService } from './foodie.service';
import { Item } from '../model/item';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  isLoggedIn:boolean=false;
  adminLoggedIn:boolean=false;
  userLoggedIn:EventEmitter<boolean>=new EventEmitter();
  adminLogged:EventEmitter<boolean>=new EventEmitter();
  searchElement:EventEmitter<string>=new EventEmitter();
  search:EventEmitter<boolean>=new EventEmitter();
  cartCount:EventEmitter<number>=new EventEmitter();
  constructor(private foodie:FoodieService) { }

  loginSuccess(){
    this.isLoggedIn=true;
  }
  onFailure(){
    this.isLoggedIn=false;
  }
  adminLoginSuccess(){
    this.adminLoggedIn=true;
  }
  adminOnFailure(){
    this.adminLoggedIn=false;
  }

  getLoginStatus(){
    if(this.isLoggedIn){      
      this.userLoggedIn.emit(this.isLoggedIn)
    }
  }
  admin(){
    if(this.adminLoggedIn){      
      this.adminLogged.emit(this.adminLoggedIn)
    }
  }
  findCartCount(){
    this.foodie.getItems("incart",localStorage.getItem('email')).subscribe((response:Item[])=>{
      let itemss:Item[]=response;
      let count=0;
      itemss.map(item=>{
        if (item.count !== undefined) {
          count += item.count;
        }
       })
      this.cartCount.emit(count);
    },(error)=>{
      this.cartCount.emit(0);
    })
  }
}
