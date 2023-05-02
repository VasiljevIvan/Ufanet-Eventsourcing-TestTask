package ru.vasiljev.UfanetTestTask.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vasiljev.UfanetTestTask.models.Product;

public interface ProductsRepository extends JpaRepository<Product, Integer> {

}
