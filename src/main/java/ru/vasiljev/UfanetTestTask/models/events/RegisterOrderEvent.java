package ru.vasiljev.UfanetTestTask.models.events;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.vasiljev.UfanetTestTask.models.Customer;
import ru.vasiljev.UfanetTestTask.models.Product;

import java.time.LocalDateTime;

import static ru.vasiljev.UfanetTestTask.Constants.REGISTER;

@Entity
@DiscriminatorValue(REGISTER)
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class RegisterOrderEvent extends OrderEvent {

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    private double productPrice;

    private LocalDateTime expectedCompletionTime;
}
