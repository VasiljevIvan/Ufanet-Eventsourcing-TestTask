package ru.vasiljev.UfanetTestTask.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vasiljev.UfanetTestTask.models.Employee;

public interface EmployeesRepository extends JpaRepository<Employee, Integer> {

}
