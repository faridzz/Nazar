package org.example.nazar.repository;

import org.example.nazar.LoggingTestExecutionListener;
import org.example.nazar.model.Product;
import org.example.nazar.model.Type;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(LoggingTestExecutionListener.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TypeRepository typeRepository;

    @Test
    void testSaveTypeAndProduct() {
        // Arrange
        Type type = new Type();
        type.setName("Electronics");
        type = typeRepository.save(type);

        Product product = new Product();
        product.setName("Smartphone");
        product.setType(type);
        product = productRepository.save(product);

        // Act & Assert - Type
        Optional<Type> foundType = typeRepository.findById(type.getId());
        assertThat(foundType)
                .as("Type with ID '%d' should exist", type.getId())
                .isPresent()
                .get()
                .extracting(Type::getName)
                .as("Type name should be '%s'", type.getName())
                .isEqualTo(type.getName());

        // Act & Assert - Product
        Optional<Product> foundProduct = productRepository.findById(product.getId());
        Product finalProduct = product;
        Type finalType = type;
        assertThat(foundProduct)
                .as("Product with ID '%d' should exist", product.getId())
                .isPresent()
                .get()
                .satisfies(p -> {
                    assertThat(p.getName())
                            .as("Product name should be '%s'", finalProduct.getName())
                            .isEqualTo(finalProduct.getName());
                    assertThat(p.getType())
                            .as("Product type should match the saved type with ID '%d'", finalType.getId())
                            .isEqualTo(finalType);
                });
    }

    @Test
    void testFindByNameAndTypeName() {
        // Arrange
        Type type = new Type();
        type.setName("Electronics");
        type = typeRepository.save(type);

        Product product = new Product();
        product.setName("Smartphone");
        product.setType(type);
        product = productRepository.save(product);

        // Act & Assert - findByName
        Product foundProduct = productRepository.findByName(product.getName());
        assertThat(foundProduct)
                .as("Product with name '%s' should exist", product.getName())
                .extracting(Product::getName)
                .as("Product name should be '%s'", product.getName())
                .isEqualTo(product.getName());

        // Act & Assert - findByNameAndTypeName
        Product foundProductByNameAndType = productRepository.findByNameAndTypeName(product.getName(), type.getName());
        assertThat(foundProductByNameAndType)
                .as("Product with name '%s' and type '%s' should exist", product.getName(), type.getName())
                .isNotNull()
                .extracting(Product::getName)
                .as("Product name should be '%s'", product.getName())
                .isEqualTo(product.getName());

        assertThat(foundProductByNameAndType.getType().getName())
                .as("Product type name should be '%s'", type.getName())
                .isEqualTo(type.getName());
    }

    @Test
    void testExistsByNameAndTypeName() {
        // Arrange
        Type type = new Type();
        type.setName("Electronics");
        type = typeRepository.save(type);

        Product product = new Product();
        product.setName("Smartphone");
        product.setType(type);
        product = productRepository.save(product);

        // Act
        boolean exist = productRepository.existsByNameAndTypeName(product.getName(), type.getName());

        // Assert
        assertThat(exist)
                .as("Product with name '%s' and type '%s' should exist", product.getName(), type.getName())
                .isTrue();
    }
}
