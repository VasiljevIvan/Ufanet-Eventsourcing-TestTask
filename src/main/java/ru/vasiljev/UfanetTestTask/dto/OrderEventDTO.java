package ru.vasiljev.UfanetTestTask.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderEventDTO {
    private Integer orderId;
    private Integer employeeId;
    private Integer customerId;
    private Integer productId;
    private String cancelReason;
}