package com.smartbank.validation.Config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${account.topic}")
    private String accountTopic;

    @Value("${transaction_request.topic}")
    private String transaction_requestTopic;

    @Value("${transaction_response.topic}")
    private String transaction_responseTopic;

    @Value("${transaction.topic}")
    private String transactionTopic;

    @Bean
    public NewTopic accountTopic() {
        return TopicBuilder.name(accountTopic)
                .build();
    }

    @Bean
    public NewTopic transactionRequestTopic() {
        return TopicBuilder.name(transaction_requestTopic)
                .build();
    }

    @Bean
    public NewTopic transactionResponseTopic() {
        return TopicBuilder.name(transaction_responseTopic)
                .build();
    }

    @Bean
    public NewTopic transactionTopic() {
        return TopicBuilder.name(transactionTopic)
                .build();
    }
}
