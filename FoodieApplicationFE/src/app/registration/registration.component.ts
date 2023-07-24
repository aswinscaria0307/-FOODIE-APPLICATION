import { Component, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, Validators } from '@angular/forms';
import { UserService } from '../service/user.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LoginService } from '../service/login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit,OnDestroy{
  responseData:any;
  public uploadedImage:any=File;
  isDirty=false;

  constructor(private fb:FormBuilder,private userService:UserService,private _sanckBar:MatSnackBar,private loginStatus:LoginService,private router:Router){}
  ngOnInit(): void {
    this.loginStatus.search.emit(true);
     this.registrationForm.valueChanges.subscribe(() => {
      this.isDirty = true;
    });
  }
  ngOnDestroy(): void {
    this.loginStatus.search.emit(false);
  }
          registrationForm = this.fb.group({
            email:["",[Validators.required,this.checkIfGuestEmailsAreValid]],
            password:["",[Validators.required,Validators.pattern(/^(?=.*\d)(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z]).{7,}$/)]],
            confirmPassword:["",[Validators.required,Validators.pattern(/^(?=.*\d)(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z]).{7,}$/)]],
            name:["",[Validators.required,Validators.minLength(3)]],
            phoneNo:["",[Validators.required,Validators.pattern(/^[789]\d{9,9}$/)]]
            // userImg:[""]
          },{validators:[this.passMustMatchValidator]})

  get email(){
    return this.registrationForm.get('email');
  }

  get password(){
    return this.registrationForm.get('password');
  }

  get confirmPassword(){
    return this.registrationForm.get('confirmPassword');
  }

  get name(){
    return this.registrationForm.get('name');
  }

  get phone(){
    return this.registrationForm.get('phoneNo');
  }

  checkIfGuestEmailsAreValid(c: AbstractControl) {
 
    if (c.value !== '') {
      const emailString = c.value;
      const emails = emailString.split(',').map((e: string) => e.trim());
      const emailRegex = /^(([^<>()[\]\.,;:\s@\"]+(\.[^<>()[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;
      const anyInvalidEmail = emails.every((e: string) => e.match(emailRegex) !== null);
      if (!anyInvalidEmail) {
        return { checkIfGuestEmailsAreValid: false }
      }
    }
    return null;
  }

  passMustMatchValidator(match:AbstractControl){
    const passwordValue = match.get('password')?.value
    const confirmPass = match.get('confirmPassword')?.value
    if(!passwordValue || !confirmPass){
      return null;
    }
    if(passwordValue != confirmPass){
      return { mustMatch: false }
    }
    return null;
  }

  public onImageUpload(event:any) {
   const userImg = event.target.files[0];
    this.uploadedImage = userImg;
    
  }

  addUser(){
    const userData = this.registrationForm.value;
    this.isDirty=false;
    const fData = new FormData();
    // form data always supports string file
    fData.append('userData', JSON.stringify(userData));
    fData.append('file',this.uploadedImage);
    
    this.userService.registerUser(fData).subscribe({
      next:data=>{
        console.log(data);
        this._sanckBar.open('Account created successfully.....', 'success', {
          duration: 3000,
          panelClass: ['mat-toolbar', 'blue']
        });
        this.loginStatus.loginSuccess();
        this.router.navigateByUrl("/login")
      },error(err) {
        alert("not submitted")
      },
    })
  }

}
