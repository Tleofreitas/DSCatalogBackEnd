package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest // Não carrega controlador, services, etc
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository repository;

    // Fixtures - Para evitar repetição de código em muitos testes
    private long existingId;
    private long notExistingId;
    private long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        notExistingId = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
        // Arrange
        Product product = new Factory().createProduct();
        product.setId(null);

        // Act
        product = repository.save(product);

        // Assert
        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts+1L, product.getId());
    }

    @Test
    public void findByIdShouldReturnNotEmptyOptionalWhenIdExists() {
        // Arrange

        // Act
        Optional<Product> result = repository.findById(existingId);

        // Assert
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExists() {
        // Arrange

        // Act
        Optional<Product> result = repository.findById(notExistingId);

        // Assert
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void deleteShouldDeleteObjWhenIdExists() {
        // Arrange
        // existingId = 1L;

        // Act
        repository.deleteById(existingId);

        // Assert
        Optional<Product> result = repository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }
}
