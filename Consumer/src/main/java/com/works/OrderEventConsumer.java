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
            // İş mantığı idempotent olmalı; listener hatasında ack etmeden exception fırlatın.
            acknowledgment.acknowledge();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RetryableTopic(attempts = "4",
            exclude = { Exception.class },
            dltStrategy = DltStrategy.FAIL_ON_ERROR)
    @KafkaListener(topics = "orders.eventsx", groupId = "order-processor")
    void receive(OrderEvent e) {
        System.out.println("OrderEvent: " + e);
    }

    @DltHandler
    void dlt(OrderEvent e, @Header(KafkaHeaders.DLT_EXCEPTION_MESSAGE) String msg) {
        /* alarm + sakla */
    }

}
