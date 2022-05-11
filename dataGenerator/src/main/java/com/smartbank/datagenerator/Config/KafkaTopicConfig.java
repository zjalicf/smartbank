package com.smartbank.datagenerator.Config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic accountTopic() {
        return TopicBuilder.name("account")
                .build();
    }

    @Bean
    public NewTopic bankTopic() {
        return TopicBuilder.name("bank_working")
                .build();
    }

    @Bean
    public NewTopic transactionRequestTopic() {
        return TopicBuilder.name("transaction_request")
                .build();
    }
}
