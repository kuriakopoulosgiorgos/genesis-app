import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Product } from 'src/app/api/models/product';

@Component({
  selector: 'app-products-grid-ui',
  templateUrl: './products-grid.component.html',
  styleUrls: ['./products-grid.component.css']
})
export class ProductsGridComponent implements OnInit {

  @Input()
  products: Product[];

  @Input()
  page: number;

  @Input()
  pageSize: number;

  @Input()
  totalCount: number;

  @Output()
  onPageChange: EventEmitter<number> = new EventEmitter();

  @Output()
  onPageSizeChange: EventEmitter<number> = new EventEmitter();

  constructor() { }

  ngOnInit(): void {
  }

  pageChanged(): void {
    console.log('Page Changed ' + this.page);
    this.onPageChange.emit(this.page);
  }

  pageSizeChanged(): void {
    console.log('Page Size Changed ' + this.pageSize);
    this.onPageSizeChange.emit(this.pageSize);
  }
}
