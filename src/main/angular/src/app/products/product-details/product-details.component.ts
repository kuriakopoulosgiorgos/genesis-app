import { Component, EventEmitter, Input, OnInit, Output, ViewEncapsulation } from '@angular/core';
import { Product } from 'src/app/api/models/product';

@Component({
  selector: 'app-product-details-ui',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ProductDetailsComponent implements OnInit {

  @Input()
  product: Product;

  @Input()
  quantity = 0;

  @Input()
  previewMode = false;

  @Output()
  onAddToCart: EventEmitter<Product> = new EventEmitter();

  constructor() { }

  ngOnInit(): void {
  }

  addToCart(): void {
    this.onAddToCart.emit(this.product);
  }
}
