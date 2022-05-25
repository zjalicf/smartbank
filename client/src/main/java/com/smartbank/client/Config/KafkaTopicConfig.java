package com.smartbank.client.Config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${account.topic}")
    private String accountTopic;

    @Value("${transaction.topic}")
    private String transactionTopic;

    @Value("${saldo_update.topic}")
    private String saldoUpdateTopic;

    @Value("${amount_update.topic}")
    private String amountUpdateTopic;

    @Bean
    public NewTopic accountTopic() {
        return TopicBuilder.name(accountTopic)
                .build();
    }

    @Bean
    public NewTopic transactionTopic() {
        return TopicBuilder.name(transactionTopic)
                .build();
    }

    @Bean
    public NewTopic saldoUpdateTopic() {
        return TopicBuilder.name(saldoUpdateTopic)
                .build();
    }

    @Bean
    public NewTopic amountUpdateTopic() {
        return TopicBuilder.name(amountUpdateTopic)
                .build();
    }
}
