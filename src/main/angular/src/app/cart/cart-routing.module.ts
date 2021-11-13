import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MyCartContainerComponent } from './my-cart/my-cart.container';

const routes: Routes = [
  { path: '', component: MyCartContainerComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CartRoutingModule { }
