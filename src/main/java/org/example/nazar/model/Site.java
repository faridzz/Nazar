package org.example.nazar.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;


import java.util.List;
import java.util.Locale;

@Entity
@Getter
@Table(indexes = {@Index(name = "idx_url", columnList = "url")})
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    private String url;


    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProductReview> productReviews;

    // Getters and setters
    public void setName(String name) {
        this.name = name.toLowerCase(Locale.ROOT).trim();
    }

    public void setUrl(String url) {
        this.url = url.trim().toLowerCase();
    }
}
