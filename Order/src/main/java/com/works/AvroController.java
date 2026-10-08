package com.works;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("avro")
public class AvroController {


    private final AvroOrderPublisher avroOrderPublisher;
    public AvroController(AvroOrderPublisher avroOrderPublisher) {
        this.avroOrderPublisher = avroOrderPublisher;
    }

    @PostMapping("add")
    public ResponseEntity addOrder() {
        String uuid = java.util.UUID.randomUUID().toString();
        long randon = (long) (Math.random() * 1000);
        avroOrderPublisher.send(uuid, randon)
                .thenAccept(result -> System.out.println("Avro order event published successfully for orderId: order-1"))
                .exceptionally(ex -> {
                    System.err.println("Failed to publish Avro order event for orderId: order-1, error: " + ex.getMessage());
                    return null;
                });
        return ResponseEntity.accepted().build();
    }

}
