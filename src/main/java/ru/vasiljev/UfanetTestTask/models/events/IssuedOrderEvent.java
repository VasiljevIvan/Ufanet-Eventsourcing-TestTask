package ru.vasiljev.UfanetTestTask.models.events;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Immutable;

import static ru.vasiljev.UfanetTestTask.Constants.ISSUED;

@Entity
@DiscriminatorValue(ISSUED)
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class IssuedOrderEvent extends OrderEvent {

}
