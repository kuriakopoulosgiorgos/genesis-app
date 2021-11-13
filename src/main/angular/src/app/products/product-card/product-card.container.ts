import { Component, Input } from '@angular/core';
import { Product } from '../../api/models/product';
import { CartService } from '../../cart.service';

@Component({
  selector: 'app-product-card',
  templateUrl: './product-card.container.html'
})
export class ProductCardContainer {

  @Input()
  product: Product;

  quantity = 0;

  constructor(private cartService: CartService) { }

  ngOnInit(): void {
    this.quantity = this.cartService.findQuantityByProductCode(this.product.id);
  }

  addToCart(): void {
    this.cartService.addItem('' + this.product.id, 1);
    this.quantity++;
  }
}
