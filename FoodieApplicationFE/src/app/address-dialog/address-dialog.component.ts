import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { UserService } from '../service/user.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-address-dialog',
  templateUrl: './address-dialog.component.html',
  styleUrls: ['./address-dialog.component.css']
})
export class AddressDialogComponent {
  addressForm: FormGroup;

constructor(public dialogRef:MatDialogRef<AddressDialogComponent>,@Inject(MAT_DIALOG_DATA) public data:any,private formBuilder: FormBuilder,private user:UserService,private route:Router){
  this.addressForm = this.formBuilder.group({
    houseNo: ['', Validators.required],
    landmark:['', Validators.required],
    street:['', Validators.required],
    city:['', Validators.required],
    pin:['', Validators.required]
  });
}
saveAddress() {
  if (this.addressForm.valid) {
    const newAddress = this.addressForm.value;
    this.user.changeAddress(newAddress).subscribe(response=>{
      this.user.addressChanged.emit(true);
      this.dialogRef.close(newAddress);
    },(error)=>{
      console.log("something Went wrong");
    }
    );
  }
}

cancel() {
  this.dialogRef.close();
}
}
