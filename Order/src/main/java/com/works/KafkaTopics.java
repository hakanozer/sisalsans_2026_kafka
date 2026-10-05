package com.works;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopics {
    @Bean
    NewTopic ordersTopic() {
        return TopicBuilder.name("orders.events")
                .partitions(3).replicas(1).build();
    }

    @Bean
    NewTopic ordersDltTopic() {
        return TopicBuilder.name("orders.events.DLT")
                .partitions(3).replicas(1).build();
    }
}
