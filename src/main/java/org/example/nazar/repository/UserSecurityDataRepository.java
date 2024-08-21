package org.example.nazar.repository;

import org.example.nazar.model.UserSecurityData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSecurityDataRepository extends JpaRepository<UserSecurityData, Long> {
    Optional<UserSecurityData> findUserSecurityDataByUsername(String username);

    boolean existsByUsername(String username);
}
