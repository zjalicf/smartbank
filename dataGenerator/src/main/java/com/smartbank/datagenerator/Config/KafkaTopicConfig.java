package com.smartbank.datagenerator.Config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${account.topic}")
    private String accountTopic;

    @Value("${account.topic}")
    private String bank_workingTopic;

    @Value("${transaction_request.topic}")
    private String transaction_requestTopic;

    @Bean
    public NewTopic accountTopic() {
        return TopicBuilder.name(accountTopic)
                .build();
    }

    @Bean
    public NewTopic bankTopic() {
        return TopicBuilder.name(bank_workingTopic)
                .build();
    }

    @Bean
    public NewTopic transactionRequestTopic() {
        return TopicBuilder.name(transaction_requestTopic)
                .build();
    }
}
