package org.xyz.productsvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xyz.productsvc.dto.ProductRequest;
import org.xyz.productsvc.dto.ProductResponse;
import org.xyz.productsvc.entity.Product;
import org.xyz.productsvc.enums.ProductErrorInfo;
import org.xyz.productsvc.exception.ResourceNotFoundException;
import org.xyz.productsvc.mapper.ProductMapper;
import org.xyz.productsvc.repository.ProductRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public void createProduct(ProductRequest productRequest) {

        productRepository.save(productMapper.mapToProduct(productRequest));
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product =  productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ProductErrorInfo.PRODUCT_NOT_FOUND));

        return productMapper.mapToProductResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return List.of();
    }


}
