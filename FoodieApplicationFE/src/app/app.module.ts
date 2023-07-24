import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CartComponent } from './cart/cart.component';
import{HttpClientModule} from "@angular/common/http";
import { MyOrdersComponent } from './my-orders/my-orders.component';
import { LoginComponent } from "./login/login.component";
import { MatDividerModule } from '@angular/material/divider';
import {MatTabsModule} from '@angular/material/tabs';
import { FormsModule,ReactiveFormsModule } from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatIconModule} from '@angular/material/icon';
import {MatCardModule} from '@angular/material/card';
import { RegistrationComponent } from './registration/registration.component';
import { HeaderComponent } from './header/header.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatListModule} from '@angular/material/list';
import {MatToolbarModule} from '@angular/material/toolbar';
import { RestaurantViewComponent } from './restaurant-view/restaurant-view.component';
import { ViewItemsComponent } from './view-items/view-items.component';
import { FooterComponent } from './footer/footer.component';
import { FavouritesComponent } from './favourites/favourites.component';
import { AdminComponent } from './admin/admin.component';
import { AdminFoodItemsViewComponent } from './admin-food-items-view/admin-food-items-view.component';
import { AdminRestaurantViewComponent } from './admin-restaurant-view/admin-restaurant-view.component';
import { AddItemsComponent } from "./add-items/add-items.component";
import { AddRestaurantComponent } from "./add-restaurant/add-restaurant.component";
import { FooditemComponent } from "./fooditem/fooditem.component";
import { ProfileComponent } from "./profile/profile.component";
import { UpdateUserComponent } from "./update-user/update-user.component";
import { AddressDialogComponent } from './address-dialog/address-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { AboutUsComponent } from "./about-us/about-us.component";
import { HomeComponent } from "./home/home.component";
import { ContactComponent } from './contact/contact.component';
import { MatBadgeModule } from "@angular/material/badge";

@NgModule({
  declarations: [
    AppComponent,
    CartComponent,
    MyOrdersComponent,
    LoginComponent,
    RegistrationComponent,
    HeaderComponent,
    PageNotFoundComponent,
    RestaurantViewComponent,
    ViewItemsComponent,
    FooterComponent,
    FavouritesComponent,
    AdminComponent,
    AdminFoodItemsViewComponent,
    AdminRestaurantViewComponent,
    AddItemsComponent,
    AddRestaurantComponent,
    FooditemComponent,
    ProfileComponent,
    UpdateUserComponent,
    AddressDialogComponent,
    AboutUsComponent,
    HomeComponent,
    ContactComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MatDividerModule,
    MatTabsModule,
    ReactiveFormsModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSnackBarModule,
    MatIconModule,
    MatCardModule,
    MatSidenavModule,
    MatListModule,
    MatToolbarModule,
    MatDialogModule,
    MatBadgeModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
