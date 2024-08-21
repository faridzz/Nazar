package org.example.nazar.repository;

import org.example.nazar.model.Type;

import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the TypeRepository class.
 */
//TODO: this code hava some problems run it and you ll know
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class TypeRepositoryTest {
    @Autowired
    private TypeRepository typeRepository;


    @AfterEach
    void tearDown() {
        typeRepository.deleteAll();
    }
    @BeforeEach
    void beforeEach(){

    }


    /**
     * Test existsByName method to ensure it returns true
     * when a Type with the specified name exists.
     */
    @Test
    void testExistsByName_Return_True_If_Type_Exists() {
        // Arrange: Create and save a Type instance
        String typeName = "Electronics";
        Type type = new Type();
        type.setName(typeName);
        typeRepository.save(type);
        //in Type model name will save in lower case and trim
        typeName = typeName.trim().toLowerCase(Locale.ROOT);
        // Act: Check existence of Type with the specified name
        boolean result = typeRepository.existsByName(typeName);

        // Assert: Verify that the result is true
        assertThat(result)
                .as("Type with name '%s' should exist", typeName)
                .isTrue();
    }

    /**
     * Test existsByName method to ensure it returns false
     * when no Type with the specified name exists.
     */
    @Test
    void testExistsByName_Return_False_If_Type_Not_Exists() {
        String nonExistingTypeName = "NonExistingType";

        // Act: Check existence of Type with a non-existing name
        boolean result = typeRepository.existsByName(nonExistingTypeName);

        // Assert: Verify that the result is false
        assertThat(result)
                .as("Type with name '%s' should not exist", nonExistingTypeName)
                .isFalse();
    }

    /**
     * Test findByName method to ensure it returns the correct Type
     * when a Type with the specified name exists.
     */
    @Test
    void testFindByName_Return_Correct_Type_If_Exists() {
        // Arrange: Create and save a Type instance
        String typeName = "Electronics";
        Type type = new Type();
        type.setName(typeName);
        typeRepository.save(type);
        //in Type model name will save in lower case and trim
        typeName = typeName.trim().toLowerCase(Locale.ROOT);
        // Act: Retrieve the Type by name
        Type foundType = typeRepository.findByName(typeName);

        System.out.println(foundType.getName());
        // Assert: Verify that the retrieved Type is correct
        assertThat(foundType)
                .as("Type with name '%s' should be found", typeName)
                .isNotNull();
        assertThat(foundType.getName())
                .as("Type name should be '%s'", typeName)
                .isEqualTo(typeName);
    }

    /**
     * Test findByName method to ensure it returns null
     * when no Type with the specified name exists.
     */
    @Test
    void testFindByName_Return_Null_If_Type_Not_Exists() {
        String nonExistingTypeName = "NonExistingType";

        // Act: Retrieve the Type by a non-existing name
        Type foundType = typeRepository.findByName(nonExistingTypeName);

        // Assert: Verify that the result is null
        assertThat(foundType)
                .as("Type with name '%s' should not be found", nonExistingTypeName)
                .isNull();
    }
}
