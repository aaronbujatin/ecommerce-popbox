import { Routes } from '@angular/router';
import { ProductDetail } from './features/product-detail/product-detail';
import { CartSummary } from './features/cart-summary/cart-summary';

export const routes: Routes = [
    {
        path: 'products/:id',
        component: ProductDetail
    },
    {
        path: 'cart',
        component: CartSummary
    }
];
