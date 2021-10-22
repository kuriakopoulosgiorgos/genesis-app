import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ProductsRoutingModule } from './products-routing.module';
import { ProductsGridContainerComponent } from './products-grid/products-grid.container';
import { ProductsGridComponent } from './products-grid/products-grid.component';
import { SharedModule } from '../shared/shared.module';
import { NgbCarouselModule, NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { ProductCardComponent } from './product-card/product-card.component';
import { ProductDetailsContainerComponent } from './product-details/product-details.container';
import { ProductDetailsComponent } from './product-details/product-details.component';
import { RenderModule } from '../render/render.module';


@NgModule({
  declarations: [
     ProductsGridContainerComponent,
     ProductsGridComponent,
     ProductCardComponent,
     ProductDetailsContainerComponent,
     ProductDetailsComponent
    ],
  imports: [
    SharedModule,
    CommonModule,
    ProductsRoutingModule,
    FormsModule,
    NgbPaginationModule,
    NgbCarouselModule,
    RenderModule
  ]
})
export class ProductsModule { }
