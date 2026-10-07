package com.works;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    @KafkaListener(topics = "orders.events", groupId = "order-processor",
            concurrency = "3")
    public void receive(ConsumerRecord<String, OrderEvent> record,
                        Acknowledgment acknowledgment) {
        try {
            Thread.sleep(3000);
            var event = record.value();
            System.out.printf("key=%s partition=%d offset=%d timestamp=%d%n",
                    record.key(), record.partition(), record.offset(), record.timestamp());
            System.out.printf("headers=%s order=%s status=%s%n",
                    record.headers(), event.orderId(), event.status());
            acknowledgment.acknowledge();
        } catch (Exception e) {
            throw new RuntimeException("Failed to process order event", e);
        }
    }

    @RetryableTopic(attempts = "4",
            dltStrategy = DltStrategy.FAIL_ON_ERROR)
    @KafkaListener(topics = "orders.events", groupId = "order-processor")
    void receiveRetry(OrderEvent e) {
        if (e == null || e.orderId() == null || e.status() == null) {
            throw new IllegalArgumentException("Invalid order event payload");
        }
        System.out.println("OrderEvent: " + e);
    }

    @DltHandler
    void dlt(OrderEvent e, @Header(KafkaHeaders.DLT_EXCEPTION_MESSAGE) String msg) {
        System.out.printf("Dead-letter topic received orderId=%s status=%s cause=%s%n",
                e.orderId(), e.status(), msg);
    }

}
