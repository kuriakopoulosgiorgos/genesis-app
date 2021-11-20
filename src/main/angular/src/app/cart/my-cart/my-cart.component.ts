import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Product } from 'src/app/api/models/product';
import { Cart } from 'src/app/models';

@Component({
  selector: 'app-my-cart-ui',
  templateUrl: './my-cart.component.html',
  styleUrls: ['./my-cart.component.css']
})
export class MyCartComponent implements OnInit {

  @Input()
  cart: Cart;

  @Input()
  products: Product[] = [];

  @Output()
  onItemRemove: EventEmitter<number> = new EventEmitter();

  @Output()
  onCheckout: EventEmitter<void> = new EventEmitter();

  constructor() { }

  ngOnInit(): void {
  }

  get total(): number {
    return this.products.map(product => product.price * (this.cart[product.id] ? this.cart[product.id] : 0)).reduce((a, b) => a + b, 0);
  }

  itemRemove(productId: number): void {
    this.onItemRemove.emit(productId);
  }

  checkout(): void {
    this.onCheckout.emit();
  }

}
