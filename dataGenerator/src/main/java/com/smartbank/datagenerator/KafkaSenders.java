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
    private String accountTopic;

    @Value("${transaction_request.topic}")
    private String transactionRequestTopic;

    @Autowired
    private KafkaTemplate<String, Account> accountKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, Transaction> transactionKafkaTemplate;

    public void sendAccount(Account account) {
        accountKafkaTemplate.send(accountTopic, account);
    }

    public void sendTransaction(Transaction transaction) {
        transactionKafkaTemplate.send(transactionRequestTopic, transaction);
    }
}
