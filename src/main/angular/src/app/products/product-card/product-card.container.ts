import { Component, Input } from '@angular/core';
import { Product } from '../../api/models/product';
import { CartService } from '../../cart.service';
import { ApiConfiguration } from 'src/app/api/api-configuration';

@Component({
  selector: 'app-product-card',
  templateUrl: './product-card.container.html'
})
export class ProductCardContainer {

  @Input()
  product: Product;

  quantity = 0;
  rootUrl: string = this.apiConfiguration.rootUrl;

  constructor(private apiConfiguration: ApiConfiguration, private cartService: CartService) { }

  ngOnInit(): void {
    this.quantity = this.cartService.findQuantityByProductCode(this.product.id);
  }

  addToCart(): void {
    this.cartService.addItem('' + this.product.id, 1);
    this.quantity++;
  }
}
