import { Product } from './shared/models/product-model';
import { environment } from './../environments/environment.prod';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private httpClient: HttpClient) { }


  getProducts(): Observable<Product[]> {
    return this.httpClient.get<Product[]>(environment.api.products);
  }
}
