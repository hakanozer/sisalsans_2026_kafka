package com.works;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
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
            // İş mantığı idempotent olmalı; listener hatasında ack etmeden exception fırlatın.
            acknowledgment.acknowledge();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
