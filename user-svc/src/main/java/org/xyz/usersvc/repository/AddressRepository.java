package org.xyz.usersvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xyz.usersvc.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
