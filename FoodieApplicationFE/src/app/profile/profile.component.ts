import { Component, OnInit } from '@angular/core';
import { UserService } from '../service/user.service';
import { DomSanitizer,SafeUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  userImg:SafeUrl='';
  userInfo:any;

constructor(private userService:UserService,private sanitizer:DomSanitizer){}

  ngOnInit(): void {
    this.userService.getProfileImg().subscribe((data: any) => {
      if(data && data.imageData){
        const imageData = data.imageData;
        const binaryData = atob(imageData);
        const arrayBuffer = new ArrayBuffer(binaryData.length);
        const uint8Array = new Uint8Array(arrayBuffer);
        for (let i = 0; i < binaryData.length; i++) {
          uint8Array[i] = binaryData.charCodeAt(i);
        }
        const blob = new Blob([uint8Array], { type: 'image/png' });
        this.userImg = this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(blob));
      }
    });

    this.userService.getUserName().subscribe((data:any)=>{
        this.userInfo=data;
        console.log(this.userInfo);
        
    })
    
  }
}
