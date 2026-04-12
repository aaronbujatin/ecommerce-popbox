package org.xyz.authsvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xyz.authsvc.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenId(String tokenId);

    void deleteByUserId(Long id);

}
