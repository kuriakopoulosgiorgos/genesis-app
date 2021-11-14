import { Component, OnInit } from '@angular/core';
import { Product } from 'src/app/api/models/product';
import { CartService } from 'src/app/cart.service';
import { Subject, Observable, of } from 'rxjs';
import { tap, switchMap, take, map } from 'rxjs/operators';
import { ProductService } from 'src/app/api/services/product.service';
import { multiScan } from 'rxjs-multi-scan';
import { Cart } from 'src/app/models';

@Component({
  selector: 'app-my-cart',
  templateUrl: './my-cart.container.html',
})
export class MyCartContainerComponent implements OnInit {

  private products: Observable<Product[]> = !this.cartService.isEmpty() ? this.cartService.cart$.pipe(
    take(1),
    switchMap(cart => this.productService.findAll({
       productCodes: Object.keys(cart).map((id) => +id),
     })
     .pipe(
       map((pageableProducts) => {
         this.loading = false;
         return pageableProducts.data
       })
     ))) : of([]);

  private onItemRemove: Subject<number> = new Subject();

  cart$ = this.cartService.cart$;

  products$: Observable<Product[]> = multiScan(
    this.products,
    (products, loadedProducts) => [...products, ...loadedProducts],
    this.onItemRemove,
    (products, productId) => products.filter(p => p.id !== productId),
    []
  );

  loading = !this.cartService.isEmpty();

  constructor(
    private cartService: CartService,
    private productService: ProductService
  ) {}

  ngOnInit(): void {}

  itemRemove(productId: number): void {
    this.onItemRemove.next(productId);
    this.cartService.removeAllItemsByProductCode(productId + '');
  }
}
