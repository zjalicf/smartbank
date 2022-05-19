package com.smartbank.validation;

import com.smartbank.validation.Model.Account;
import com.smartbank.validation.Model.Transaction;
import com.smartbank.validation.Repository.AccountRepository;
import com.smartbank.validation.Service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    private static boolean proba = false;

    @Autowired
    ValidationService validationService;

    @Autowired
    AccountRepository accountService;

    @KafkaListener (
            topics = "account",
            groupId = "groupId",
            containerFactory= "accountKafkaListenerContainerFactory"
    )
    void accountListener(Account account) {
        proba = true;
        accountService.save(account);
        System.out.println(accountService.findAll());
    }

    @KafkaListener(
            id= "transactionRequestListener",
//            autoStartup = "false",
            topics = "transaction_request",
            groupId = "groupId",
            containerFactory= "transactionKafkaListenerContainerFactory"
    )
    void transactionRequestListener(Transaction transaction) {
        if (proba) {
            validationService.validate(transaction);
        }
    }

    @KafkaListener(
            topics = "transaction",
            groupId = "groupId",
            containerFactory= "transactionKafkaListenerContainerFactory"
    )
    void transactionListener(Transaction transaction) {
        System.out.println("2. Received transaction done:" + transaction);
    }
}