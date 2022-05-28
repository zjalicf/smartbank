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
    private String bankWorkingTopic;

    @Value("${transaction_request.topic}")
    private String transactionRequestTopic;

    @Bean
    public NewTopic accountTopic() {
        return TopicBuilder.name(accountTopic)
                .build();
    }

    @Bean
    public NewTopic bankTopic() {
        return TopicBuilder.name(bankWorkingTopic)
                .build();
    }

    @Bean
    public NewTopic transactionRequestTopic() {
        return TopicBuilder.name(transactionRequestTopic)
                .build();
    }
}
