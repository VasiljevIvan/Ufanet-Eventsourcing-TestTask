package ru.vasiljev.UfanetTestTask.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vasiljev.UfanetTestTask.dto.OrderEventDTO;
import ru.vasiljev.UfanetTestTask.dto.Order;
import ru.vasiljev.UfanetTestTask.models.events.*;
import ru.vasiljev.UfanetTestTask.services.OrderServiceImpl;
import ru.vasiljev.UfanetTestTask.util.ErrorResponse;
import ru.vasiljev.UfanetTestTask.services.OrderEventCreator;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/events")
public class OrderEventController {
    private final OrderEventCreator orderEventCreator;
    private final OrderServiceImpl orderService;

    public OrderEventController(OrderEventCreator orderEventCreator, OrderServiceImpl orderService) {
        this.orderEventCreator = orderEventCreator;
        this.orderService = orderService;
    }

    @PostMapping("/registration")
    public ResponseEntity<Order> reg(@RequestBody OrderEventDTO orderEventDTO) {
        RegisterOrderEvent registerOrderEvent = orderEventCreator.createRegisterOrderEvent(orderEventDTO);
        orderService.publishEvent(registerOrderEvent);
        return new ResponseEntity<>(orderService.findOrder(registerOrderEvent.getOrderId()), HttpStatus.OK);
    }

    @PostMapping("/cancel")
    public ResponseEntity<Order> ccl(@RequestBody OrderEventDTO orderEventDTO) {
        CancelOrderEvent cancelOrderEvent = orderEventCreator.createCancelOrderEvent(orderEventDTO);
        orderService.publishEvent(cancelOrderEvent);
        return new ResponseEntity<>(orderService.findOrder(cancelOrderEvent.getOrderId()), HttpStatus.OK);
    }

    @PostMapping("/taken_to_work")
    public ResponseEntity<Order> wrk(@RequestBody OrderEventDTO orderEventDTO) {
        TakenToWorkOrderEvent takenToWorkOrderEvent = orderEventCreator.createTakenToWorkOrderEvent(orderEventDTO);
        orderService.publishEvent(takenToWorkOrderEvent);
        return new ResponseEntity<>(orderService.findOrder(takenToWorkOrderEvent.getOrderId()), HttpStatus.OK);
    }

    @PostMapping("/ready")
    public ResponseEntity<Order> rdy(@RequestBody OrderEventDTO orderEventDTO) {
        ReadyOrderEvent readyOrderEvent = orderEventCreator.createReadyOrderEvent(orderEventDTO);
        orderService.publishEvent(readyOrderEvent);
        return new ResponseEntity<>(orderService.findOrder(readyOrderEvent.getOrderId()), HttpStatus.OK);
    }

    @PostMapping("/issued")
    public ResponseEntity<Order> iss(@RequestBody OrderEventDTO orderEventDTO) {
        IssuedOrderEvent issuedOrderEvent = orderEventCreator.createIssuedOrderEvent(orderEventDTO);
        orderService.publishEvent(issuedOrderEvent);
        return new ResponseEntity<>(orderService.findOrder(issuedOrderEvent.getOrderId()), HttpStatus.OK);
    }

    @PostMapping("/order/{id}")
    public ResponseEntity<Order> order(@PathVariable("id") int id) {
        return new ResponseEntity<>(orderService.findOrder(id), HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(), LocalDateTime.now());
        log.error(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
