package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

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

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        notExistingId = 1000L;

        // Configurar o comportamento simulado do delete by id -----------------------

            // Quando chamar o delete com id existente, método não faz nada
            Mockito.doNothing().when(repository).deleteById(existingId);

            // Se Id existe retorna true
            Mockito.when(repository.existsById(existingId)).thenReturn(true);

            // Se Id não existe retorna false
            Mockito.when(repository.existsById(notExistingId)).thenReturn(false);

            // Se for Id dependente (relação de integridade com outra entidade) retorna true
            // Mockito.when(repository.existsById(dependentId)).thenReturn(true);

        // ----------------------------------------------------------------------------
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
}
