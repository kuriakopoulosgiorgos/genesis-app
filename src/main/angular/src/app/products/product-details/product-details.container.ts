import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { switchMap, tap, map } from 'rxjs/operators';
import { Product } from 'src/app/api/models/product';
import { ProductService } from 'src/app/api/services/product.service';
import { CartService } from 'src/app/cart.service';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.container.html'
})
export class ProductDetailsContainerComponent implements OnInit {

  loading = true;

  product$: Observable<Product> = this.route.params.pipe(
    switchMap(params => this.productService.findById({ id: params.id }).pipe(
      tap(_product => this.loading = false)
    ))
  );

  quantity$: Observable<number> = this.route.params.pipe(
    switchMap(params => this.cartService.cart$.pipe(
      map(cart => cart[params.id])
    ))
  );

  constructor(private route: ActivatedRoute, private productService: ProductService, private cartService: CartService) { }

  ngOnInit(): void {
  }

  addToCart(product: Product) {
    this.cartService.addItem(product.id + '', 1);
  }
}
