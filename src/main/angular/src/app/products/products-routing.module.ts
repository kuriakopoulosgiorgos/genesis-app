import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ProductsGridContainerComponent } from './products-grid/products-grid.container';
import { ProductDetailsContainerComponent } from './product-details/product-details.container';

const routes: Routes = [
  { path: '', component: ProductsGridContainerComponent },
  { path: 'details/:id', component: ProductDetailsContainerComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProductsRoutingModule { }
