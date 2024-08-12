package org.example.nazar.repository;

import org.example.nazar.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);

    boolean existsByNameAndTypeName(String name, String type);

    Product findByNameAndTypeName(String name, String type);
}
