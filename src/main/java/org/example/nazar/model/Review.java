package org.example.nazar.model;


import jakarta.persistence.*;

import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.nazar.util.hashdata.EntityHashGenerator;

import java.time.LocalDate;

import java.time.ZoneId;
import java.util.Objects;

@Entity
@Getter
@Builder
@AllArgsConstructor
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
    @Setter
    private int maximumRate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(author, review.author) && Objects.equals(content, review.content) && Objects.equals(postedAt, review.postedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, content, postedAt);
    }
}
