package com.works;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("orders")
public class OrderController {

    private final OrderEventPublisher orderEventPublisher;
    public OrderController(OrderEventPublisher orderEventPublisher) {
        this.orderEventPublisher = orderEventPublisher;
    }

    @PostMapping("add")
    public ResponseEntity addOrder() {
        // Simulate order creation
        String orderId = "order-" + System.currentTimeMillis();
        String status = "CREATED";

        // Publish order event to Kafka
        orderEventPublisher.publish(orderId, status)
                .thenAccept(result -> {
                    // Handle successful publish if needed
                    System.out.println("Order event published successfully for orderId: " + orderId);
                })
                .exceptionally(ex -> {
                    // Handle publish failure if needed
                    System.err.println("Failed to publish order event for orderId: " + orderId + ", error: " + ex.getMessage());
                    return null;
                });
        return ResponseEntity.ok().build();
    }

}
