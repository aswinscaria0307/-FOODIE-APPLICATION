import { HttpClient, HttpHeaders } from '@angular/common/http';
import { EventEmitter, Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RestaurantServiceService {

  constructor(private http:HttpClient) { }
  resId: EventEmitter<number> = new EventEmitter();
  id:number=0;
  location:string='';
  baseUrl:string="http://localhost:8081/api/v1/restaurant/";

  getAllRestaurant(){
    return this.http.get(this.baseUrl+"getRestaurant");
  }

  getAllItems(id:number){
    console.log(`Hi + ${id}`);
    return this.http.get(`http://localhost:8081/api/v1/restaurant/getItems/${id}`);
    
  }

  getId(id:number){
    
    if(id){
      this.id = id;
      this.resId.emit(id);
    }
  }

  getLocation(location:string){
    this.location = location;
    console.log(location);
    
  }

  getRestaurantByLocation(location:string){
    return this.http.get(`http://localhost:8081/api/v1/restaurant/getLocation/${location}`);
  }



  addRestaurant(addRestaurant:any){
    let httpHeader = new HttpHeaders({
      'Authorization':'Bearer ' +localStorage.getItem("jwt")
    });
    console.log(localStorage.getItem("Token"));
    let requestOption = {headers:httpHeader}
    return this.http.post(`http://localhost:8081/api/v1/restaurant/addRestaurant`,addRestaurant,requestOption);
    
  }

  updateRestaurant(updateRes:any,id:number){
    let httpHeader = new HttpHeaders({
      'Authorization':'Bearer ' +localStorage.getItem("jwt")
    });
    let requestOption = {headers:httpHeader}
    return this.http.put(`http://localhost:8081/api/v1/restaurant/update/${id}`,updateRes,requestOption);
  }

  deleteRestaurant(id:number){
    let httpHeader = new HttpHeaders({
      'Authorization':'Bearer ' +localStorage.getItem("jwt")
    });
    console.log(localStorage.getItem("Token"));
    let requestOption = {headers:httpHeader}
    return this.http.delete(`http://localhost:8081/api/v1/restaurant/delete/${id}`,requestOption);
  }

  addItem(addItem:any,id:any){
    console.log(`Hi + ${id}`);
    let httpHeader = new HttpHeaders({
      'Authorization':'Bearer ' +localStorage.getItem("jwt")
    });
    let requestOption = {headers:httpHeader}
    return this.http.post(`http://localhost:8081/api/v1/restaurant/addItem/${id}`,addItem,requestOption);
  }

  updateItem(updateItem:any,id:number){
    let httpHeader = new HttpHeaders({
      'Authorization':'Bearer ' +localStorage.getItem("jwt")
    });
    let requestOption = {headers:httpHeader}
    return this.http.put(`http://localhost:8081/api/v1/restaurant/updateItem/${id}`,updateItem,requestOption);
  }

  deleteItem(deleteItem:any,id:number){
    let httpHeader = new HttpHeaders({
      'Authorization':'Bearer ' +localStorage.getItem("jwt")
    });
    let requestOption = {headers:httpHeader}
    console.log(deleteItem);
    console.log(id);    
    return this.http.post(`http://localhost:8081/api/v1/restaurant/deleteItem/${id}`,deleteItem,requestOption);
  }
}

// user part
// http://localhost:25500/api/v1/restaurant/getRestaurant
// http://localhost:25500/api/v1/restaurant/getItems/{id}

// search 
// http://localhost:25500/api/v1/restaurant/getRating/{rating}
// http://localhost:25500/api/v1/restaurant/getLocation/{location}


// admin part -restaurant
// http://localhost:25500/api/v1/restaurant/update/{id}
// http://localhost:25500/api/v1/restaurant/addRestaurant
// http://localhost:25500/api/v1/restaurant/delete/{id}


// admin part - food items
// http://localhost:25500/api/v1/restaurant/addItem/{id}
// http://localhost:25500/api/v1/restaurant/updateItem/{restaurantId}/{itemId}
// http://localhost:25500/api/v1/restaurant/deleteItem/{restaurantId}/{itemId}
