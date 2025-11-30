package org.xyz.productsvc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "product")
public class Product {

    @Id
    @SequenceGenerator(
            name = "product_seq",
            sequenceName = "product_sequence",
            allocationSize = 1

    )
    @GeneratedValue(
            generator = "product_seq",
            strategy = GenerationType.SEQUENCE
    )
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private List<String> images;
    private int stock;



}
