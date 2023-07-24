import { Injectable } from '@angular/core';
import { Item } from '../model/item';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { PaymentOrder } from '../model/paymentOrder';
import { Order } from '../model/order';

@Injectable({
  providedIn: 'root'
})
export class FoodieService {
  private url1:String;
  private url2:String;
  private url3:String;


  constructor(private http:HttpClient) {
      // this.url1= 'http://localhost:8070/api/v1/order/';
      // this.url2= 'http://localhost:1122/api/v1/payment/';
      // this.url3= 'http://localhost:7171/api/v1/notification/';
      this.url1= 'http://localhost:8081/api/v1/order/';
      this.url2= 'http://localhost:8081/api/v1/payment/';
      this.url3= 'http://localhost:8081/api/v1/notification/';
      
   }

   public getItems(status:String,email:String|null):Observable<Item[]>{
    const url=`${this.url1}getItems/${status}/${email}`;
    return this.http.get<Item[]>(url);
   }

   public insertOrder(order:Order):Observable<any>{
    let httpHeader=new HttpHeaders({
      'Authorization':'Bearer '+localStorage.getItem('jwt')
    });
    let requestOptions={headers:httpHeader}
    const url=`${this.url1}insertOrder`;
    return this.http.post<Order>(url,order,requestOptions);
   }


   public addItem(email:String|null,item:Item):Observable<any>{
    let httpHeader=new HttpHeaders({
      'Authorization':'Bearer '+localStorage.getItem('jwt')
    });
    let requestOptions={headers:httpHeader}
    const url=`${this.url1}addItem/${email}`;
    return this.http.post<Item>(url,item,requestOptions);
   }

   public removeItem(email:String|null,item:Item):Observable<any>{
    let httpHeader=new HttpHeaders({
      'Authorization':'Bearer '+localStorage.getItem('jwt')
    });
    let requestOptions={headers:httpHeader}
    const url=`${this.url1}removeItem/${email}`;
    return this.http.post<Item>(url,item,requestOptions);
   }

   public createPaymentOrder(data:PaymentOrder){
    console.log(data);
    const url=`${this.url2}createOrder`;
    return this.http.post<PaymentOrder>(url,data);
   }

   cancelOrder(items: Item[], email: string | null) {
    const httpHeader = new HttpHeaders({
      'Authorization': 'Bearer ' + localStorage.getItem('jwt')
    });
    const requestOptions = {
      headers: httpHeader,
      body: items
    };
    const url = `${this.url1}cancelOrder/${email}`;
    return this.http.delete<Item[]>(url, requestOptions);
  }
  placeOrder(items: Item[], email: string | null) {
    const httpHeader = new HttpHeaders({
      'Authorization': 'Bearer ' + localStorage.getItem('jwt')
    });
    const requestOptions = {headers: httpHeader};    
    const url = `${this.url1}placeOrder/${email}`;
    return this.http.post<Item[]>(url,items,requestOptions);
  }
  
  sendMail(email:string|null,subject:string,body:string){
    const url=`${this.url3}email/${email}`;
    return this.http.post(url,{subject,body});
  }
  sendSms(destinationNumber:string|null,message:string){
    const url=`${this.url3}sms`;
    return this.http.post(url,{message,destinationNumber});
  }
   
}
