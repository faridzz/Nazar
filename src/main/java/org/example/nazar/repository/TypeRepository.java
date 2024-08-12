package org.example.nazar.repository;


import org.example.nazar.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {

    boolean existsByName(String type);

    Type findByName(String type);

}
