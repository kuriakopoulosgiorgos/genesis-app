import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ProductsRoutingModule } from './products-routing.module';
import { ProductsGridContainerComponent } from './products-grid/products-grid.container';
import { ProductsGridComponent } from './products-grid/products-grid.component';
import { SharedModule } from '../shared/shared.module';
import { NgbCarouselModule, NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ProductCardContainer } from './product-card/product-card.container';
import { ProductCardComponent } from './product-card/product-card.component';
import { ProductDetailsContainerComponent } from './product-details/product-details.container';
import { ProductDetailsComponent } from './product-details/product-details.component';
import { RenderModule } from '../render/render.module';
import { NewProductContainerComponent } from './new-product/new-product.container';
import { NewProductComponent } from './new-product/new-product.component';


@NgModule({
  declarations: [
     ProductsGridContainerComponent,
     ProductsGridComponent,
     ProductCardContainer,
     ProductCardComponent,
     ProductDetailsContainerComponent,
     ProductDetailsComponent,
     NewProductContainerComponent,
     NewProductComponent
    ],
  imports: [
    SharedModule,
    CommonModule,
    ProductsRoutingModule,
    FormsModule,
    NgbPaginationModule,
    NgbCarouselModule,
    RenderModule,
    ReactiveFormsModule,
  ]
})
export class ProductsModule { }
