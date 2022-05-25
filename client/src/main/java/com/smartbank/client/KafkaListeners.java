package com.smartbank.client;

import com.smartbank.client.Model.Account;
import com.smartbank.client.Model.Transaction;
import com.smartbank.client.Repository.AccountRepository;
import com.smartbank.client.Service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    KafkaListenerEndpointRegistry registry;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientService clientService;

    @KafkaListener (
            topics = "account",
            groupId = "account-client",
            containerFactory= "accountKafkaListenerContainerFactory"
    )
    void accountListener(Account account) {
        accountRepository.save(account);
        registry.getListenerContainer("transactionListener").start();
    }

    @KafkaListener(
            id= "transactionListener",
            autoStartup = "false",
            topics = "transaction",
            groupId = "transaction-client",
            containerFactory= "transactionKafkaListenerContainerFactory"
    )
    void transactionRequestListener(Transaction transaction) {
        clientService.processTransaction(transaction);
    }
}