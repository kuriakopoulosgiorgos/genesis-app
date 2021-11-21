import { Component, OnInit } from '@angular/core';
import { Product } from 'src/app/api/models/product';
import { CartService } from 'src/app/cart.service';
import { Subject, Observable, of } from 'rxjs';
import { switchMap, take, map } from 'rxjs/operators';
import { ProductService } from 'src/app/api/services/product.service';
import { multiScan } from 'rxjs-multi-scan';
import { CheckoutService } from 'src/app/api/services/checkout.service';

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
    private productService: ProductService,
    private checkoutService: CheckoutService
  ) {}

  ngOnInit(): void {}

  itemRemove(productId: number): void {
    this.onItemRemove.next(productId);
    this.cartService.removeAllItemsByProductCode(productId + '');
  }

  checkout(): void {
    this.loading = true;
    this.cartService.cart$.pipe(
      take(1),
      switchMap(cart => this.checkoutService.checkout$Response({
        paymentSuccessFragment: '#payment/payment-success',
        paymentCancelFragment: '#payment/payment-cancel',
        body: {...cart } }
        ))
    ).subscribe((result) => window.location.href = result.headers.get('location'));
  }
}
