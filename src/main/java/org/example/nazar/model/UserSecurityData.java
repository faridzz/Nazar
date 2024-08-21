package org.example.nazar.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.nazar.enums.Role;


import java.time.LocalDateTime;

@Entity
public class UserSecurityData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Getter
    @NotNull(message = "user name cnt be empty")
    private String username;
    @Getter
    @Setter
    @NotNull(message = "password cnt be empty")
    private String password;
    @Getter
    @Enumerated(EnumType.STRING)
    @NotNull
    @Setter
    private Role role;
    @Column
    private LocalDateTime createdDate;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JsonBackReference
    private UserData userData;

    @PrePersist
    private void prePersist() {
        if (role == null) {
            role = Role.USER;
        }
        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }

    }


    public void setUsername(@NotNull String username) {
        this.username = username.toLowerCase().trim();
    }
}
