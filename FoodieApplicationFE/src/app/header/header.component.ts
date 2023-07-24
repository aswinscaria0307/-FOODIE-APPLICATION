import { Component, EventEmitter, OnInit, Sanitizer } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { UserService } from '../service/user.service';
import { LoginService } from '../service/login.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FoodieService } from '../service/foodie.service';
import { Item } from '../model/item';
import {MatBadgeModule} from '@angular/material/badge';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})

export class HeaderComponent implements OnInit {
  imageSrc:SafeUrl='';
  status!:String;
  adminLoggedIn=false;
  userName:String = "";
  isVisible:boolean =false;
  search:boolean=false;
  cartCount:number=0;

  constructor(private userService:UserService,private sanitizer:DomSanitizer,private logSer:LoginService,private router:Router,private _sanckBar:MatSnackBar,private foodie:FoodieService){}
  ngOnInit(): void {
    this.status="login";
    this.logSer.userLoggedIn.subscribe((email:boolean)=>{      
      if(email==true){
        this.userService.getUserName().subscribe((data:any)=>{
          this.userName = data.name;
          });
          this.userService.getProfileImg().subscribe((data:any)=>{
            if(data && data.imageData){
              const imageData = data.imageData;
              const binaryData = atob(imageData);
              const arrayBuffer = new ArrayBuffer(binaryData.length);
              const uint8Array = new Uint8Array(arrayBuffer);
              for (let i = 0; i < binaryData.length; i++) {
                uint8Array[i] = binaryData.charCodeAt(i);
              }
              const blob = new Blob([uint8Array], { type: 'image/png' });
              this.imageSrc = this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(blob));
            }
          });
          
      this.status="logout";
      }
    });


    this.logSer.adminLogged.subscribe((admin:boolean)=>{      
      if(admin==true){
      this.adminLoggedIn=true;
      }
    });

    this.logSer.search.subscribe((s:boolean)=>{
      if(s==true){
        this.search=true;
      }else{
        this.search=false;
      }
    })

    this.logSer.cartCount.subscribe((num)=>{
      this.cartCount=num;
    })

    document.addEventListener("DOMContentLoaded",function(){
      window.addEventListener("scroll",function(){
        if(this.scrollY>120){
          document.querySelector('#navbar')?.classList.add("sticky");
        }
        else{
          document.querySelector('#navbar')?.classList.remove("sticky");
        }
      });
    });
  } 


  clicked(){
    if(this.status==="logout"){
      localStorage.clear();
      this._sanckBar.open('Logged out successfully.....', 'success', {
        duration: 3000,
        panelClass: ['mat-toolbar', 'mat-primary']
      });
      this.logSer.onFailure();
      this.userName='';
      this.imageSrc='';
      this.isVisible=false;
      this.adminLoggedIn=false;
      this.router.navigateByUrl("/login");
      this.status="login";
        }
  }
  toCart(){
    this.router.navigateByUrl("/cart");
  }
  clickedList(){
    if(!this.isVisible){
      this.isVisible=true;
    }
    else{
      this.isVisible=false;
    }
  }
  navigate(){
    if(this.adminLoggedIn){
      this.router.navigateByUrl("admin");
    }else{
      this.router.navigateByUrl("");
    }
  }
  onSearchTextChanged(searchText:string){
    this.logSer.searchElement.emit(searchText);
  }
  hideUserList(){
    this.isVisible=false;
  }
  
  hideMenu() {
    const checkbox = document.getElementById("check")as HTMLInputElement;
    if (checkbox) {
      checkbox.checked = false;
    }
  }
}
