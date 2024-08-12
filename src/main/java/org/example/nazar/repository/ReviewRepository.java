package org.example.nazar.repository;


import org.example.nazar.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByHashId(String hashId);
}