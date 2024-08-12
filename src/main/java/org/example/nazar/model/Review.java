package org.example.nazar.model;


import jakarta.persistence.*;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.nazar.util.hashdata.EntityHashGenerator;

import java.time.LocalDate;

import java.time.ZoneId;

@Entity
@Getter
@Table(indexes = {@Index(name = "idx_HashId", columnList = "hashId")})
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String hashId;
    @Setter
    private String author;

    @Size(max = 10000)
    private String content;
    @Column(name = "Created_at")
    private LocalDate createdAt;
    @Setter
    @Column(name = "posted_at")
    private LocalDate postedAt;
    @Setter
    private int voteDown;
    @Setter
    private int voteUp;
    @Setter
    private int rating;

    @OneToOne(mappedBy = "review", cascade = CascadeType.ALL)
    private ProductReview productReview;


    public Review() {
        createdAt = LocalDate.now(ZoneId.of("Asia/Tehran"));

    }

    public void createHashId() {
        hashId = EntityHashGenerator.reviewHash(author, content, postedAt);
    }


    public void setContent(@Size(max = 10000) String content) {
        this.content = content;
    }


}
