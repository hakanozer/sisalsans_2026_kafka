package com.works;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderEventPublisher {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    public OrderEventPublisher(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CompletableFuture<SendResult<String, OrderEvent>> publish(
            String orderId, String status) {
        var event = new OrderEvent(orderId, status, Instant.now());
        return kafkaTemplate.send("orders.events", orderId, event)
                .whenComplete((result, error) -> {
                    if (error != null) {
                        // Üretimde logger/metric/notification ile hatayı görünür kılın.
                        System.err.println("Kafka publish failed: " + error.getMessage());
                    } else {
                        var metadata = result.getRecordMetadata();
                        System.out.printf("Published topic=%s partition=%d offset=%d%n",
                                metadata.topic(), metadata.partition(), metadata.offset());
                    }
                });
    }


}
