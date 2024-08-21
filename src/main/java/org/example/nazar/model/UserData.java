package org.example.nazar.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity

public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    @Getter
    @Setter
    @NotNull
    private String name;
    @Column
    @Getter
    @Setter
    private String family;
    @Column(unique = true)
    @Email
    @Getter
    @Setter
    @NotNull
    private String email;
    @Column
    @CreatedDate
    @Getter
    @Setter
    private LocalDateTime createdDate;
    @OneToOne(mappedBy = "userData")
    @JsonManagedReference
    private UserSecurityData userSecurityData;

}
