package org.example.nazar.repository;

import org.example.nazar.enums.Role;
import org.example.nazar.model.UserSecurityData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserSecurityDataRepositoryTest {

    @Autowired
    private UserSecurityDataRepository userSecurityDataRepository;

    private UserSecurityData userSecurityData;

    @BeforeEach
    void setUp() {
        userSecurityData = new UserSecurityData();
        userSecurityData.setUsername("farid");
        userSecurityData.setPassword("1234");
        userSecurityData.setRole(Role.ADMIN);
        userSecurityDataRepository.save(userSecurityData);
    }

    @AfterEach
    void tearDown() {
        userSecurityDataRepository.deleteAll();
    }

    @Test
    void findUserSecurityDataByUsername_Return_UserSecurityData_If_Exists() {
        // Act
        Optional<UserSecurityData> securityDataByUsername = userSecurityDataRepository.findUserSecurityDataByUsername(userSecurityData.getUsername());

        // Assert
        assertThat(securityDataByUsername)
                .isPresent()
                .as("securityData with username '%s' should exist", userSecurityData.getUsername().toLowerCase().trim())
                .get().extracting(UserSecurityData::getUsername)
                .as("securityData with username should be '%s'", userSecurityData.getUsername().toLowerCase().trim())
                .isEqualTo(userSecurityData.getUsername());
    }

    @Test
    void findUserSecurityDataByUsername_Return_Empty_If_Not_Exists() {
        // Act
        Optional<UserSecurityData> securityDataByUsername = userSecurityDataRepository.findUserSecurityDataByUsername("nonexistentuser");

        // Assert
        assertThat(securityDataByUsername)
                .isNotPresent()
                .as("securityData with username 'nonexistentuser' should not exist");
    }

    @Test
    void existsByUsername_Return_True_If_Exists() {
        // Act
        boolean userExists = userSecurityDataRepository.existsByUsername(userSecurityData.getUsername());

        // Assert
        assertThat(userExists).as("user with username '%s' should exist and return true", userSecurityData.getUsername())
                .isTrue();
    }

    @Test
    void existsByUsername_Return_False_If_Not_Exists() {
        // Act
        boolean userExists = userSecurityDataRepository.existsByUsername("nonexistentuser");

        // Assert
        assertThat(userExists).as("user with username 'nonexistentuser' should not exist and return false")
                .isFalse();
    }
}
