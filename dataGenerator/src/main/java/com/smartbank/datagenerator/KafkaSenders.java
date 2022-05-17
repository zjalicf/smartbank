package com.smartbank.datagenerator;

import com.smartbank.datagenerator.Model.Account;
import com.smartbank.datagenerator.Model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSenders {

    @Value("${account.topic}")
    private String ACCOUNT_TOPIC;

    @Value("${transaction_request.topic}")
    private String TRANSACTION_REQUEST_TOPIC;

    @Autowired
    private KafkaTemplate<String, Account> accountKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, Transaction> transactionKafkaTemplate;

    public void sendAccount(Account account) {
        accountKafkaTemplate.send(ACCOUNT_TOPIC, account);
    }

    public void sendTransaction(Transaction transaction) {
        transactionKafkaTemplate.send(TRANSACTION_REQUEST_TOPIC, transaction);
    }
}
