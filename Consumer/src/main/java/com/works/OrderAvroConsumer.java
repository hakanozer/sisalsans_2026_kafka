package com.works;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class OrderAvroConsumer {

    @KafkaListener(topics = "orders.avro", groupId = "order-processor")
    public void listen(ConsumerRecord<String, OrderCreated> record,
                       Acknowledgment acknowledgment) {
        OrderCreated orderCreated = record.value();
        System.out.println("Received Avro order message: " + orderCreated);
        acknowledgment.acknowledge();
    }

}
