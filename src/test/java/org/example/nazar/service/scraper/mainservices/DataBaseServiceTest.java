package org.example.nazar.service.scraper.mainservices;

import org.example.nazar.model.Product;
import org.example.nazar.model.Type;
import org.example.nazar.repository.ProductRepository;
import org.example.nazar.repository.TypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DataBaseServiceTest {

    @Mock
    private TypeRepository mockTypeRepository;
    @Mock
    private ProductRepository mockProductRepository;
    @InjectMocks
    private DataBaseService dataBaseService;

    private Product product;
    private Type type;

    @BeforeEach
    void setUp() {
        type = Type.builder()
                .name("smartphone").build();
        product = Product.builder()
                .name("mi9")
                .type(type).build();

    }

    @Test
    void testAddProduct_If_Exists_Return_Product() {
        //Arrange
//        final String productName = product.getName();
//        final String typeName = product.getType().getName();
//
//        when(mockTypeRepository.existsByName(typeName)).thenReturn(true);
//        when(mockProductRepository.existsByNameAndTypeName(productName, typeName)).thenReturn(false);
//        when(mockTypeRepository.findByName(typeName)).thenReturn(type);
//        when(mockProductRepository.save(product)).thenReturn(product);
//
//        // Act
//        Product productReturn = dataBaseService.addProduct(productName, typeName);
//
//        // Assert
//        assertThat(productReturn).isEqualTo(any(Product.class));
//        verify(mockTypeRepository).existsByName(typeName);
//        verify(mockProductRepository).existsByNameAndTypeName(productName, typeName);
//        verify(mockTypeRepository).findByName(typeName);
//        verify(mockProductRepository).save(product);
    }


    @Test
    void getReviewsForProduct() {

    }

    @Test
    void getSiteByUrl() {
    }

    @Test
    void getTypeByName() {
    }

    @Test
    void addReview() {
    }

    @Test
    void addReviews() {
    }

    @Test
    void addSite() {
    }

    @Test
    void addType() {
    }

    @Test
    void existReviewByHashId() {
    }
}