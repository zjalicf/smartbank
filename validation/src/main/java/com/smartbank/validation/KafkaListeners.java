package com.smartbank.validation;

import com.smartbank.validation.Model.Account;
import com.smartbank.validation.Model.Transaction;
import com.smartbank.validation.Repository.AccountRepository;
import com.smartbank.validation.Service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    private static boolean accountsInsertedCheck = false;

    @Autowired
    KafkaListenerEndpointRegistry registry;

    @Autowired
    ValidationService validationService;

    @Autowired
    AccountRepository accountRepository;

    @KafkaListener (
            topics = "account",
            groupId = "groupId",
            containerFactory= "accountKafkaListenerContainerFactory"
    )
    void accountListener(Account account) {
        registry.getListenerContainer("x1").start();
        accountRepository.save(account);
    }

    @KafkaListener(
            id= "x1",
            autoStartup = "false",
            topics = "transaction_request",
            groupId = "groupId",
            containerFactory= "transactionKafkaListenerContainerFactory"
    )
    void transactionRequestListener(Transaction transaction) {
        validationService.validate(transaction);
    }

    @KafkaListener(
            topics = "transaction",
            groupId = "groupId",
            containerFactory= "transactionKafkaListenerContainerFactory"
    )
    void transactionListener(Transaction transaction) {
//        System.out.println("2. Received transaction done:" + transaction);
    }
}