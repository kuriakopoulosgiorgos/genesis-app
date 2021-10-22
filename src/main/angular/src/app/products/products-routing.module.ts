import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ProductsGridContainerComponent } from './products-grid/products-grid.container';

const routes: Routes = [
  { path: '', component: ProductsGridContainerComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProductsRoutingModule { }
