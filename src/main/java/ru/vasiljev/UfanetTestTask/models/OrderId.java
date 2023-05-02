package ru.vasiljev.UfanetTestTask.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_id")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int val;
}
