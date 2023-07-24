import { Component, OnInit } from '@angular/core';
import { Item } from '../model/item';
import { FoodieService } from '../service/foodie.service';
import { RazorpayResponse } from '../model/razorPay';
import { PaymentOrder } from '../model/paymentOrder';
import { Router } from '@angular/router';
import { Address } from '../model/address';
import { MatDialog } from '@angular/material/dialog';
import { AddressDialogComponent } from '../address-dialog/address-dialog.component';
import { UserService } from '../service/user.service';
import { LoginService } from '../service/login.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DatePipe } from '@angular/common';

declare var Razorpay:any;

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit{
  items:Item[]=[];
  theItem:Item|null=null;
   total:number=0;
   subtotal:number=0;
   shipping:number=0;
   address:Address | undefined;
   time1!: string;
   time2!: string;

constructor(private foodie:FoodieService,private route:Router,private dialog: MatDialog,private user:UserService,private login:LoginService,private _sanckBar:MatSnackBar){
}
ngOnInit(): void {
  this.login.findCartCount();
  this.updateCurrentTime();
  this.user.getAddress().subscribe((response:Address)=>{
    this.address=response;
  });
  this.user.addressChanged.subscribe((res)=>{
    this.ngOnInit();
  })
  this.foodie.getItems("incart",localStorage.getItem('email')).subscribe(response=>{
    console.log(response);
    this.items=response; 
    this.totalPrice();   
  },(error)=>{
    this.items=[];
    this.totalPrice();   
  });
}
updateCurrentTime() {
  const currentDate = new Date();
    currentDate.setMinutes(currentDate.getMinutes() + 15);
    const datePipe = new DatePipe('en-US');
    this.time1 = datePipe.transform(currentDate, 'h:mm:ss a') || '';
    currentDate.setMinutes(currentDate.getMinutes() + 15);
    this.time2 = datePipe.transform(currentDate, 'h:mm:ss a') || ''; 
}

remove(itemName?:string){
  if(itemName!=null){
    let remove:Item |undefined=this.items.find(item => item.itemName === itemName)
    console.log(remove);
    if(remove!=undefined){
    this.foodie.removeItem(localStorage.getItem('email'),remove).subscribe(response=>{
      this.ngOnInit();
      this.totalPrice();
          });
    }
  }
} 
 
add(itemName?:string){
  if(itemName!=null){
    let add:Item |undefined=this.items.find(item => item.itemName === itemName)
    console.log(add);
    if(add!=undefined){
    this.foodie.addItem(localStorage.getItem('email'),add).subscribe(responce=>{
      this.ngOnInit();
      this.totalPrice();
    })
    }
  }
}

totalPrice(){
  this.total = this.items.map(item => {
    if (item.itemPrice&&item.count) {
      return item.itemPrice*item.count;
    }
    return 0;
  })
  .reduce((acc, val) => acc + val, 0);
  this.subtotal=this.total;
  if(this.total>250){
    this.shipping=0;    
  }
  if(this.total<250 && this.total>0){
    this.shipping=50;    
  }
  if(this.total<250  && this.total>0){
    this.subtotal=this.total+this.shipping;
  }
  
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
   goToPayment =()=>{
    let data:PaymentOrder={orderAmount:this.subtotal,orderInfo:"order_request"};
    this.foodie.createPaymentOrder(data).subscribe((response:any)=>{
      console.log(response); 
      if(response.status=='created'){
        //open payment form
        var options={
          key:'rzp_test_pVaEM3TS2Oh7JN',
          amount:response.amount,
          currency:'INR',
          name:'Foodie',
          description:'order total amount',
          image:'data:image/png;base64,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',
          order_id:response.id,
          handler: (response: RazorpayResponse) => {
            console.log(response.razorpay_payment_id);
            console.log(response.razorpay_order_id);
            console.log(response.razorpay_signature);
            this.paymentSuccess(); // Call the paymentSuccess() function
          },
          prefill: {
            name: "",
            email: "",
            contact: ""
              },
          notes: {
            address: "Razorpay Corporate Office"
                
                },
          theme: {
            color: "#3399cc"
                }              
        };
        var rzp = new Razorpay(options) as any;
rzp.on('payment.failed', function (response:any){
console.log(response.error.code);
console.log(response.error.description);
console.log(response.error.source);
console.log(response.error.step);
console.log(response.error.reason);
console.log(response.error.metadata.order_id);
console.log(response.error.metadata.payment_id);
 
});
rzp.open();
      }
    },failure=>{
      console.log(failure);
      alert("Something went wrong...")
    })
  }

  paymentSuccess() {
    this.foodie.placeOrder(this.items,localStorage.getItem("email")).subscribe(response=>{
      this._sanckBar.open('Order Placed successfully.....', 'success', {
        duration: 5000,
        panelClass: ['mat-toolbar', 'mat-primary']
      });
      setTimeout(() => {
        this.route.navigateByUrl("/myOrders");
      }, 2500);
      },(error) => {
      console.error("payment done but order not placed contact customer care:", error);
    })
          let body="";
          for (const item of this.items) {
            body += `${item.itemName} ( ${item.count} items on the way), `;
            }
            body = body.slice(0, -2);

    this.foodie.sendMail(localStorage.getItem("email"), "Order Placed Successfully",body )
      .subscribe(() => {
        console.log("Email sent successfully");
      }, (error) => {
        console.error("Error sending email:", error);
      });
      // this.foodie.sendSms(localStorage.getItem("number"), body )//
      // .subscribe(() => {
      //   console.log("Sms sent successfully");
      // }, (error) => {
      //   console.error("Error sending Sms:", error);
      // });
  }

  changeAddress(){
      const dialogRef = this.dialog.open(AddressDialogComponent, {
        width: '700px',
      });
  }
  
}

