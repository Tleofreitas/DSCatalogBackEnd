package com.devsuperior.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsuperior.dscatalog.entities.Category;

// Camada de acesso aos dados (banco de dados)
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
