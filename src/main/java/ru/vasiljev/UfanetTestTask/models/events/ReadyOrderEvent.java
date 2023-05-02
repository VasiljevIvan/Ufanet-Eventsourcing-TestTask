package ru.vasiljev.UfanetTestTask.models.events;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static ru.vasiljev.UfanetTestTask.Constants.READY;

@Entity
@DiscriminatorValue(READY)
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ReadyOrderEvent extends OrderEvent {

}