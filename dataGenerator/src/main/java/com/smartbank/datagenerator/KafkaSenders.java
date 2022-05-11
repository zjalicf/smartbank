package com.smartbank.datagenerator;

import com.smartbank.datagenerator.Model.Account;
import com.smartbank.datagenerator.Model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSenders {

    @Autowired
    private KafkaTemplate<String, Account> accountKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, Transaction> transactionKafkaTemplate;

    public void sendAccount(Account account) {
        accountKafkaTemplate.send("account", account);
    }

    public void sendTransaction(Transaction transaction) {
        transactionKafkaTemplate.send("transaction_request", transaction);
    }
}
