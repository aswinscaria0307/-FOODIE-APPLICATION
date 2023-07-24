import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MyOrdersComponent } from './my-orders/my-orders.component';
import { CartComponent } from './cart/cart.component';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { HeaderComponent } from './header/header.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { RestaurantViewComponent } from './restaurant-view/restaurant-view.component';
import { ViewItemsComponent } from './view-items/view-items.component';
import { FavouritesComponent } from './favourites/favourites.component';
import { AdminComponent } from './admin/admin.component';
import { AdminFoodItemsViewComponent } from './admin-food-items-view/admin-food-items-view.component';
import { AdminRestaurantViewComponent } from './admin-restaurant-view/admin-restaurant-view.component';
import { AddItemsComponent } from './add-items/add-items.component';
import { AddRestaurantComponent } from './add-restaurant/add-restaurant.component';
import { FooditemComponent } from './fooditem/fooditem.component';
import { ProfileComponent } from './profile/profile.component';
import { UpdateUserComponent } from './update-user/update-user.component';
import { AuthGuard } from './guard/auth.guard';
import { AddressDialogComponent } from './address-dialog/address-dialog.component';
import { AboutUsComponent } from './about-us/about-us.component';
import { HomeComponent } from './home/home.component';
import { ContactComponent } from './contact/contact.component';
import { CanDeactivateGuard } from './guard/can-deactivate.guard';

const routes: Routes = [{path:"myOrders",component:MyOrdersComponent,canActivate:[AuthGuard]},
{path:"cart",component:CartComponent,canActivate:[AuthGuard]},
{path:"login",component:LoginComponent},
{path:"profile",component:ProfileComponent,canActivate:[AuthGuard]},
{path:"update",component:UpdateUserComponent,canActivate:[AuthGuard]},
{path:"registration",component:RegistrationComponent,canDeactivate:[CanDeactivateGuard]},
{path:"header",component:HeaderComponent},
{path:"",component:RestaurantViewComponent},
{path:"viewItem",component:ViewItemsComponent},
{path:"favourite",component:FavouritesComponent,canActivate:[AuthGuard]},
{path:"admin",component:AdminComponent,canActivate:[AuthGuard]},
{path:"adminFoodItemView",component:AdminFoodItemsViewComponent,canActivate:[AuthGuard]},
{path:"adminRestaurantView",component:AdminRestaurantViewComponent,canActivate:[AuthGuard]},
{path:"adminAddItem",component:AddItemsComponent,canActivate:[AuthGuard]},
{path:"adminAddRestaurant",component:AddRestaurantComponent,canActivate:[AuthGuard]},
{path:"adminFoodItem",component:FooditemComponent,canActivate:[AuthGuard]},
{path:"addressDialog",component:AddressDialogComponent},
{path:"about",component:AboutUsComponent},
{path:"home",component:HomeComponent},
{path:"contact",component:ContactComponent},
{path:"**",component:PageNotFoundComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
