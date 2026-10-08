package com.works;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AvroOrderPublisher {
    private final KafkaTemplate<String, OrderCreated> template;

    public AvroOrderPublisher(KafkaTemplate<String, OrderCreated> template) {
        this.template = template;
    }

    public CompletableFuture<SendResult<String, OrderCreated>> send(
            String orderId, long amountCents) {
        var event = OrderCreated.newBuilder()
                .setOrderId(orderId)
                .setAmountCents(amountCents)
                .setCurrency("TRY")
                .build();
        return template.send("orders.avro", orderId, event);
    }

}
