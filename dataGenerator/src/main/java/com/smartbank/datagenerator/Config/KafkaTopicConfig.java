package com.smartbank.datagenerator.Config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${account.topic}")
    private String ACCOUNT_TOPIC;

    @Value("${account.topic}")
    private String BANK_WORKING_TOPIC;

    @Value("${transaction_request.topic}")
    private String TRANSACTION_REQUEST_TOPIC;

    @Bean
    public NewTopic accountTopic() {
        return TopicBuilder.name(ACCOUNT_TOPIC)
                .build();
    }

    @Bean
    public NewTopic bankTopic() {
        return TopicBuilder.name(BANK_WORKING_TOPIC)
                .build();
    }

    @Bean
    public NewTopic transactionRequestTopic() {
        return TopicBuilder.name(TRANSACTION_REQUEST_TOPIC)
                .build();
    }
}
