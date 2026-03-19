import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal, Signal } from '@angular/core';
import { environment } from '../../environment/environemnt-development';
import { Product } from '../model/product';

@Injectable({
  providedIn: 'root',
})
export class ProductService {

  private httpClient = inject(HttpClient)
  
  getProductById(id: number) {
    return this.httpClient.get<Product>(`${environment.PRODUCT_BASEURL}/api/v1/products/${id}`)
  } 
  


}
