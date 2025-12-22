package org.xyz.usersvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xyz.usersvc.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findById(Long id);

}
