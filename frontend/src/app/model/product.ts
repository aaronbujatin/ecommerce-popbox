import { ProductUnit } from "./product-unit";

export interface Product {

    id: number;
    name: string;
    description: string;
    price: number;
    images: string[];
    stock: string;
    categoryName: string;
    productUnits: ProductUnit[]

}
