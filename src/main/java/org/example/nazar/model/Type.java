package org.example.nazar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;


import java.util.List;
import java.util.Locale;

@Entity
@Getter
@Table(indexes = {@Index(name = "idx_typeName" , columnList = "name")})
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> product;

    public void setName(String name) {
        this.name = name.toLowerCase(Locale.ROOT).trim();
    }
}
