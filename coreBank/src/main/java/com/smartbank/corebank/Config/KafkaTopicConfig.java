package com.smartbank.corebank.Config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${bank_working.topic}")
    private String bankWorking;

    @Value("${transaction_response.topic}")
    private String transactionResponseTopic;

    @Bean
    public NewTopic bankWorkingTopic() {
        return TopicBuilder.name(bankWorking)
                .build();
    }

    @Bean
    public NewTopic transactionResponseTopic() {
        return TopicBuilder.name(transactionResponseTopic)
                .build();
    }
}
