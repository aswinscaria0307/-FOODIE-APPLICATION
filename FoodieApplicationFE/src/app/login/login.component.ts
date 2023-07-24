import { Component, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../service/user.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { LoginService } from '../service/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit,OnDestroy{

  responseData:any;

  constructor(private fb:FormBuilder,private userService:UserService,private _sanckBar:MatSnackBar,private route:Router,private loginStatus:LoginService){}
  ngOnInit(): void {
    this.loginStatus.search.emit(true);
  }
  ngOnDestroy(): void {
    this.loginStatus.search.emit(false);
  }
  loginForm = this.fb.group({
    email:["",[Validators.required,this.checkIfGuestEmailsAreValid]],
    password:["",[Validators.required,Validators.minLength(7)]]
  })

  get email(){
    return this.loginForm.get('email');
  }

  get password(){
    return this.loginForm.get('password')
  }

  checkIfGuestEmailsAreValid(c: AbstractControl) {
 
    if (c.value !== '') {
      const emailString = c.value;
      const emails = emailString.split(',').map((e: string) => e.trim());
      const emailRegex = /^(([^<>()[\]\.,;:\s@\"]+(\.[^<>()[\]\.,;:\s@\"]+)*)|(\".+\"))@(gmail\.com|yahoo\.com)$/i;
      const anyInvalidEmail = emails.every((e: string) => e.match(emailRegex) !== null);
      if (!anyInvalidEmail) {
        return { checkIfGuestEmailsAreValid: false }
      }
    }
    return null;
  }

  loginCheck(){    
      this.userService.login(this.loginForm.value).subscribe({
        next:data=>{
          this.responseData=data; 
          localStorage.setItem('jwt',this.responseData.Token);
          localStorage.setItem('email',this.responseData.email);
          localStorage.setItem("role",this.responseData.role);
          console.log(localStorage.getItem("jwt"));
          this._sanckBar.open('Logged In successfully.....', 'success', {
            duration: 3000,
            panelClass: ['mat-toolbar', 'mat-primary']
          });
          if(this.responseData.role==="adminRole"){
            this.route.navigateByUrl('/admin');
            this.loginStatus.loginSuccess();
            this.loginStatus.adminLoginSuccess();
            this.loginStatus.getLoginStatus();
            this.loginStatus.admin();
          }else{
          this.loginStatus.loginSuccess();
          this.loginStatus.getLoginStatus();
          this.route.navigateByUrl("");
          }
        },error:(err)=>{
           this.loginStatus.onFailure()
          alert("Authorization failed, Please check the provided details again!")
        }
      })
  }
  showHidePass(){
    const password1 = document.getElementById('password') as HTMLInputElement;
    const toggler = document.getElementById('toggler') as HTMLElement;
  
    if(password1.type==='password'){
      password1.setAttribute('type','text');
      toggler.classList.add('fa-eye-slash');
    }
    else{
      toggler.classList.remove('fa-eye-slash');
      password1.setAttribute('type','password');
     }
    }
}
