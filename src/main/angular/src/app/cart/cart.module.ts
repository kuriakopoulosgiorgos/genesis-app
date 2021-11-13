import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CartRoutingModule } from './cart-routing.module';
import { MyCartContainerComponent } from './my-cart/my-cart.container';
import { MyCartComponent } from './my-cart/my-cart.component';
import { SharedModule } from '../shared/shared.module';


@NgModule({
  declarations: [
    MyCartContainerComponent,
    MyCartComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    CartRoutingModule,
  ]
})
export class CartModule { }
