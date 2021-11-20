import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PaymentCancelComponent } from './payment-cancel/payment-cancel.component';
import { PaymentSuccessComponent } from './payment-success/payment-success.component';

const routes: Routes = [
  { path: 'payment-success', component: PaymentSuccessComponent },
  { path: 'payment-cancel', component: PaymentCancelComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PaymentRoutingModule { }
