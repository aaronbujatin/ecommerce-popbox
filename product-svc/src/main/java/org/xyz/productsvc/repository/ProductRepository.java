package org.xyz.productsvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xyz.productsvc.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
