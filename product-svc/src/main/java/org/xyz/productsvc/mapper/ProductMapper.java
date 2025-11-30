package org.xyz.productsvc.mapper;

import org.springframework.stereotype.Service;
import org.xyz.productsvc.dto.ProductRequest;
import org.xyz.productsvc.dto.ProductResponse;
import org.xyz.productsvc.entity.Product;

@Service
public class ProductMapper {

    public Product mapToProduct(ProductRequest productRequest){
        return new Product(
            null,
            productRequest.name(),
            productRequest.description(),
            productRequest.price(),
            productRequest.images(),
            productRequest.stock()
        );
    }

    public ProductResponse mapToProductResponse(Product product){
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImages(),
                product.getStock()
        );
    }

}
