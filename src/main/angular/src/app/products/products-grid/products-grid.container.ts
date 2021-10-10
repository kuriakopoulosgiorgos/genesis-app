import { Component, OnInit } from '@angular/core';;
import { ProductService } from 'src/app/api/services/product.service';
import { map, switchMap, catchError } from 'rxjs/operators';
import { Subject, BehaviorSubject, Observable } from 'rxjs';
import { Product } from 'src/app/api/models/product';

@Component({
  selector: 'app-products-grid',
  templateUrl: './products-grid.container.html',
})
export class ProductsGridContainerComponent implements OnInit {

  page: number = 1;
  pageSize: number = 6;
  totalCount: number;
  loading = true;

  private onLoadProducts: Subject<void> = new BehaviorSubject(null);
  products$: Observable<Product[]> = this.onLoadProducts
    .pipe(
        switchMap(_v => this.findAllProducts())
    );

  constructor(private productService: ProductService) { }

  ngOnInit(): void {
    this.onLoadProducts.next();
  }

  changePage(page: number): void {
    this.page = page;
    this.onLoadProducts.next();
  }

  changePageSize(pageSize: number): void {
    this.pageSize = pageSize;
    this.onLoadProducts.next();
  }

  private findAllProducts(): Observable<Product[]> {
    this.loading = true;
    return this.productService.findAll({
      page: this.page,
      pageSize: this.pageSize
    }).pipe(
      catchError((_error) => {
        this.loading = false;
        return [];
      }),
      map((pageableProducts) => {
        this.loading = false;
        this.totalCount = pageableProducts.totalCount;
        return pageableProducts.data;
      })
    );
  }
}
