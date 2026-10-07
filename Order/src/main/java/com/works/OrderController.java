package com.works;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("orders")
public class OrderController {

    private final OrderEventPublisher orderEventPublisher;

    public OrderController(OrderEventPublisher orderEventPublisher) {
        this.orderEventPublisher = orderEventPublisher;
    }

    @PostMapping("add")
    public ResponseEntity<Void> addOrder(@RequestParam String orderId,
                                        @RequestParam(defaultValue = "CREATED") String status) {
        if (orderId == null || orderId.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        orderEventPublisher.publish(orderId, status)
                .thenAccept(result -> System.out.println("Order event published successfully for orderId: " + orderId))
                .exceptionally(ex -> {
                    System.err.println("Failed to publish order event for orderId: " + orderId + ", error: " + ex.getMessage());
                    return null;
                });
        return ResponseEntity.accepted().build();
    }

}
