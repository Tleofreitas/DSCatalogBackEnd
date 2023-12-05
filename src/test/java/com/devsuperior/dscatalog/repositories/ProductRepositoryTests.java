package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
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

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        notExistingId = 1000L;
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
