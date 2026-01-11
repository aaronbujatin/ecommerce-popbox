package org.xyz.productsvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.xyz.productsvc.dto.ProductRequest;
import org.xyz.productsvc.dto.ProductResponse;
import org.xyz.productsvc.dto.ProductBatchReq;
import org.xyz.productsvc.dto.ProductUnitResponse;
import org.xyz.productsvc.entity.Product;
import org.xyz.productsvc.enums.ProductErrorInfo;
import org.xyz.productsvc.exception.ResourceNotFoundException;
import org.xyz.productsvc.mapper.ProductMapper;
import org.xyz.productsvc.repository.ProductRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public void createProduct(ProductRequest productRequest) {

        log.info("Creating product with name: {}", productRequest.name());
        productRepository.save(productMapper.mapToProduct(productRequest));
    }

    @Override
    public ProductResponse getProductById(Long id) {

        log.info("Getting the product with id of {}", id);
        Product product =  productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ProductErrorInfo.PRODUCT_NOT_FOUND));

        ProductResponse productResponse = mapToProductResponse(product);

        log.info("Returning the product with id of {} {}", id, productResponse);
        return productResponse;
    }

    @Override
    public List<ProductResponse> getAllProducts() {
//        log.info("Getting the list of product");
//
//        List<Product> products = productRepository.findAll();
//
//        List<ProductResponse> productResponses = products
//                .stream()
//                .map(productMapper::mapToProductResponse)
//                .toList();
//
//        log.info("Returning list of products {}", productResponses);
//        return productResponses;

        return null;
    }

    @Override
    public List<ProductResponse> getAllProductsByFilter(List<ProductBatchReq> productBatchReqs) {

//        var productIds = productBatchReqs.stream().map(ProductBatchReq::id).toList();

//        var product = productRepository.findAllById(productIds)
//                .stream()
//                .map(p -> new ProductResponse(
//                            p.getId(),
//                            p.getName(),
//                            p.getDescription(),
//                            p.getImages(),
//                            p.getCategory(),
//                            p.getProductUnits()
//                                    .stream()
//                                    .filter()
//                        )
//                );


        return List.of();
    }

    private ProductResponse mapToProductResponse(Product product){

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getImages(),
                product.getCategory().getName(),
                product.getProductUnits()
                        .stream()
                        .map(unit -> new ProductUnitResponse(
                                        unit.getId(),
                                        unit.getProductUnitType(),
                                        unit.getPrice(),
                                        unit.getStock(),
                                        unit.getImageUrl()
                                )
                        )
                        .toList()
        );
    }


}
