import { Component, inject, OnInit, signal } from '@angular/core';
import { CartService } from '../../core/service/cart-service';
import { Cart } from '../../model/cart';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-cart-summary',
  imports: [CommonModule],
  templateUrl: './cart-summary.html',
  styleUrl: './cart-summary.css',
})
export class CartSummary implements OnInit {

  cartService = inject(CartService)

  cart = signal<Cart | null>(null)

  ngOnInit(): void {
    this.getCartByUserId(5)
  }

  getCartByUserId(id: number) {
    this.cartService.getCartByUserId(id).subscribe({
      next: (data) => {
        this.cart.set(data);
        console.log(this.cart())
      },
      error: (error) => {
        console.log(error);
      }
    })
  }


  unitTypeMapper(unitType: string) {
    if (unitType == "SINGLE_BOX") {
        unitType = "Single Box"
    } else if(unitType == "WHOLE_SET") {
      unitType = "Whole Set"
    } else {
      ""
    }

    return unitType
  }


  cartItemIncrement(productId: number, productUnitId: number) {

    const body = {
      userId: 5,
      productId: productId,
      quantity: 1,
      productUnitId: productUnitId
    }

    this.cartService.incrementCartItem(body).subscribe({
      next: () => {
        this.ngOnInit()
      },
      error: (error) => {
        console.log(error);
      }
    })
  }

  cartItemDecrement(productId: number, productUnitId: number) {
    const body = {
      userId: 5,
      productId: productId,
      quantity: 1,
      productUnitId: productUnitId
    }

    this.cartService.decrementCartItem(body).subscribe({
      next: () => {
        this.ngOnInit()
      },
      error: (error) => {
        console.log(error);
      }
    })
  }

  

}
