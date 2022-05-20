package com.smartbank.validation;

import com.smartbank.validation.Model.Account;
import com.smartbank.validation.Model.Transaction;
import com.smartbank.validation.Repository.AccountRepository;
import com.smartbank.validation.Service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    private static boolean accountsInsertedCheck = false;

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
        accountsInsertedCheck = true;
        accountRepository.save(account);
        System.out.println(accountRepository.findAll());
    }

    @KafkaListener(
//            id= "transactionRequestListener",
//            autoStartup = "false",
            topics = "transaction_request",
            groupId = "groupId",
            containerFactory= "transactionKafkaListenerContainerFactory"
    )
    void transactionRequestListener(Transaction transaction) {
        if (accountsInsertedCheck) {
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