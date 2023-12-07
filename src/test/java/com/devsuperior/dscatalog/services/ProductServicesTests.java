package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class) // Testes de Unidade
public class ProductServicesTests {
    // Mocks servem para simular comportamento de um obj
    // Mockado = dados simulados
    @InjectMocks
    private ProductService service;
    @Mock
    private ProductRepository repository;

    // Fixtures - Para evitar repetição de código em muitos testes
    private long existingId;
    private long notExistingId;
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        notExistingId = 2L;
        dependentId = 3L;
        product = Factory.createProduct();
        page = new PageImpl<>(List.of(product));

        // Configurar o comportamento simulado do delete by id -----------------------

            // Quando chamar o delete com id existente, método não faz nada
            Mockito.doNothing().when(repository).deleteById(existingId);

            // Quando chamar o delete com Id dependente (com relação de integridade) lança Exceção
            Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

            // Se Id existe retorna true
            Mockito.when(repository.existsById(existingId)).thenReturn(true);

            // Se Id não existe retorna false
            Mockito.when(repository.existsById(notExistingId)).thenReturn(false);

            // Se for Id dependente (relação de integridade com outra entidade) retorna true
            Mockito.when(repository.existsById(dependentId)).thenReturn(true);

        // ----------------------------------------------------------------------------

        // Simulação do findAll retorna page contendo uma lista com produto
        Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);

        // Simulação do Save com etidade qualquer retorna produto
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        // Simulação do find by Id com id existente
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));

        // Simulação do find by Id com id inexistente
        Mockito.when(repository.findById(notExistingId)).thenReturn(Optional.empty());
    }

    @Test
    public void deleteByIdShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });

        // Verificar se o delete by id foi chamado no Mock
        Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(notExistingId);
        });
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
        Assertions.assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });
    }

    @Test
    public void findAllPagedShouldReturnPage() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<ProductDTO> result = service.findAllPaged(pageable);

        // Assertions
        Assertions.assertNotNull(result);
        Mockito.verify(repository).findAll(pageable);
    }
}
