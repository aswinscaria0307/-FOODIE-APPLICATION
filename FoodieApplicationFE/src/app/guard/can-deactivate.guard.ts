import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanDeactivate, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { RegistrationComponent } from '../registration/registration.component';

@Injectable({
  providedIn: 'root'
})
export class CanDeactivateGuard implements CanDeactivate<RegistrationComponent> {
  canDeactivate(componet:RegistrationComponent){
    if(componet.isDirty){
      return window.confirm("Are you sure want to go back");
    }
    return true;
  }
  
}
