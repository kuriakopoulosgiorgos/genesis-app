import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PaymentRoutingModule } from './payment-routing.module';
import { PaymentSuccessComponent } from './payment-success/payment-success.component';
import { PaymentCancelComponent } from './payment-cancel/payment-cancel.component';
import { SharedModule } from '../shared/shared.module';


@NgModule({
  declarations: [PaymentSuccessComponent, PaymentCancelComponent],
  imports: [
    SharedModule,
    CommonModule,
    PaymentRoutingModule
  ]
})
export class PaymentModule { }
