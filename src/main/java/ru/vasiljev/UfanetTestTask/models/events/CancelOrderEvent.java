package ru.vasiljev.UfanetTestTask.models.events;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import static ru.vasiljev.UfanetTestTask.Constants.CANCEL;

@Entity
@DiscriminatorValue(CANCEL)
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CancelOrderEvent extends OrderEvent {
    private String cancelReason;
}
