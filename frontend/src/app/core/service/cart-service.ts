import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Cart } from '../../model/cart';
import { environment } from '../../../environment/environemnt-development';
import { CartItemIncrement } from './../../model/cart-item-increment';
import { CartItemDecrement } from '../../model/cart-item-decrement';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  
   private httpClient = inject(HttpClient)

  getCartByUserId(userId: number) {
    return this.httpClient.get<Cart>(`${environment.CART_API}/api/v1/carts/items/user/${userId}`)
  } 

  incrementCartItem(body: CartItemIncrement) {
    return this.httpClient.post(`${environment.CART_API}/api/v1/carts/items/increment`, body)
  } 

  decrementCartItem(body: CartItemDecrement) {
    return this.httpClient.post(`${environment.CART_API}/api/v1/carts/items/decrement`, body)
  }

}
