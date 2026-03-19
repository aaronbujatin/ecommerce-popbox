import { Component, HostListener, inject, OnInit, signal } from '@angular/core';
import { Collapse } from 'flowbite';
import { CartService } from '../../../core/service/cart-service';
import { Cart } from '../../../model/cart';
@Component({
  selector: 'app-navbar',
  imports: [],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar implements OnInit{

  private cartService = inject(CartService)

   cart = signal<Cart | null>(null);

  ngOnInit(): void {
    this.getCartByUserId(5);
      console.log(this.cart()!);
  }


  getCartByUserId(userId: number) {
    // this.loadingSignal.set(true);
    this.cartService.getCartByUserId(userId).subscribe({
      next: (data) => {
        this.cart.set(data);
        console.log(this.cart()!)
        // this.loadingSignal.set(false);
      },
      error: (err) => {
        // this.errorSignal.set(err.message);
        // this.loadingSignal.set(false);
      }
    });
  }

  // set the target element that will be collapsed or expanded (eg. navbar menu)
  $targetEl = document.getElementById('targetEl');

  // optionally set a trigger element (eg. a button, hamburger icon)
  $triggerEl = document.getElementById('triggerEl');

  // optional options with default values and callback functions
  options = {
    onCollapse: () => {
      console.log('element has been collapsed');
    },
    onExpand: () => {
      console.log('element has been expanded');
    },
    onToggle: () => {
      console.log('element has been toggled');
    },
  };

  instanceOptions = {
    id: 'targetEl',
    override: true
  };

  isMegaMenuOpen = false;

  toggleMegaMenu() {
    this.isMegaMenuOpen = !this.isMegaMenuOpen;
  }

  closeMegaMenu() {
    this.isMegaMenuOpen = false;
  }


}
