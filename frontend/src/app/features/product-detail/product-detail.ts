import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../../core/product-service';
import { Product } from '../../model/product';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { HotToastService } from '@ngxpert/hot-toast';

@Component({
  selector: 'app-product-detail',
  imports: [CurrencyPipe, CommonModule],
  templateUrl: './product-detail.html',
  styleUrl: './product-detail.css',
})
export class ProductDetail implements OnInit {

  toast = inject(HotToastService)
  productService = inject(ProductService)
  activatedRoute = inject(ActivatedRoute)
  router = inject(Router)

  product = signal<Product | null>(null);

  imageSrcSingleBox: string | undefined
  imageSrcWholeSet: string | undefined
  isSeries: boolean = false;

  ngOnInit(): void {
    const id = this.activatedRoute.snapshot.params['id'];
    this.customToast()
    this.getProductById(id);
  }

  getProductById(id: number) {
    this.productService.getProductById(id).subscribe({
      next: (data) => {

        this.product.set(data);
        this.isSeries = this.product()?.productUnits.length! > 0
        this.imageSrcSingleBox = this.product()?.images[0]
        this.imageSrcWholeSet = this.product()?.images[4]

      },
      error: (err) => {

      }
    });
  }
  selectedVariant: string = 'SINGLE_BOX';

  customToast() {
    this.toast.success(
      `
    <div style="display: flex; align-items: center; justify-content: space-between; gap: 16px;">
      <span>ADD TO CART SUCCESSFULLY</span>
       <a href="#" class="text-sm font-medium leading-none text-[#d2001e] underline hover:no-underline dark:text-white">
            VIEW CART
        </a>
    </div>
    `,
      {
        duration: 5000, // Longer duration so users can click
        style: {
          padding: '16px',
          color: '#FFFFFF',
          background: '#000000',
          fontFamily: 'Cabin, sans-serif',
        },
        position: 'top-right',
        icon: ''
      }
    );
  }

  selectVariant(variantId: string): void {
    this.selectedVariant = variantId;
  }

  isSelected(variantId: string): boolean {
    return this.selectedVariant === variantId;
  }

  addToCart() {
    
  }


}
