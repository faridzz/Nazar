package org.example.nazar.repository;

import org.example.nazar.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository extends JpaRepository<Site, Long> {
    boolean existsByUrl(String url);

    Site findByUrl(String url);
}
