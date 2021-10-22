import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { Product } from 'src/app/api/models/product';
import { ProductService } from 'src/app/api/services/product.service';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.container.html'
})
export class ProductDetailsContainerComponent implements OnInit {

  product$: Observable<Product> = this.route.params.pipe(
    switchMap(params => this.productService.findById({ id: params.id }))
  );

  constructor(private route: ActivatedRoute, private productService: ProductService) { }

  ngOnInit(): void {
  }
}
