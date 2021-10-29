import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { Product } from 'src/app/api/models/product';

@Component({
  selector: 'app-product-details-ui',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ProductDetailsComponent implements OnInit {

  @Input()
  product: Product;

  @Input()
  previewMode = false;

  constructor() { }

  ngOnInit(): void {
  }

}
