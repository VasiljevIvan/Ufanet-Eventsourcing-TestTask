package ru.vasiljev.UfanetTestTask.models.events;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Immutable;
import ru.vasiljev.UfanetTestTask.models.Employee;
import ru.vasiljev.UfanetTestTask.models.OrderId;

import java.time.LocalDateTime;

@Entity
@Immutable
@Table(name = "order_event")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public abstract class OrderEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "val")
    private OrderId orderId;

    private String eventType;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;
}
