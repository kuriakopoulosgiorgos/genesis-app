import { Component, OnInit } from '@angular/core';;
import { ProductService } from 'src/app/api/services/product.service';
import { map, switchMap, catchError } from 'rxjs/operators';
import { Subject, BehaviorSubject, Observable } from 'rxjs';
import { Product } from 'src/app/api/models/product';
import { SortByDirection } from 'src/app/api/models/sort-by-direction';
import { ProductSortByField } from 'src/app/api/models/product-sort-by-field';

@Component({
  selector: 'app-products-grid',
  templateUrl: './products-grid.container.html',
})
export class ProductsGridContainerComponent implements OnInit {

  searchTerm: string;
  sortByDirection: SortByDirection = SortByDirection.Asc;
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

  search(searchTerm: string): void {
    this.searchTerm = searchTerm;
    this.onLoadProducts.next();
  }

  changeSortByDirection(sortByDirection: SortByDirection): void {
    this.sortByDirection = sortByDirection;
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
      pageSize: this.pageSize,
      searchTerm: this.searchTerm,
      sortByDirection: this.sortByDirection,
      sortByField: ProductSortByField.Price
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
