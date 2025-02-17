import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Product } from 'src/app/api/models/product';

@Component({
  selector: 'app-product-card-ui',
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.css']
})
export class ProductCardComponent implements OnInit {

  @Input()
  product: Product;

  @Input()
  quantity = 0;

  @Input()
  rootUrl: string;

  @Output()
  onAddToCart: EventEmitter<void> = new EventEmitter();

  readonly maxTitleSize = 19;
  readonly maxDescriptionSize = 61;

  constructor() { }

  ngOnInit(): void {
  }

  get thumbnail() {
    return this.rootUrl + "/api/attachments/" + this.product.photos[0].reference;
  }

  get titlePopover() {
    return this.product?.name.length > this.maxTitleSize ? this.product.name : '';
  }

  get descriptionPopover() {
    return this.product?.description.length > this.maxDescriptionSize ? this.product.description : '';
  }

  addToCart(): void {
    this.onAddToCart.emit();
  }
}
