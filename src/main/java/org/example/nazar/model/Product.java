package org.example.nazar.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Locale;

@Entity
@Getter

@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Setter
    @ManyToOne
    @JoinColumn(name = "type_id")
    private Type type;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductReview> productReviews;


    // Getters and setters
    public void setName(String name) {
        this.name = name.toLowerCase(Locale.ROOT).trim();
    }

}
