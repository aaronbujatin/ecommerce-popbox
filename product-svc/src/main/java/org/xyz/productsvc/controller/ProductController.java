package org.xyz.productsvc.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xyz.productsvc.dto.ProductRequest;
import org.xyz.productsvc.dto.ProductResponse;
import org.xyz.productsvc.dto.ProductBatchReq;
import org.xyz.productsvc.dto.ProductUnitResponse;
import org.xyz.productsvc.entity.Product;
import org.xyz.productsvc.repository.ProductRepository;
import org.xyz.productsvc.service.ProductService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        productService.createProduct(productRequest);
        return ResponseEntity.ok("product successfully saved");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") Long id) {
        ProductResponse productResponse = productService.getProductById(id);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping()
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> productResponses = productService.getAllProducts();
        return ResponseEntity.ok(productRepository.findAll()
                .stream()
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getImages(),
                        product.getCategory().getName(),
                        product.getProductUnits()
                                .stream()
                                .map(productUnit -> new ProductUnitResponse(
                                        productUnit.getId(),
                                        productUnit.getProductUnitType(),
                                        productUnit.getPrice(),
                                        productUnit.getStock(),
                                        productUnit.getImageUrl()
                                ))
                                .toList()
                )).toList());
    }

    @PostMapping("/batch")
    public ResponseEntity<List<ProductResponse>> getAllProductByBatchIds(@RequestBody List<ProductBatchReq> productBatchReqs) {
        log.info("Receiving request: {}", productBatchReqs);
        Map<Long, List<Long>> batchReqs = productBatchReqs.stream()
                .collect(Collectors.toMap(
                        ProductBatchReq::productId,
                        ProductBatchReq::productUnitIds
                    )
                );

        batchReqs.forEach((key, value) -> {
            System.out.println("Key : " + key  + "Value: " + value);
        });

        var products = productRepository.findAllById(batchReqs.keySet());
        var productResponse = products.stream()
                .map(product -> {
                    var productUnitIds = batchReqs.getOrDefault(product.getId(), Collections.emptyList());
                           return new ProductResponse(
                                    product.getId(),
                                    product.getName(),
                                    product.getDescription(),
                                    product.getImages(),
                                    product.getCategory().getName(),
                                    product.getProductUnits()
                                            .stream()
                                            .filter(unit -> productUnitIds.contains(unit.getId()))
                                            .map(unit -> new ProductUnitResponse(
                                                    unit.getId(),
                                                    unit.getProductUnitType(),
                                                    unit.getPrice(),
                                                    unit.getStock(),
                                                    unit.getImageUrl()
                                                )
                                            ).toList()
                           );
                        }
                    )
                .toList();




//        var product = productRepository.findAllById(productIds)
//        .stream()
//        .map(p -> new ProductResponse(
//                    p.getId(),
//                    p.getName(),
//                    p.getDescription(),
//                    p.getImages(),
//                    p.getCategory().getName(),
//                    p.getProductUnits()
//                            .stream()
//                            .filter(productUnit -> {
//                                var productUnitIds = productBatchReqs
//                                        .stream()
//                                        .map(ProductBatchReq::productId)
//                                        .filter(id -> p.getId().equals(id))
//                                        .toList();
//
//                                return  productUnitIds.contains(productUnit.getId());
//                            })
//                            .map(productUnit -> new ProductUnitResponse(
//                                    productUnit.getId(),
//                                    productUnit.getProductUnitType(),
//                                    productUnit.getPrice(),
//                                    productUnit.getStock()
//                                )
//                            )
//                            .toList()
//                )
//        ).toList();
        return ResponseEntity.ok(productResponse);


    }

    @GetMapping("/batch2")
    public ResponseEntity<String> getAllProductByBatchIds(HttpServletResponse httpServletResponse) {

        var cookie = new Cookie("authToken", "myToken");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setAttribute("Strict", "Strict value");
        httpServletResponse.addCookie(cookie);

        return ResponseEntity.ok("Success");
    }


}
