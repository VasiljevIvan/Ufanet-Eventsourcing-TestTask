package ru.vasiljev.UfanetTestTask.models.events;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static ru.vasiljev.UfanetTestTask.Constants.TAKEN_TO_WORK;

@Entity
@DiscriminatorValue(TAKEN_TO_WORK)
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class TakenToWorkOrderEvent extends OrderEvent {

}