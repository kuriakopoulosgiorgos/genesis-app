import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { Product } from 'src/app/api/models/product';
import { SortByDirection } from 'src/app/api/models/sort-by-direction';
import { ProductsGridPresenter } from './products-grid.presenter';

@Component({
  selector: 'app-products-grid-ui',
  templateUrl: './products-grid.component.html',
  styleUrls: ['./products-grid.component.css'],
  providers: [ProductsGridPresenter]
})
export class ProductsGridComponent implements OnInit, OnDestroy {

  @Input()
  products: Product[];

  @Input()
  page: number;

  @Input()
  pageSize: number;

  @Input()
  totalCount: number;

  @Input()
  sortByDirection: SortByDirection;

  @Output()
  onSearch: EventEmitter<string> = new EventEmitter();

  @Output()
  OnSortByDirection: EventEmitter<SortByDirection> = new EventEmitter();

  @Output()
  onPageChange: EventEmitter<number> = new EventEmitter();

  @Output()
  onPageSizeChange: EventEmitter<number> = new EventEmitter();

  private destroy: Subject<void>  = new Subject();

  constructor(private presenter: ProductsGridPresenter) { }

  ngOnInit(): void {
    this.presenter.searchTerm$.pipe(
      takeUntil(this.destroy)
    ).subscribe(searchTerm => this.onSearch.emit(searchTerm));
  }

  get arrow(): string {
    return this.sortByDirection === SortByDirection.Asc ? "up" : "down";
  }

  get searchInput(): string {
    return this.presenter.searchInput;
  }

  set searchInput(searchInput: string) {
    this.presenter.searchInput = searchInput;
  }

  search(): void {
    this.presenter.search();
  }

  resetSearch(): void {
    this.presenter.reset();
  }

  sortByDirectionChanged(): void {
    this.OnSortByDirection.emit(this.sortByDirection === SortByDirection.Asc ? SortByDirection.Desc : SortByDirection.Asc);
  }

  pageChanged(): void {
    this.onPageChange.emit(this.page);
  }

  pageSizeChanged(): void {
    this.onPageSizeChange.emit(this.pageSize);
  }

  ngOnDestroy(): void {
    this.destroy.next();
    this.destroy.complete();
  }
}
