import { Component, Input, OnInit } from '@angular/core';
import { RestaurantServiceService } from '../service/restaurant-service.service';
import Typed from 'typed.js' ;

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit{
  constructor(){}
  ngOnInit(): void {
   const typed = new Typed('.typing',{      //.typing is a target element // we, need to install Typed class from typed.js library
                                             // npm install typed.js
    strings:['your food now 😋 ',' different deserts..!!','many more..'],
    typeSpeed:100,
    backSpeed:60,
    loop:true
   })
  }
  
}
