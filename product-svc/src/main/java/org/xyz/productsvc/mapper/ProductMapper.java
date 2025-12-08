package org.xyz.productsvc.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xyz.productsvc.dto.ProductRequest;
import org.xyz.productsvc.dto.ProductResponse;
import org.xyz.productsvc.entity.Category;
import org.xyz.productsvc.entity.Product;
import org.xyz.productsvc.enums.ProductErrorInfo;
import org.xyz.productsvc.exception.ResourceNotFoundException;
import org.xyz.productsvc.repository.CategoryRepository;

@RequiredArgsConstructor
@Service
public class ProductMapper {

    private final CategoryRepository categoryRepository;

    public Product mapToProduct(ProductRequest productRequest){
        Product product = new Product();
        product.setName(productRequest.name());
        product.setDescription(productRequest.description());
        product.setPrice(productRequest.price());
        product.setImages(productRequest.images());
        product.setStock(productRequest.stock());

        Category category = categoryRepository.findById(productRequest.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException(ProductErrorInfo.PRODUCT_NOT_FOUND));
        product.setCategory(category);

        return product;
    }

    public ProductResponse mapToProductResponse(Product product){

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImages(),
                product.getStock(),
                product.getCategory().getName()
        );
    }

}
