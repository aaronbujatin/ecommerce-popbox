package org.xyz.usersvc.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "CUSTOMER_ADDRESS")
public class Address {

    @Id
    @SequenceGenerator(
            name = "customer_add_seq",
            sequenceName = "customer_add_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_add_seq"
    )
    private Long id;
    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customerId;
    private String street;
    private String city;
    private String municipal;
    private String postalCode;
    private String country;


}