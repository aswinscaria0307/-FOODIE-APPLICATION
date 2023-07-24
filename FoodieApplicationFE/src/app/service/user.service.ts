import { EventEmitter, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Address } from '../model/address';
@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private httpClient:HttpClient) { }

  // authUrl:String="http://localhost:9099/api/v1/auth";
  // userUrl:String="http://localhost:8090/api/v1/userService";
  authUrl:String="http://localhost:8081/api/v1/auth";
  userUrl:String="http://localhost:8081/api/v1/userService";
  addressChanged:EventEmitter<boolean>=new EventEmitter();


  login(loginData:any):Observable<any>{
    return this.httpClient.post(`${this.authUrl}/login`,loginData);
  }

  registerUser(formData:FormData):Observable<any>{
     return this.httpClient.post(`${this.userUrl}/register/user`,formData);
  }
  
  updateUser(formData:FormData){
    let httpHeader = new HttpHeaders({
      "Authorization":"Bearer "+localStorage.getItem('jwt')
    });
    let reqOption  = {headers:httpHeader}
    console.log(reqOption);
    console.log(formData);
    
    return this.httpClient.put(`${this.userUrl}/update/user`,formData,reqOption)
  }

  getProfileImg(){
    let httpHeader = new HttpHeaders({
      "Authorization":"Bearer "+localStorage.getItem('jwt')
    });
    let reqOption  = {headers:httpHeader}
    console.log(reqOption);
    return this.httpClient.get(`${this.userUrl}/get/profile`,reqOption);
  }

  getUserName(){
    let httpHeader = new HttpHeaders({
      "Authorization":"Bearer "+localStorage.getItem('jwt')
    });
    let reqOption  = {headers:httpHeader}
    console.log(reqOption);
    return this.httpClient.get(`${this.userUrl}/getName`,reqOption)
  }
  addItemToFavourites(itemData:any){
    let httpHeader = new HttpHeaders({
      "Authorization":"Bearer "+localStorage.getItem('jwt')
    });
    let reqOption  = {headers:httpHeader}
    return this.httpClient.post(`${this.userUrl}/add/item`,itemData,reqOption)
  }
  getListOfFavourites():Observable<Array<any>>{
    let httpHeader = new HttpHeaders({
      "Authorization":"Bearer "+localStorage.getItem('jwt')
    });
    let reqOption  = {headers:httpHeader}    
    return this.httpClient.get<Array<any>>(`${this.userUrl}/get/user/favourite`,reqOption)
  }
  removeFavourite(itemRemoved:any){
    let httpHeader = new HttpHeaders({
      "Authorization":"Bearer "+localStorage.getItem('jwt')
    });
    let reqOption  = {headers:httpHeader}
    return this.httpClient.delete(`${this.userUrl}/remove/favourite?itemId=${itemRemoved}`,reqOption);
  }
  changeAddress(address:Address){
    let httpHeader = new HttpHeaders({
      "Authorization":"Bearer "+localStorage.getItem('jwt')
    });
    let reqOption  = {headers:httpHeader}
    return this.httpClient.post(`${this.userUrl}/add/address`,address,reqOption)
  }
  getAddress():Observable<Address|any>{
    let httpHeader = new HttpHeaders({
      "Authorization":"Bearer "+localStorage.getItem('jwt')
    });
    let reqOption  = {headers:httpHeader}
    return this.httpClient.get(`${this.userUrl}/get/address`,reqOption)
  }
}
