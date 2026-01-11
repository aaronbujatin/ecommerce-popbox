package org.xyz.productsvc.service;


import org.xyz.productsvc.dto.ProductRequest;
import org.xyz.productsvc.dto.ProductResponse;
import org.xyz.productsvc.dto.ProductBatchReq;

import java.util.List;

public interface ProductService {

    void createProduct(ProductRequest productRequest);
    ProductResponse getProductById(Long id);
    List<ProductResponse> getAllProducts();
    List<ProductResponse> getAllProductsByFilter(List<ProductBatchReq> productBatchReqs);

}
