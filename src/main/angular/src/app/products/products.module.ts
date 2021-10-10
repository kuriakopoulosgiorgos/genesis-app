import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ProductsRoutingModule } from './products-routing.module';
import { ProductsGridContainerComponent } from './products-grid/products-grid.container';
import { ProductsGridComponent } from './products-grid/products-grid.component';
import { SharedModule } from '../shared/shared.module';
import { NgbPagination, NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { ProductCardComponent } from './product-card/product-card.component';


@NgModule({
  declarations: [
     ProductsGridContainerComponent,
     ProductsGridComponent,
     ProductCardComponent
    ],
  imports: [
    SharedModule,
    CommonModule,
    ProductsRoutingModule,
    FormsModule,
    NgbPaginationModule
  ]
})
export class ProductsModule { }
