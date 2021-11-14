import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Subject, BehaviorSubject, Observable } from 'rxjs';
import { ErrorService } from './error.service';
import { Cart } from './models';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private static readonly CART = '_cart';
  private static readonly EMPTY_CART: Cart = {};

  private itemsInCart: Subject<number>;
  private _cart: Cart;
  private currentItemsCount = 0;
  private cart:Subject<Cart>;

  itemsInCart$: Observable<number>;
  cart$: Observable<Cart>;


  constructor(private translateService: TranslateService, private errorService: ErrorService) {
    this._cart = this.getCartFromLocalStorage();
    this.currentItemsCount = Object.keys(this._cart).map(productId => this._cart[productId]).reduce((a, b) => a + b, 0);

    this.itemsInCart = new BehaviorSubject(this.currentItemsCount);
    this.itemsInCart$ = this.itemsInCart.asObservable();
    this.cart = new BehaviorSubject(this._cart);
    this.cart$ = this.cart.asObservable();
  }

  addItem(productCode: string, quantity: number): void {
    if(Object.keys(this._cart).length >= 25) {
      this.errorService.error(this.translateService.instant('_cart-is-full'));
    }
    if(this._cart[productCode]) {
      this._cart[productCode] += quantity;
    } else {
      this._cart[productCode] = quantity;
    }
    this.currentItemsCount += quantity;
    localStorage.setItem(CartService.CART, JSON.stringify(this._cart));
    this.itemsInCart.next(this.currentItemsCount);
    this.cart.next(this._cart);
  }

  removeAllItemsByProductCode(productCode: string): void {
    let quantity =  this._cart[productCode] ? this._cart[productCode] : 0;
    this.currentItemsCount -= quantity;
    delete this._cart[productCode];
    localStorage.setItem(CartService.CART, JSON.stringify(this._cart));
    this.itemsInCart.next(this.currentItemsCount);
    this.cart.next(this._cart);
  }

  findQuantityByProductCode(productCode: number): number {
      return this._cart[productCode] ? this._cart[productCode] : 0;
  }

  isEmpty(): boolean {
    return Object.keys(this._cart).length == 0;
  }

  private getCartFromLocalStorage(): Cart {
    let _cart = JSON.parse(localStorage.getItem(CartService.CART)) as Cart;

    if(!_cart) {
      _cart = CartService.EMPTY_CART;
    }

    return _cart;
  }
}
