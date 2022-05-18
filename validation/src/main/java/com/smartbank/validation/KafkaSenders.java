package com.smartbank.validation;

import com.smartbank.validation.Model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSenders {

    @Value("${transaction.topic}")
    private String transactionTopic;

    @Autowired
    private KafkaTemplate<String, Transaction> transactionKafkaTemplate;

    public void sendTransaction(Transaction transaction) {
        transactionKafkaTemplate.send("transaction_response", transaction);
    }
}
