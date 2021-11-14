import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ProductsGridContainerComponent } from './products-grid/products-grid.container';
import { ProductDetailsContainerComponent } from './product-details/product-details.container';
import { NewProductContainerComponent } from './new-product/new-product.container';
import { SupplierGuard } from '../supplier.guard';

const routes: Routes = [
  { path: '', component: ProductsGridContainerComponent },
  { path: 'details/:id', component: ProductDetailsContainerComponent },
  { path: 'new-product', component: NewProductContainerComponent, canActivate: [SupplierGuard] },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProductsRoutingModule { }
